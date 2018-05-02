package fundamentals;

public class Arguments {

  public static void main(String... args) {
    String greeting = "Hello";
    System.out.println(greeting == "Hello"); //true
    System.out.println(greeting.substring(0, 4) == "Hell"); //false
    System.out.println("Length of \u1D546 is "+"\u1D546".length());

    final java.util.Scanner scanner = new java.util.Scanner(System.in);

    while(true) {
      System.out.println("Choose index of argument you entered "+
      "starting from 0 or -1 to exit");
      int index = scanner.nextInt();
      //String input = scanner.nextLine();

      if (index == -1) {
        break;
      } else if (index < args.length) {
        System.out.println(args[index]);
      } else {
        System.out.println("There is no element at that index.");
      }
    }
    scanner.close();
}

}
