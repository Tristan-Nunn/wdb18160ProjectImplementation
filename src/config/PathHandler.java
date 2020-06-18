package config;

import data_structures.Ride;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class PathHandler
{
  // Using an adjacency  map
  public static final Map<Ride, Map<Ride, Integer>> PATHS = generatePaths();

  private static Map<Ride, Map<Ride, Integer>> generatePaths()
  {
    Map<Ride, Map<Ride, Integer>> paths = new HashMap<>();

    paths.put(null, new HashMap<>()); // this is the entrance
    for (Ride r : List.copyOf(RideHandler.RIDES))
    {
      paths.put(r, new HashMap<>());
    }

    FileInputStream stream = null;
    BufferedReader reader = null;
    String line = null;
    int lineNo = 0;

    try
    {
      stream = new FileInputStream("src/config/paths.txt");
      reader = new BufferedReader(new InputStreamReader(stream));

      Ride source = null;
      Map<Ride, Integer> destinations = new HashMap<>();

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

        if (line.startsWith("newpath"))
        {
          source = getRide(line.substring(line.indexOf('"') + 1, line.lastIndexOf('"')));
        }
        else if (line.startsWith("\""))
        {
          Ride destination = getRide(line.substring(line.indexOf('"') + 1, line.lastIndexOf('"')));
          Integer walkTime = parseNumber(getNumber(line.substring(line.lastIndexOf('"'))));

          Map<Ride, Integer> sourceDestinations = paths.get(source);
          sourceDestinations.put(destination, walkTime);
          paths.put(source, sourceDestinations);

          if (source != null && destination != null)
          {
            Map<Ride, Integer> destinationDestinations = paths.get(destination);
            destinationDestinations.put(source, walkTime);
            paths.put(destination, destinationDestinations);
          }
        }
        else
          throw new Exception("Unrecognised command: " + line);
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

    return paths;
  }

  private static int parseNumber(String s)
  {
    if (s.startsWith("-"))
      return -1;
    else
      return Integer.parseInt(s);
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

  private static Ride getRide(String s) throws IllegalArgumentException
  {
    if (s.equalsIgnoreCase("entrance"))
      return null;

    for (Ride r : RideHandler.RIDES)
      if (r.name.equalsIgnoreCase(s))
        return r;

    throw new IllegalArgumentException("Ride " + s + " does not exist!");
  }

}
