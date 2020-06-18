// written by Tristan Nunn, May 12020HE, for uni
// I promise I wrote this code myself, etc...
package cli_components;

import java.util.List;
import java.util.Scanner;

public class Get
{
  public static String stringFromUser(String prompt)
  {
    Scanner scanner = new Scanner(System.in);
    String input = null;

    while (input == null)
    {
      System.out.print(prompt);
      input = scanner.nextLine();
      System.out.println();
      if (input == null || input.equals(""))
      {
        System.out.println("That is not a valid input");
        input = null;
      }
    }
    return input;
  }

  public static int intFromUser(String prompt, int min, int max, String limitPrompt)
  {
    Scanner scanner = new Scanner(System.in);
    Integer input = null;
    boolean isValid = false;

    while (!isValid)
    {
      System.out.print(prompt);
      if (scanner.hasNextInt())
      {
        input = scanner.nextInt();
        // this line is required because scanner behaves weirdly with nextInt();
        scanner.nextLine();
        System.out.println();
      }
      else
        scanner.next();
      if (input == null)
        System.out.println("Please enter a value");
      else if (min != max && (input < min || input > max))
        System.out.println(limitPrompt);
      else
        isValid = true;
    }
    return input;
  }

  public static double doubleFromUser(String prompt, double min, double max, String limitPrompt)
  {
    Scanner scanner = new Scanner(System.in);
    Double input = null;
    boolean isValid = false;

    while (!isValid)
    {
      System.out.print(prompt);
      if (scanner.hasNextDouble())
      {
        input = scanner.nextDouble();
        // this line is required because scanner behaves weirdly with nextDouble();
        scanner.nextLine();
        System.out.println();
      }
      else
        scanner.next();
      if (input == null)
        System.out.println("Please enter a value");
      else if (min != max && (input < min || input > max))
        System.out.println(limitPrompt);
      else
        isValid = true;
    }
    return input;
  }

  public static String emailFromUser(String prompt)
  {
    Scanner scanner = new Scanner(System.in);
    String input = null;
    boolean isValid = false;

    while (!isValid)
    {
      System.out.print(prompt);
      input = scanner.nextLine();
      System.out.println();
      if (input == null || input == "")
        System.out.println("Please enter a value");
      else if (!isValidEmail(input))
        System.out.println("Please enter a valid email address");
      else
        isValid = true;
    }
    return input;
  }

  public static boolean boolFromUser(String prompt)
  {
    Scanner scanner = new Scanner(System.in);
    String input = null;
    boolean result = false;
    boolean isValid = false;

    while (!isValid)
    {
      System.out.print(prompt);
      input = scanner.nextLine();
      System.out.println();
      if (input == null || input == "")
        System.out.println("Please enter a value");
      else if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes"))
      {
        isValid = true;
        result = true;
      }
      else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no"))
      {
        isValid = true;
        result = false;
      }
      else
      {
        System.out.println("Please enter a valid value! (Y/Yes/N/No)");
      }
    }
    return result;
  }

  public static int choiceFromUser(String prompt, List<String> options)
  {
    if (options.size() < 1)
      return -1;
    else if (options.size() == 1)
      return 0;

    System.out.println(prompt);
    int index = 1;
    for (String option : options)
      System.out.println("\t" + index++ + ".\t" + option);

    int answer = intFromUser(">>> ", 1, options.size(), "Please select one of the options.");
    return answer;

  }

  private static boolean isValidEmail(String dis)
  {
    if (dis == null || dis.length() < 7)
      return false;

    // does not start with a . or an @
    boolean startValid = !(dis.startsWith("@") || dis.startsWith("."));
    // has a single @ in the middle, followed by at least 1 letter
    boolean centreValid = (dis.indexOf("@") == dis.lastIndexOf("@") && dis.charAt(dis.indexOf("@") + 1) != '.');
    // ends with a valid domain thingy
    boolean endValid = dis.endsWith(".com") || dis.endsWith(".co.uk") || dis.endsWith(".org") || dis.endsWith("ac.uk");

    return startValid && centreValid && endValid;
  }
}
