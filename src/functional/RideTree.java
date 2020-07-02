// written by Tristan Nunn (wdb18160), June 12020HE, for the CS251 Project

package functional;

import data_structures.Preferences;
import data_structures.Ride;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RideTree
{
  private class Node
  {
    int[] path;
    public List<Ride> compatibleRides = new ArrayList<>();
    public Node leftNode = null;
    public Node rightNode = null;

    public Node(int[] arr, int toSet)
    {
      path = Arrays.copyOf(arr, arr.length);
      int point = 0;
      while (point < 5)
      {
        if (arr[point] == -1)
          break;
        else
          point++;
      }
      path[point] = toSet;
    }
  }

  private Node root;

  public RideTree(List<Ride> rides)
  {
    Node newRoot = new Node(new int[] {-1, -1, -1, -1, -1}, -1);
    this.root = newRoot;
    for (Ride r : rides)
    {
      fillRide(r);
    }
  }

  private void fillRide(Ride r)
  {
    Node current = root;
    current = fillRideHelper(current, r.wheelchair);
    current = fillRideHelper(current, r.kids);
    current = fillRideHelper(current, r.adrenaline);
    current = fillRideHelper(current, r.horror);
    current = fillRideHelper(current, r.water);
    current.compatibleRides.add(r);
  }

  private Node fillRideHelper(Node current, boolean pathChoice)
  {
    if (pathChoice)
    {
      if (current.rightNode == null)
        current.rightNode = new Node(current.path, 1);
      return current.rightNode;
    }
    else
    {
      if (current.leftNode == null)
        current.leftNode = new Node(current.path, 0);
      return current.leftNode;
    }
  }

  public Ride getSingleRide()
  {
    Stack<Node> nodes = new Stack<>();
    nodes.push(root);
    List<Ride> rides = new ArrayList<>();

    while (!nodes.empty() && rides.size() == 0)
    {
      Node thisNode = nodes.pop();
      if (thisNode.leftNode != null)
        nodes.push(thisNode.leftNode);
      if (thisNode.rightNode != null)
        nodes.push(thisNode.rightNode);
      rides.addAll(thisNode.compatibleRides);
    }
    return rides.get(0);
  }

  public List<Ride> getMultipleRides(Preferences p)
  {
    List<Node> activeNodes = new ArrayList<>();
    activeNodes.add(root);

    int wheelchairCount = 0;
    for (int i = 0; i < p.getWheelchairCount().length; i++)
      if (p.getWheelchairCount()[i])
        wheelchairCount++;

    activeNodes = getMultipleRidesWheelchairHelper(activeNodes, wheelchairCount, p.partySize);
    activeNodes = getMultipleRidesHelper(activeNodes, p.getKid_likes() + p.getKid_loves(), p.partySize);
    activeNodes = getMultipleRidesHelper(activeNodes, p.getAdren_likes() + p.getAdren_loves(), p.partySize);
    activeNodes = getMultipleRidesHelper(activeNodes, p.getHorror_likes() + p.getHorror_loves(), p.partySize);
    activeNodes = getMultipleRidesHelper(activeNodes, p.getWater_likes() + p.getWater_loves(), p.partySize);

    return activeNodes.stream()
        .map(node -> {return node.compatibleRides;}) // extract rides to List<List<Ride>>
        .flatMap(List::stream) // flatten to List<Ride>
        .filter(ride -> {return new Compatibility(ride, p, true).isCompatible();}) // remove incompatible rides
        .collect(Collectors.toList());
  }

  private List<Node> getMultipleRidesHelper(List<Node> nodes, int count, int max)
  {
    List<Node> toReturn = new ArrayList<>();
    for (Node n : nodes)
    {
      if (count > max/2) // if party doesn't not like a certain thing
        if (n.rightNode != null)
          toReturn.add(n.rightNode);
      if (n.leftNode != null)
      toReturn.add(n.leftNode);
    }
    return toReturn;
  }

  private List<Node> getMultipleRidesWheelchairHelper(List<Node> nodes, int count, int max)
  {
    List<Node> toReturn = new ArrayList<>();
    for (Node n : nodes)
    {
      if (count <= max/2) // if the party has a minority of wheelchair users
        toReturn.add(n.leftNode); // return non-accessible rides
      toReturn.add(n.rightNode); // always return rides suitable for wheelchair users
    }
    return toReturn;
  }
}
