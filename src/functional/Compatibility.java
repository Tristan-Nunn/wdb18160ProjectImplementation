// written by Tristan Nunn (wdb18160), June 12020HE, for the CS251 Project

package functional;

import data_structures.Preferences;
import data_structures.Ride;

import java.util.ArrayList;

public class Compatibility
{
  private Ride ride;
  private Preferences preferences;
  private boolean isCompatible;
  private ArrayList<String> reasons = new ArrayList<>();

  public Compatibility(Ride r, Preferences p, boolean checkOnlyAble)
  {
    ride = r;
    preferences = p;
    isCompatible = checkCompatibility(checkOnlyAble);
  }

  private boolean checkCompatibility(boolean toCheckOnlyAble)
  {
    boolean toSet = true;
    int max = preferences.partySize;
    if (!groupSizeCompatible())
    {
      reasons.add("your group is the wrong size");
      toSet = false;
    }
    int ineligibleRiders = countIneligibleRiders();
    if (ineligibleRiders > max/2)
    {
      reasons.add(ineligibleRiders + " out of " + max + " people cannot go on this ride");
      toSet = false;
    }
    if (!toCheckOnlyAble)
    {
      if (checkNotLiked(ride.water, preferences.getWater_likes(), preferences.getWater_loves(), "water"))
        toSet = false;
      if (checkNotLiked(ride.horror, preferences.getHorror_likes(), preferences.getHorror_loves(), "horror"))
        toSet = false;
      if (checkNotLiked(ride.adrenaline, preferences.getAdren_likes(), preferences.getAdren_loves(), "adrenaline"))
        toSet = false;
      if (checkNotLiked(ride.kids, preferences.getKid_likes(), preferences.getKid_loves(), "kids"))
        toSet = false;
    }

    return toSet;
  }

  private boolean checkNotLiked(boolean contains, int likes, int loves, String rideType)
  {
    int max = preferences.partySize;
    if (contains && isLessThanHalf(likes + loves, max))
    {
      reasons.add((max  - (likes + loves)) + " out of " + max + " people do not like " + rideType + " rides");
      return true;
    }
    return false;
  }

  private boolean isLessThanHalf(int test, int threshold)
  {
    if (threshold %2 == 0)
      return test < threshold/2;
    else
      return test*2 < threshold;
  }

  private int countIneligibleRiders()
  {
    int cantGoCount = 0;
    for (int i = 0; i < preferences.partySize; i++)
    {
      boolean canGo = true;
      if(
          !(ride.heightmin == -1 || (preferences.getHeights()[i] > ride.heightmin && !ride.minclusive) || (preferences.getHeights()[i] >= ride.heightmin && ride.minclusive)) ||
          !(ride.heightmax == -1 || (preferences.getHeights()[i] < ride.heightmax)))
        canGo = false;
      else if (!preferences.getChildren()[i] && ride.kidsonly)
        canGo = false;
      else if (preferences.getWheelchairCount()[i] && !ride.wheelchair)
        canGo = false;
      if (!canGo)
        cantGoCount++;
    }

    return cantGoCount;
  }

  private boolean groupSizeCompatible()
  {
    return (
        (ride.groupmin == -1 || preferences.partySize >= ride.groupmin) &&
            (ride.groupmax == -1 || preferences.partySize <= ride.groupmax) ||
            preferences.splitable);
  }

  public String getReason()
  {
    if (isCompatible)
      return "This ride is compatible with your preferences.";

    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < reasons.size(); i++)
    {
      builder.append(reasons.get(i));
      if (i < reasons.size()-2)
        builder.append(", ");
      if (i == reasons.size()-2)
        builder.append(" and ");
    }

    return builder.toString();
  }

  public boolean isCompatible()
  {
    return isCompatible;
  }
}
