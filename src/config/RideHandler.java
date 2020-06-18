// written by Tristan Nunn, May 12020HE, for uni
// I promise I wrote this code myself, etc...
package config;

import data_structures.Ride;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RideHandler
{
  public static final List<String> ZONES = new ArrayList<>();
  public static final List<Ride> RIDES = generateRides();

      private static List<Ride> generateRides()
      {
        List<Ride> rides = new LinkedList<Ride>();

        FileInputStream stream = null;
        BufferedReader reader = null;
        String line = null;
        int lineNo = 0;

        try
        {
          stream = new FileInputStream("src/config/rides.txt");
          reader = new BufferedReader(new InputStreamReader(stream));

          String category = null;
          String name = "";
          int heightmin = -1;
          int heightmax = -1;
          boolean minclusive = false;
          boolean maxclusive = false;
          int groupmin = -1;
          int groupmax = -1;
          boolean adrenaline = false;
          boolean wheelchair = false;
          boolean horror = false;
          boolean water = false;
          boolean kids = false;
          boolean kidsonly = false;
          int waitTime = 0;

          while ((line = reader.readLine()) != null)
          {
            lineNo++;
            // chop off comments
            int hashindex = line.indexOf('#');
            if (hashindex > -1)
              line = line.substring(0, (hashindex));
            // chop off starting whitespace
            while (line.startsWith(" ") || line.startsWith("\t"))
              line = line.substring(1, line.length());
            if (line.equals(""))
              continue;

            // can't switch with strings in java :C
            if (line.startsWith("wheelchair"))
              wheelchair = true;
            else if (line.startsWith("adrenaline"))
              adrenaline = true;
            else if (line.startsWith("horror"))
              horror = true;
            else if (line.startsWith("water"))
              water = true;
            else if (line.startsWith("kidsonly"))
            {
              kids = true;
              kidsonly = true;
            }
            else if (line.startsWith("kids"))
              kids = true;
            else if (line.startsWith("minclusive"))
              minclusive = true;
            else if (line.startsWith("height"))
            {
              String[] numbers = getNumbers(line);
              heightmin = parseNumber(numbers[0]);
              heightmax = parseNumber(numbers[1]);
            }
            else if (line.startsWith("group"))
            {
              String[] numbers = getNumbers(line);
              groupmin = parseNumber(numbers[0]);
              groupmax = parseNumber(numbers[1]);
            }
            else if (line.startsWith("linetime"))
            {
              String number = getNumber(line);
              waitTime = parseNumber(number);
            }
            else if (line.startsWith("newride"))
            {
              if (!name.equals(""))
              {
                rides.add(new Ride(name, category, heightmin, heightmax, minclusive, maxclusive, groupmin, groupmax, adrenaline, wheelchair, horror, water, kids, kidsonly, waitTime));
                heightmin = -1;
                heightmax = -1;
                minclusive = false;
                maxclusive = false;
                groupmin = -1;
                groupmax = -1;
                adrenaline = false;
                wheelchair = false;
                horror = false;
                water = false;
                kids = false;
                kidsonly = false;
                waitTime = 0;
              }
              name = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
            }
            else if (line.startsWith("category"))
            {
                if (!name.equals(""))
                {
                  rides.add(new Ride(name, category, heightmin, heightmax, minclusive, maxclusive, groupmin, groupmax, adrenaline, wheelchair, horror, water, kids, kidsonly, waitTime));
                  heightmin = -1;
                  heightmax = -1;
                  minclusive = false;
                  maxclusive = false;
                  groupmin = -1;
                  groupmax = -1;
                  adrenaline = false;
                  wheelchair = false;
                  horror = false;
                  water = false;
                  kids = false;
                  kidsonly = false;
                  name = "";
                  waitTime = 0;
                }
                category = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
                if (!ZONES.contains(category))
                  ZONES.add(category);
            }
            else
              throw new Exception("Unrecognised command: " + line);
          }
          // at the end of the file, submit the final ride if one has been logged
          if (!name.equals(""))
          {
            rides.add(new Ride(name, category, heightmin, heightmax, minclusive, maxclusive, groupmin, groupmax, adrenaline, wheelchair, horror, water, kids, kidsonly, waitTime));
          }
        }
        catch (Exception e)
        {
          System.out.println("Something went wrong! " + "\"" + e.getMessage());
          if (lineNo > 0)
          {
            System.out.println("There is a typo on line " + lineNo);
          }
        }

        return rides;
      }

      private static int parseNumber(String s)
      {
        if (s.startsWith("-"))
          return -1;
        else
          return Integer.parseInt(s);
      }

      private static String[] getNumbers(String s)
      {
        String[] nums = new String[2];
        // chop off first word and whitespace to get to first number
        s = s.substring(s.indexOf(" "));
        while (s.charAt(0) == ' ' || s.charAt(0) == '\t')
          s = s.substring(1, s.length());

        nums[0] = s.substring(0, s.indexOf(' '));

        //chop off first number and whitespace to get to second number
        s = s.substring(s.indexOf(" "));
        while (s.charAt(0) == ' ' || s.charAt(0) == '\t')
          s = s.substring(1, s.length());

        int endSpace = s.indexOf(' ');
        nums[1] = s.substring(0, (endSpace!= -1) ? endSpace : s.length());

        return nums;
      }

      private static String getNumber(String s)
      {
        // chop off word and whitespace to get number
        s = s.substring(s.indexOf(" "));
        while (s.charAt(0) == ' ' || s.charAt(0) == '\t')
          s = s.substring(1, s.length());

        int endSpace = s.indexOf(' ');
        return s.substring(0, (endSpace!= -1) ? endSpace : s.length());
      }

}
