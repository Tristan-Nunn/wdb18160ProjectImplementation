// written by Tristan Nunn (wdb18160), June 12020HE, for the CS251 Project
package functional;

import data_structures.Preferences;
import data_structures.Ride;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RideTreeTest
{

  @Test
  public void testRideTreeSingleRide()
  {
    List<Ride> allRides = new ArrayList<>(2);
    allRides.add(getRightRide());
    allRides.add(getLeftRide());

    RideTree tree = new RideTree(allRides);

    assertTrue(tree.getSingleRide().name.equals("Every Right Route"));
  }

  @Test
  public void testRideTreeMultipleRidesReturnsOnlyLikedRides()
  {
    Preferences allPrefs = new Preferences("John", "john.doe@gmail.com", false, 1);
    allPrefs.incrementAdrenLikes();
    allPrefs.incrementWaterLikes();
    allPrefs.incrementHorrorLikes();
    allPrefs.incrementKidLikes();
    Preferences noPrefs = new Preferences("Jane", "kane.doe@gmail.com", false, 1);

    List<Ride> allRides = new ArrayList<>(2);
    allRides.add(getRightRide());
    allRides.add(getLeftRide());

    RideTree tree = new RideTree(allRides);

    List<Ride> allPrefRides = tree.getMultipleRides(allPrefs);
    List<Ride> noPrefRides = tree.getMultipleRides(noPrefs);

    assertEquals(2, allPrefRides.size());
    assertEquals(1, noPrefRides.size());

    assertTrue(noPrefRides.get(0).name.equals("Every Left Route"));

    assertTrue(tree.getSingleRide().name.equals("Every Right Route"));
  }

  private static Ride getRightRide()
  {
    return new Ride("Every Right Route", "Test", -1, -1, false, -1, -1, true, true, true, true, true, false, 10);
  }

  private static Ride getLeftRide()
  {
    return new Ride("Every Left Route", "Test", -1, -1, false, -1, -1, false, false, false, false, false, false, 10);
  }
}