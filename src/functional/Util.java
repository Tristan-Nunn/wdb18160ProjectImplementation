// written by Tristan Nunn, May 12020HE, for uni
// I promise I wrote this code myself, etc...

package functional;

public class Util
{
  public static String plural(int count, String word)
  {
    if (word.equals("person"))
    {
      return count == 1 ? " person" : " people";
    }
    if (word.equals("does"))
    {
      return count == 1 ? " does" : " do";
    }
    return count == 1 ? " " + word : " " + word + "s";
  }
}
