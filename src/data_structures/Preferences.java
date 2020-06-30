// written by Tristan Nunn (wdb18160), May 12020HE, for the CS251 Project
package data_structures;

import user_input.Get;

public class Preferences
{
  public final String leaderName;
  public final String leaderEmail;
  public final int partySize;
  public final boolean splitable;
  private int[] heights;
  private boolean[] children;
  private boolean[] wheelchairs;
  private int kid_loves = 0;
  private int kid_likes = 0;
  private int water_loves = 0;
  private int water_likes = 0;
  private int horror_loves = 0;
  private int horror_likes = 0;
  private int adren_loves = 0;
  private int adren_likes = 0;

  public Preferences(String leaderName, String leaderEmail, boolean splitable, int partySize)
  {
    this.leaderName = leaderName;
    this.leaderEmail = leaderEmail;
    this.splitable = splitable;
    this.partySize = partySize;
    heights = new int[partySize];
    children = new boolean[partySize];
    wheelchairs = new boolean[partySize];
  }

  public static Preferences initPreferences()
  {
    String leader = Get.stringFromUser("Please enter your first name: ");
    boolean toEmail = Get.boolFromUser("Hi " + leader + " would you like your results to be emailed to you? (Y/N): ");
    String email = null;
    if (toEmail)
    {
      email = Get.emailFromUser("Please enter your email address: ");
      System.out.println("Thank you, you reccomendations will be emailed to " + email);
    } else
      System.out.println("Your reccomendations will be printed.");
    return createNewParty(leader, email);
  }

  public static Preferences createNewParty(String leader, String email)
  {
    int partySize = -1;
    while (partySize < 1)
      // arbitrary 25 limit on party size, as anything larger than this could be really unreasonable
      partySize = Get.intFromUser("How many people are in your party?", 1, 25, "Are you sure your party is that big?");

    boolean splitable = partySize == 1 ? true : Get.boolFromUser("Is your group comfortable being split up or sharing rides with strangers? (Y/N): ");
    Preferences workingPrefs = new Preferences(leader, email, splitable, partySize);

    System.out.println("Please use the height chart next to this terminal to measure the height of each party member.");
    for (int i = 0; i < partySize; i++)
    {
      int height = (int) (Get.doubleFromUser("Please enter the height for person " + (i+1) + " in metres (e.g. 1.4): ", 0, 0, null) * 100);
      while (height < 40 || height > 250)
      {
        if (height < 40)
          System.out.print("Are you really that short? ");
        if (height > 250)
          System.out.print("Are you really that tall? ");
        height = (int) Get.doubleFromUser("Please re-enter your height in metres: ", 0, 0, null) * 100;
      }
      workingPrefs.setHeight(height, i);

      int water =
          Get.boolFromUser("Do you like water rides? (Y/N): ") ? 2 :
              Get.boolFromUser("Are you okay with water rides? (Y/N): ") ? 1 : 0;
      if (water == 2)
        workingPrefs.incrementWaterLoves();
      else if (water == 1)
        workingPrefs.incrementWaterLikes();

      int adren =
          Get.boolFromUser("Do you like adrenaline-pumping rides? (Y/N): ") ? 2 :
              Get.boolFromUser("Are you okay with adrenaline-pumping rides? (Y/N): ") ? 1 : 0;
      if (adren == 2)
        workingPrefs.incrementAdrenLoves();
      else if (adren == 1)
        workingPrefs.incrementAdrenLikes();

      int horror =
          Get.boolFromUser("Do you like scary rides? (Y/N): ") ? 2 :
              Get.boolFromUser("Are you okay with scary rides? (Y/N): ") ? 1 : 0;
      if (horror == 2)
        workingPrefs.incrementHorrorLoves();
      else if (horror == 1)
        workingPrefs.incrementHorrorLikes();

      boolean wheelchair = (Get.boolFromUser("Do you need accomodations for a wheelchair? (Y/N): "));
      workingPrefs.setWheelchair(wheelchair, i);

      boolean isChild = (Get.boolFromUser("Are you currently under the age of 16? (Y/N): "));
      workingPrefs.setChild(isChild, i);

      int child = isChild ? 2 :
          Get.boolFromUser("Do you like children's rides? (Y/N): ") ? 2 :
              Get.boolFromUser("Are you okay with children's rides? (Y/N): ") ? 1 : 0;
      if (child == 2)
        workingPrefs.incrementKidLoves();
      else if (child == 1)
        workingPrefs.incrementKidLikes();
    }
    return workingPrefs;
  }

  public int[] getHeights()
  {
    return heights;
  }
  public boolean[] getChildren()
  {
    return children;
  }
  public boolean[] getWheelchairCount()
  {
    return wheelchairs;
  }
  public int getKid_loves()
  {
    return kid_loves;
  }
  public int getKid_likes()
  {
    return kid_likes;
  }
  public int getWater_loves()
  {
    return water_loves;
  }
  public int getWater_likes()
  {
    return water_likes;
  }
  public int getHorror_loves()
  {
    return horror_loves;
  }
  public int getHorror_likes()
  {
    return horror_likes;
  }
  public int getAdren_loves()
  {
    return adren_loves;
  }
  public int getAdren_likes()
  {
    return adren_likes;
  }
  public void setHeight(int height, int index)
  {
    if (index < heights.length && index > -1)
      heights[index] = height;
  }

  public void setChild(boolean value, int index)
  {
    if (index < children.length && index > -1)
      children[index] = value;
  }
  public void setWheelchair(boolean value, int index)
  {
    if (index < wheelchairs.length && index > -1)
      wheelchairs[index] = value;
  }
  public void incrementKidLoves()
  {
    kid_loves++;
  }
  public void incrementKidLikes()
  {
    kid_likes++;
  }
  public void incrementWaterLoves()
  {
    water_loves++;
  }
  public void incrementWaterLikes()
  {
    water_likes++;
  }
  public void incrementHorrorLoves()
  {
    horror_loves++;
  }
  public void incrementHorrorLikes()
  {
    horror_likes++;
  }
  public void incrementAdrenLoves()
  {
    adren_loves++;
  }
  public void incrementAdrenLikes()
  {
    adren_likes++;
  }
}
