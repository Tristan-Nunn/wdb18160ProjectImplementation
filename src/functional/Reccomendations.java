package functional;

import cli_components.Get;
import config.PathHandler;
import config.RideHandler;
import data_structures.Preferences;
import data_structures.Ride;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Reccomendations
{
  private static class RideNode
  {
    public Ride targetRide;
    public boolean visited = false;
    public int shortestPath = Integer.MAX_VALUE;
  }

  private static class RidePath
  {
    public Ride source;
    public Ride destination;
    public int weight;
  }

  public static void recommend(Preferences p, RideTree rt, int recKey)
  {
    switch (recKey)
    {
      case 1:
        recommendSingleRide(p, rt);
        break;
      case 2:
        recommendEntirePark(p, rt);
        break;
      case 3:
        makeMap();
        break;
      case 4:
        getRoute(p, rt);
    }
  }

  private static void recommendSingleRide(Preferences p, RideTree rt)
  {

    Ride r = rt.getSingleRide();
    printCompatibility(p, r);

    while (Get.boolFromUser("Would you like to revise your preferences? (Y/N) "))
    {
      p = Preferences.createNewParty(p.leaderName, p.leaderEmail);
      System.out.println();
      printCompatibility(p, r);
    }
  }

  private static void recommendEntirePark(Preferences p, RideTree rt)
  {
    List<Ride> rides = rt.getMultipleRides(p);
    Map<String, List<Ride>> ridesByZone = arrangeRidesByZone(rides);
    printCompatibility(ridesByZone);

    while (Get.boolFromUser("Would you like to revise your preferences? (Y/N) "))
    {
      p = Preferences.createNewParty(p.leaderName, p.leaderEmail);
      rides = rt.getMultipleRides(p);
      ridesByZone = arrangeRidesByZone(rides);
      System.out.println();
      printCompatibility(ridesByZone);
    }
  }

  // Using Kruskal's algorithm, I hope
  private static List<RidePath> getRoute(Preferences p, RideTree rt)
  {
    // 1. Find the minimum distance between each of the rides the group wants to visit
    // 2. Turn this into weight.
    // 3. Order this by Ride - List<Path>
    // 4. Select the shortest path out there
    // 4. Combine the two sources into List<Ride source + Ride dest> - List<Path sourcePaths + Path destPaths>
    // 6. Move the chosen path into its own List
    // 7. Keep combining until all rides are in the List<Rides> - List<paths>
    // 8. Keep adding the chosen paths to the list of chosen paths
    // 9. At the end the List<Paths> should be empty (Since all redundant paths are being removed)
    // 10 Return the chosen paths

    List<Ride> rides = rt.getMultipleRides(p);

    List<RidePath> finalPaths = new ArrayList<>();
    List<RidePath> pathWeights = new LinkedList<>();
    Map<Ride, List<RideNode>> nodeWeights = new HashMap<>();

    for (Ride r : rides)
    {
      List<RideNode> rawNodes = minDistance(r);
      List<RideNode> filteredNodes = rawNodes.stream().filter(n->{return rides.contains(n.targetRide) && n.targetRide != r;}).collect(Collectors.toList());
      nodeWeights.put(r, filteredNodes);
    }

    for (Map.Entry<Ride, List<RideNode>> r : nodeWeights.entrySet())
    {
      for (RideNode node : r.getValue())
      {
        RidePath newPath = new RidePath();
        newPath.source = r.getKey();
        newPath.destination = node.targetRide;
        newPath.weight = getWeight(node.shortestPath, p, node.targetRide);
        pathWeights.add(newPath);
      }
    }

    List<List<Ride>> clouds = new LinkedList<>();
    while (!pathWeights.isEmpty())
    {
      RidePath shortest = getShortestPath(pathWeights);
      finalPaths.add(shortest);
      removeInternalDestinations(pathWeights, clouds, shortest);
    }

    // also add the path from the entrance
    List<RideNode> nodesFromEntrace = minDistance(null);
    int minWeight = Integer.MAX_VALUE;
    Ride minWeightRide = null;
    for (RideNode node : nodesFromEntrace)
    {
      int weight = getWeight(node.shortestPath, p, node.targetRide);
      if (minWeight > weight)
      {
        minWeight = weight;
        minWeightRide = node.targetRide;
      }
    }
    RidePath entrance = new RidePath();
    entrance.source = null;
    entrance.destination = minWeightRide;
    entrance.weight = minWeight;
    finalPaths.add(entrance);

    return finalPaths;
  }

  private static void makeMap()
  {
    List<RideNode> nodes = minDistance(null);

    List<List<RideNode>> nodesByZone = new ArrayList<>(RideHandler.ZONES.size());
    for (int i = 0; i <  RideHandler.ZONES.size(); i++)
      nodesByZone.add(new ArrayList<>());

    for (RideNode node : nodes)
      for (int i = 0; i < RideHandler.ZONES.size(); i++)
        if (node.targetRide.category.equals(RideHandler.ZONES.get(i)))
          nodesByZone.get(i).add(node);

    for (int i = 0; i < nodesByZone.size(); i++)
    {
      System.out.println();
      System.out.println(RideHandler.ZONES.get(i) + " zone:");
      System.out.println(makeSpace("Ride name", 25) + "Distance from park entrance (metres)");

      for (RideNode r : nodesByZone.get(i))
      {
        System.out.println();
        System.out.println(makeSpace(r.targetRide.name, 25) + r.shortestPath);
      }

    }
    System.out.println();
  }

  // Using Dijkstra's algorithm, I hope
  private static List<RideNode> minDistance(Ride source)
  {
    Map<Ride, Map<Ride, Integer>> paths = PathHandler.PATHS;

    List<RideNode> r = makeRideNodes();

    if (source != null)
    {
      RideNode sourceNode = getRideNode(source, r);
      if (sourceNode != null)
        sourceNode.visited = true;
    }

    Queue<RideNode> toReview = new LinkedList<>();

    for (Ride destination : paths.get(source).keySet())
    {
      RideNode node = getRideNode(destination, r);
      node.shortestPath = paths.get(source).get(destination);
      toReview.add(node);
    }

    while (!toReview.isEmpty())
    {
      RideNode sourceNode = ((LinkedList<RideNode>) toReview).pop();
      if (!sourceNode.visited)
      {
        sourceNode.visited = true;
        for (Ride destination : paths.get(sourceNode.targetRide).keySet())
        {
          RideNode destinationNode = getRideNode(destination, r);
          int alternatePathSize = sourceNode.shortestPath + paths.get(sourceNode.targetRide).get(destination);
          destinationNode.shortestPath = destinationNode.shortestPath < alternatePathSize ? destinationNode.shortestPath : alternatePathSize;
          toReview.add(destinationNode);
        }
      }
    }

    return r;
  }

  private static Map<String, List<Ride>> arrangeRidesByZone(List<Ride> rides)
  {
    Map<String, List<Ride>> ridesByZone = new HashMap<>();

    for (String zone : RideHandler.ZONES)
    {
      List<Ride> ridesInZone = new ArrayList<>();
      for (Ride r : rides)
      {
        if (r.category.equals(zone))
          ridesInZone.add(r);
      }
      ridesByZone.put(zone, ridesInZone);
    }
    return ridesByZone;
  }

  private static void printCompatibility(Preferences prefs, Ride r)
  {
    Compatibility c = new Compatibility(r, prefs, false);

    System.out.println();
    System.out.println("Recommendations for " + prefs.leaderName + "'s party created on "
        + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))
        + " @" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm:")));
    System.out.println();
    System.out.println(r.name);

    if (c.isCompatible())
    {
      System.out.println("Based on your inputs this ride is suitable for your party.");
    }
    else
    {
      System.out.println("Based on your inputs this ride is not suitable for your party because:");
      System.out.println(c.getReason());
    }
  }

  private static void printCompatibility(Map<String, List<Ride>> ridesByZone)
  {
    for (Map.Entry<String, List<Ride>> ridesInZone : ridesByZone.entrySet())
    {
      System.out.println(ridesInZone.getKey() + ":");
      if (ridesInZone.getValue().size() == 0)
      {
        System.out.println("Based on your preferences, we found no rides in this zone.");
      }
      else
      {
        for (Ride r : ridesInZone.getValue())
        {
          System.out.println(r.name);
        }
      }
      System.out.println();
    }
  }

  private static String makeSpace(String afterThis, int columnWidth)
  {
    StringBuilder builder = new StringBuilder();
    builder.append(afterThis);

    for (int i = afterThis.length(); i < columnWidth; i++)
    {
      builder.append(' ');
    }

    return builder.toString();
  }

  private static List<RideNode> makeRideNodes()
  {
    List<RideNode> nodes = new ArrayList<>(RideHandler.RIDES.size());

    for (Ride r : RideHandler.RIDES)
    {
      RideNode toAdd = new RideNode();
      toAdd.targetRide = r;
      nodes.add(toAdd);
    }

    return nodes;
  }

  private static RideNode getRideNode(Ride r, List<RideNode> n)
  {
    for (RideNode node : n)
      if (node.targetRide == r)
        return node;

    return null;
  }

  private static Integer getWeight(int shortestPath, Preferences p, Ride ride)
  {
    int preferenceWeight = 0;

    // Only the people who really want to go on this ride type reduce the weighting.
    preferenceWeight += ride.adrenaline ? p.getAdren_loves() : 0;
    preferenceWeight += ride.kids ? p.getKid_loves() : 0;
    preferenceWeight += ride.horror ? p.getHorror_loves() : 0;
    preferenceWeight += ride.water ? p.getWater_loves() : 0;

    // scales the preference weight up to 200.
    preferenceWeight = preferenceWeight * 200 / p.partySize;

    // weighting wait time as 5 times more boring than walk time
    return shortestPath + (5*ride.waitTime) - preferenceWeight;
  }

  private static RidePath getShortestPath(List<RidePath> paths)
  {
    RidePath shortest = paths.get(0);
    Iterator<RidePath> it = paths.iterator();
    while (it.hasNext())
    {
      RidePath next = it.next();
      if (next.weight < shortest.weight)
        shortest = next;
    }
    return shortest;
  }

  private static void removeInternalDestinations(List<RidePath> paths, List<List<Ride>> clouds, RidePath newPath)
  {
    List<Ride> sourceCloud = null;
    List<Ride> destinationCloud = null;
    for (List<Ride> cloud : clouds)
    {
      boolean containsSource = cloud.contains(newPath.source);
      boolean containsDestination = cloud.contains(newPath.destination);
      if (containsSource && containsDestination)
      {
        sourceCloud = cloud;
      } else if (containsSource)
        sourceCloud = cloud;
      else if (containsDestination)
        destinationCloud = cloud;
    }

    // I have the cloud (list) which contains the source ride (if any)
    // I have the cloud (list) which contains the destination ride (if any)
    // I need to:
    //  - clouds are the same: remove all paths to each of the rides from within that list
    //  - clouds are different: merge the lists and remove all internal paths from within the new list
    //  - one cloud exists: add the not found path to the existing list and remove all internal paths from within that list
    //  - no cloud exists: create a new one with the source and destination and add it to the clouds

    boolean useDestination = false;
    if (sourceCloud != null && destinationCloud != null) // clouds are different
    {
      sourceCloud.addAll(destinationCloud);
      clouds.remove(destinationCloud);
    } else if (sourceCloud != null)
    {
      sourceCloud.add(newPath.destination);
    } else if (destinationCloud != null)
    {
      destinationCloud.add(newPath.source);
      useDestination = true;
    } else
    {
      List<Ride> cloud = new ArrayList<>();
      cloud.add(newPath.source);
      cloud.add(newPath.destination);
      clouds.add(cloud);
      sourceCloud = cloud;
    }

    Iterator<RidePath> it = paths.iterator();
    while (it.hasNext())
    {
      RidePath path = it.next();
      if (useDestination)
      {
        if (destinationCloud.contains(path.destination) && destinationCloud.contains(path.source))
          it.remove();
      }
      else
      {
        if (sourceCloud.contains(path.destination) && sourceCloud.contains(path.source))
          it.remove();
      }
    }

  }
}
