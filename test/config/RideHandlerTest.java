// written by Tristan Nunn (wdb18160), Jude 12020HE, for the CS251 Project
package config;

import data_structures.Ride;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RideHandlerTest
{

  @Test
  public void testRideHandlerWorksAsExpected()
  {
    List<Ride> rides = RideHandler.generateRides("test/config/rideHandlerTestRaw.txt");

    assertTrue(rides.size() == 2);

    Ride ride1 = rides.get(0);
    Ride ride2 = rides.get(1);

    assertTrue(ride1.name.equals("Test ride 1"));
    assertTrue(ride1.adrenaline == false);
    assertTrue(ride1.horror == false);
    assertTrue(ride1.kids == false);
    assertTrue(ride1.water == false);
    assertTrue(ride1.category.equals("Test"));
    assertTrue(ride1.minclusive == false);
    assertTrue(ride1.wheelchair == false);
    assertTrue(ride1.kidsonly == false);
    assertTrue(ride1.heightmax == -1);
    assertTrue(ride1.heightmin == -1);
    assertTrue(ride1.groupmax == -1);
    assertTrue(ride1.groupmin == -1);
    assertTrue(ride1.waitTime == 10);

    assertTrue(ride2.name.equals("Test ride 2"));
    assertTrue(ride2.adrenaline == true);
    assertTrue(ride2.horror == true);
    assertTrue(ride2.kids == true);
    assertTrue(ride2.water == true);
    assertTrue(ride2.category.equals("Test"));
    assertTrue(ride2.minclusive == true);
    assertTrue(ride2.wheelchair == true);
    assertTrue(ride2.kidsonly == true);
    assertTrue(ride2.heightmax == 200);
    assertTrue(ride2.heightmin == 100);
    assertTrue(ride2.groupmax == 8);
    assertTrue(ride2.groupmin == 4);
    assertTrue(ride2.waitTime == 10);

  }

}