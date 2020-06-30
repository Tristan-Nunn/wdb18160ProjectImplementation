// written by Tristan Nunn (wdb18160), June 12020HE, for the CS251 Project
package functional;

import user_input.Get;
import config.RideHandler;
import data_structures.Preferences;

import java.util.*;

public class Main
{

  public static void main(String[] args)
  {

    int userPath = -1;

    Preferences prefs = null;
    RideTree tree = null;
    while (prefs == null)
    {
      userPath = Get.choiceFromUser("Chose an option: ", Arrays.asList(
          "Get recommendations for a single ride",
          "Get recommendations for a the entire park",
          "Generate a map of the park",
          "Get a recommended route to take around the park",
          "Quit"
      ));

      if (userPath == 5)
        break;

      if (userPath != 3)
      {
        prefs = makeGroup(); //Preferences.initPreferences();
        tree = new RideTree(List.copyOf(RideHandler.RIDES));
      }

      Recommendations.recommend(prefs, tree, userPath);
    }

    // Quit command changes from 5 to 6
    userPath = userPath==5 ? 6 : userPath;
    while (userPath != 6)
    {
      userPath = Get.choiceFromUser("Chose an option: ", Arrays.asList(
          "Get recommendations for a single ride",
          "Get recommendations for a the entire park",
          "Generate a map of the park",
          "Get a recommended route to take around the park",
          "Change your preferences",
          "Quit"
      ));

      if (userPath == 6)
        break;

      if (userPath == 5)
      {
        prefs = Preferences.createNewParty(prefs.leaderName, prefs.leaderEmail);
        tree = new RideTree(List.copyOf(RideHandler.RIDES));
      }
      else
      {
        Recommendations.recommend(prefs, tree, userPath);
      }
    }

    if (prefs != null)
    {
      if (prefs.leaderEmail != null)
        System.out.println("Thank you " + prefs.leaderName + " your recommendations have been emailed to " + prefs.leaderEmail + ", I hope you enjoy your day at Time Travellers");
      else
        System.out.println("Thank you " + prefs.leaderName + " your recommendations have been send to your printer, I hope you enjoy your day at Time Travellers");
    }
    System.out.println();
    System.out.println("Goodbye!");
  }

  public static Preferences makeGroup()
  {
    Preferences p = new Preferences("John Doe", "something@doe.com", false, 3);
    p.incrementKidLoves();
    p.incrementKidLoves();
    p.incrementAdrenLikes();
    p.incrementAdrenLikes();
    p.incrementHorrorLoves();
    p.incrementHorrorLoves();
    p.incrementHorrorLoves();
    p.incrementWaterLikes();
    p.incrementWaterLoves();
    p.setHeight(100, 0);
    p.setHeight(160, 1);
    p.setHeight(170, 2);
    p.setChild(true, 0);
    p.setChild(true, 1);
    p.setChild(true, 2);
    p.setWheelchair(false, 0);
    p.setWheelchair(false, 1);
    p.setWheelchair(false, 2);
    return p;
  }

}
