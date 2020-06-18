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

public class Reccomendations
{
  private static class RideNode
  {
    public Ride ride;
    public boolean visited = false;
    public int shortestPath = Integer.MAX_VALUE;
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

  private static void makeMap()
  {
    List<RideNode> nodes = minDistance(null);

    List<List<RideNode>> nodesByZone = new ArrayList<>(RideHandler.ZONES.size());
    for (int i = 0; i <  RideHandler.ZONES.size(); i++)
      nodesByZone.add(new ArrayList<>());

    for (RideNode node : nodes)
      for (int i = 0; i < RideHandler.ZONES.size(); i++)
        if (node.ride.category.equals(RideHandler.ZONES.get(i)))
          nodesByZone.get(i).add(node);

    for (int i = 0; i < nodesByZone.size(); i++)
    {
      System.out.println();
      System.out.println(RideHandler.ZONES.get(i) + " zone:");
      System.out.println(makeSpace("Ride name", 25) + "Distance from park entrance (metres)");

      for (RideNode r : nodesByZone.get(i))
      {
        System.out.println();
        System.out.println(makeSpace(r.ride.name, 25) + r.shortestPath);
      }

    }
    System.out.println();
  }

  // Using Dijkstra's algorithm, I hope
  private static List<RideNode> minDistance(Ride source)
  {
    Map<Ride, Map<Ride, Integer>> paths = PathHandler.PATHS;

    List<RideNode> r = makeRideNodes();

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
        for (Ride destination : paths.get(sourceNode.ride).keySet())
        {
          RideNode destinationNode = getRideNode(destination, r);
          int alternatePathSize = sourceNode.shortestPath + paths.get(sourceNode.ride).get(destination);
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
      toAdd.ride = r;
      nodes.add(toAdd);
    }

    return nodes;
  }

  private static RideNode getRideNode(Ride r, List<RideNode> n)
  {
    for (RideNode node : n)
      if (node.ride == r)
        return node;

    return null;
  }

}
