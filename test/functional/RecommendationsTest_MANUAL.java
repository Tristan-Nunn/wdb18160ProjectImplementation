// written by Tristan Nunn (wdb18160), June 12020HE, for the CS251 Project
package functional;

import config.PathHandler;
import config.RideHandler;
import data_structures.Preferences;
import org.junit.Test;

public class RecommendationsTest_MANUAL
{
  @Test
  public void manualTestSingleRide()
  {
    System.out.println("========= Manual Test 1 - Single, constant ride ==========");
    System.out.println("Ride returned should be Robot Conflicts.");
    System.out.println("This is because it is the rightmost right.");
    System.out.println("It should be acceptable because the sole individual in the test group does not like horror rides.");
    int userPath = 1; // Single Ride
    Recommendations.recommend(makePrefs(), new RideTree(RideHandler.RIDES), userPath, PathHandler.PATHS);
  }
  @Test
  public void manualTestEntirePark()
  {
    System.out.println("========= Manual Test 2 - Every ride in a list ==========");
    System.out.println("The three rides returned should be Trench Chase, Build a Bot, and Hall O Mirrors.");
    System.out.println("This is because they are the only ones the party can visit, and they are in the party's preferences.");
    System.out.println("They should be reported in their respective zones.");
    System.out.println("There should be no rides in the jurassic and medieval zones.");
    System.out.println();
    int userPath = 2; // Every ride
    Recommendations.recommend(makePrefs(), new RideTree(RideHandler.RIDES), userPath, PathHandler.PATHS);
  }
  @Test
  public void manualTestMap()
  {
    System.out.println("========= Manual Test 3 - Map of the park ==========");
    System.out.println("This should print the minimum distance from the entrance to every ride.");
    System.out.println("While I've not checked every path, the easiest paths to check are the Tower of Terror and Rex Rampage.");
    System.out.println("Entrance->Tower of Terror is 450 metres and Entrance->Rex Rampage is 300 metres without crossing any other paths.");
    System.out.println("However entrance->Pony Jousts->Mystic Fortunes->Tower of Terror is 150 + 100 + 90 metres (340 metres).");
    System.out.println("And also entrance->Pet a Saur->Rex Rampage is 150 + 180 metres (330 metres).");
    System.out.println("Therefore the distance to the Tower of Terror should be 340 (going through the medieval zone).");
    System.out.println("And the distance to the Rex Rampage should be 300 (neglecting to go through the jurassic zone).");
    System.out.println();
    int userPath = 3; // Park Map
    Recommendations.recommend(makePrefs(), new RideTree(RideHandler.RIDES), userPath, PathHandler.PATHS);
  }
  @Test
  public void manualTestPersonalMap()
  {
    System.out.println("========= Manual Test 4 - Personalised map of the park ==========");
    System.out.println("This should print the distance minimum from the entrance to every ride.");
    System.out.println("However it should only include rides the party wants to go on with the distance unchanged.");
    System.out.println("These are Build a Bot (390), Trench Chase (570), and Hall O Mirrors (450).");
    int userPath = 4; // Personal Map
    Recommendations.recommend(makePrefs(), new RideTree(RideHandler.RIDES), userPath, PathHandler.PATHS);
  }
  @Test
  public void manualTestRecRoute()
  {
    System.out.println("========= Manual Test 5 - Recommended route through the park ==========");
    System.out.println("Of the three rides (Trench Chase, Build a Bot, and Hall O Mirrors), the user should be told to go to Trench Chase.");
    System.out.println("The two choices for first ride in terms of just distance would be Trench Chase or Hall O Mirrors, as these are at the edge.");
    System.out.println("However Trench chase should be picked as it is a Kids' ride and Hall O Mirrors is not, and this party loves Kids' rides.");
    System.out.println("The next choice should be the centre ride, Build-a-bot, as this is a kids ride and is between the two outer rides.");
    System.out.println("The last choice should be Hall O Mirrors as this is the opposite edge to Trench Chase.");
    int userPath = 5; // Recommended Route
    Recommendations.recommend(makePrefs(), new RideTree(RideHandler.RIDES), userPath, PathHandler.PATHS);
  }

  private Preferences makePrefs()
  {
    Preferences p = new Preferences("John", "john.doe@gmail.com", false, 1);
    p.setWheelchair(true, 0);
    p.setHeight(120, 0);
    p.incrementKidLoves();
    p.incrementAdrenLikes();
    return p;
  }
}