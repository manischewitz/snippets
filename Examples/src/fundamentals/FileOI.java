package fundamentals;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileOI {

  public static void main(String... args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter 'Writer' to write into file OR 'Reader' to read.");
    System.out.println("Current user dir is " + System.getProperty("user.dir"));
    String input = scanner.nextLine();

    if (input.equals("Writer")) {
      System.out.println("Enter filename");
      String filename = scanner.nextLine();
      PrintWriter out = new PrintWriter(filename, "UTF-8");
      System.out.println("Print something into file !exit to finish.");
      String in = "";
      while (!in.equals("!exit")) {
        out.print(in);
        in = scanner.nextLine();
      }
      out.close();
    } else if (input.equals("Reader")) {
      System.out.println("Enter path");
      String path = scanner.nextLine();
      Scanner in = new Scanner(Paths.get(path), "UTF-8");
      while (in.hasNext()) {
          System.out.println(in.nextLine());
      }
      in.close();
    }
    scanner.close();
}

}
