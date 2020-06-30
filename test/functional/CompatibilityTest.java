// written by Tristan Nunn (wdb18160), June 12020HE, for the CS251 Project
package functional;

import data_structures.Preferences;
import data_structures.Ride;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompatibilityTest
{

  @Test
  public void testCompatibilityWheelchair()
  {
    Preferences isAble = new Preferences("John", "john.doe@gmail.com", true, 1);
    Preferences isNotAble = new Preferences("Jane", "jane.doe@gmail.com", true, 1);
    isNotAble.setWheelchair(true, 0);

    Ride wheelchairLockedRide = new Ride("No wheelchairs", "Test", -1, -1, false, -1, -1, false, false, false, false, false, false, 0);

    Compatibility isCompatible = new Compatibility(wheelchairLockedRide, isAble, true);
    assertEquals(true, isCompatible.isCompatible());
    Compatibility isNotCompatible = new Compatibility(wheelchairLockedRide, isNotAble, true);
    assertEquals(false, isNotCompatible.isCompatible());
  }

  @Test
  public void testCompatibilityKidsOnly()
  {
    Preferences isAble = new Preferences("John", "john.doe@gmail.com", true, 1);
    isAble.setChild(true, 0);
    Preferences isNotAble = new Preferences("Jane", "jane.doe@gmail.com", true, 1);
    isNotAble.setChild(false, 0);

    Ride adultLockedRide = new Ride("No adults", "Test", -1, -1, false, -1, -1, false, false, false, false, true, true, 0);

    Compatibility isCompatible = new Compatibility(adultLockedRide, isAble, true);
    assertEquals(true, isCompatible.isCompatible());
    Compatibility isNotCompatible = new Compatibility(adultLockedRide, isNotAble, true);
    assertEquals(false, isNotCompatible.isCompatible());
  }

  @Test
  public void testCompatibilityGroupSize()
  {
    Preferences isAble = new Preferences("John", "john.doe@gmail.com", true, 1);
    Preferences isNotAble = new Preferences("Jane", "jane.doe@gmail.com", false, 10);

    Ride groupLockedRide = new Ride("No parties", "Test", -1, -1, false, 1, 1, false, false, false, false, false, false, 0);

    Compatibility isCompatible = new Compatibility(groupLockedRide, isAble, true);
    assertEquals(true, isCompatible.isCompatible());
    Compatibility isNotCompatible = new Compatibility(groupLockedRide, isNotAble, true);
    assertEquals(false, isNotCompatible.isCompatible());
  }

  @Test
  public void testCompatibilityHeightRestriction()
  {
    Preferences isAble = new Preferences("John", "john.doe@gmail.com", true, 2);
    isAble.setHeight(199, 0);
    isAble.setHeight(101, 1);
    Preferences notAbleTooTall = new Preferences("Jack", "jack.doe@gmail.com", true, 1);
    notAbleTooTall.setHeight(200, 0);
    Preferences notAbleTooShort = new Preferences("Jane", "jane.doe@gmail.com", true, 1);
    notAbleTooShort.setHeight(100, 0);

    Ride heightLockedRide = new Ride("No parties", "Test", 100, 200, false, -1, -1, false, false, false, false, false, false, 0);

    Compatibility isCompatible = new Compatibility(heightLockedRide, isAble, true);
    assertEquals(true, isCompatible.isCompatible());
    Compatibility isNotCompatible = new Compatibility(heightLockedRide, notAbleTooTall, true);
    assertEquals(false, isNotCompatible.isCompatible());
    Compatibility isAlsoNotCompatible = new Compatibility(heightLockedRide, notAbleTooShort, true);
    assertEquals(false, isAlsoNotCompatible.isCompatible());
  }

  @Test
  public void testCompatibilityHeightInclusive()
  {
    Preferences isAble = new Preferences("John", "john.doe@gmail.com", true, 1);
    isAble.setHeight(100, 0);

    Ride heightLockedRide = new Ride("No parties", "Test", 100, 200, true, -1, -1, false, false, false, false, false, false, 0);

    Compatibility isCompatible = new Compatibility(heightLockedRide, isAble, true);
    assertEquals(true, isCompatible.isCompatible());
  }

  @Test
  public void testCompatibilityForBoringPreferenceMetric()
  {
    Preferences likes = new Preferences("John", "john.doe@gmail.com", false, 1);
    likes.incrementAdrenLikes();
    Preferences loves = new Preferences("Jack", "jack.doe@gmail.com", false, 1);
    loves.incrementAdrenLoves();
    Preferences doesntlike = new Preferences("Jane", "jane.doe@gmail.com", false, 1);

    Ride adrenalineRide = new Ride("Adrenaline only", "Test", -1, -1, false, -1, -1, true, false, false, false, false, false, 0);

    Compatibility compatibilityLikes = new Compatibility(adrenalineRide, likes, false);
    assertEquals(true, compatibilityLikes.isCompatible());
    Compatibility compatibilityLoves = new Compatibility(adrenalineRide, loves, false);
    assertEquals(true, compatibilityLoves.isCompatible());
    Compatibility compatibilityDoesntLike = new Compatibility(adrenalineRide, doesntlike, false);
    assertEquals(false, compatibilityDoesntLike.isCompatible());
    assertEquals(true, compatibilityDoesntLike.getReason().contains("adrenaline"));
  }



}