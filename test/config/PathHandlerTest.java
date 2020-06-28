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

    assertTrue(paths.size() == 3);
    assertEquals(2, paths.get(null).size());
    assertEquals(1, paths.get(rides.get(0)).size());
    assertEquals(0, paths.get(rides.get(1)).size());
  }

}