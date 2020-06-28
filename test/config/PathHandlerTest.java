package config;

import data_structures.Ride;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class PathHandlerTest
{
  @Test
  public void testPathHandlerWorksAsExpected()
  {
    List<Ride> rides = RideHandler.generateRides("test/config/rideHandlerTestRaw.txt");
    Map<Ride, Map<Ride, Integer>> paths = PathHandler.generatePaths("test/config/pathHandlerTestRaw.txt", rides);

    Ride testRide1 = rides.get(0);
    Ride testRide2 = rides.get(1);

    assertTrue(paths.size() == 3);

    Map<Ride, Integer> pathsFromEntrance = paths.get(null);
    Map<Ride, Integer> pathsFromTest1 = paths.get(testRide1);
    Map<Ride, Integer> pathsFromTest2 = paths.get(testRide2);

    assertEquals(2, pathsFromEntrance.size());
    assertEquals(1, pathsFromTest1.size());
    assertEquals(1, pathsFromTest2.size());

    assertEquals(200, (long)pathsFromEntrance.get(testRide1));
    assertEquals(10000, (long)pathsFromEntrance.get(testRide2));

    assertEquals(100, (long)pathsFromTest1.get(testRide2));
    assertEquals(100, (long)pathsFromTest2.get(testRide1));
  }

}