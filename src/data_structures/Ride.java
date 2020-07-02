// written by Tristan Nunn (wdb18160), May 12020HE, for the CS251 Project
package data_structures;

public class Ride
{
  public final String name;
  public final String category;
  public final int heightmin;
  public final int heightmax;
  public final boolean minclusive;
  public final int groupmin;
  public final int groupmax;
  public final boolean adrenaline;
  public final boolean wheelchair;
  public final boolean horror;
  public final boolean water;
  public final boolean kids;
  public final boolean kidsonly;
  public final int waitTime;

  public Ride(String name, String category, int heightmin, int heightmax, boolean minclusive, int groupmin, int groupmax, boolean adrenaline, boolean wheelchair, boolean horror, boolean water, boolean kids, boolean kidsonly, int waitTime)
  {
    this.name = name;
    this.category = category;
    this.heightmin = heightmin;
    this.heightmax = heightmax;
    this.minclusive = minclusive;
    this.groupmin = groupmin;
    this.groupmax = groupmax;
    this.adrenaline = adrenaline;
    this.wheelchair = wheelchair;
    this.horror = horror;
    this.water = water;
    this.kids = kids;
    this.kidsonly = kidsonly;
    this.waitTime = waitTime;
  }
}
