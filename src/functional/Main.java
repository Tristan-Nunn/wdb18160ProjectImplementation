package functional;// written by Tristan Nunn, May 12020HE, for uni
// I promise I wrote this code myself, etc...

import cli_components.Get;
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

      Reccomendations.recommend(prefs, tree, userPath);
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
        Reccomendations.recommend(prefs, tree, userPath);
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

  /*
  private static List<Ride> sort(List<Ride> r)
  {
    int s = r.size();
    if (s < 2)
    {
      return r;
    }
    return merge(sort(List.copyOf(r.subList(0, s/2))), sort(List.copyOf(r.subList(s/2, s))));
  }
  private static List<Ride> merge(List<Ride> r1, List<Ride> r2)
  {
    List<Ride> r = new LinkedList<>();

    int r1i = 0;
    int r2i = 0;

    // pick the smallest from r1 and r2
    while (r1i < r1.size() && r2i < r2.size())
    {
      if (r1.get(r1i).getPref() < r2.get(r2i).getPref())
        r.add(r2.get(r2i++));
      else
        r.add(r1.get(r1i++));
    }

    // empty r1 if not empty
    while (r1i < r1.size())
      r.add(r1.get(r1i++));
    // empty r2 if not empty
    while (r2i < r2.size())
      r.add(r2.get(r2i++));

    return r;
  }
  */

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

  //public static Preferences makeGroup2()
  //{
  //  Preferences p = new Preferences("J", "something@doe.com", false, 1);
  //  p.incrementKidLoves();
  //  p.incrementAdrenLoves();
  //  p.setHeight(100, 0);
  //  p.setChild(true, 0);
  //  p.setWheelchair(false, 0);
  //  return p;
  //}

}
