package functional;

import data_structures.Ride;

public class RideNode
{
  private Ride ride;
  private boolean visited;

  public RideNode(Ride ride)
  {
    visited = false;
  }

  Ride getRide() {return ride;}
  boolean isVisited(){return visited;}
  void setVisited(){visited = true;}
}
