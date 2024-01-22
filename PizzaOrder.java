import java.util.Scanner;

public class PizzaOrder {

  public static void main(String[] args) {
    
    Scanner scanner = new Scanner(System.in);
    
    System.out.print("Enter pizza size (S, M, L): ");
    String size = scanner.next();
    
    System.out.print("Enter pizza toppings (comma separated): ");
    String toppings = scanner.next();
    
    System.out.print("Enter number of pizzas: ");
    int numPizzas = scanner.nextInt();
    
    double price = calculatePrice(size, toppings, numPizzas);
    
    printReceipt(size, toppings, numPizzas, price);
  }

  public static double calculatePrice(String size, String toppings, int numPizzas) {
    
    double basePrice = 0;
    if (size.equalsIgnoreCase("S")) {
      basePrice = 10;
    } else if (size.equalsIgnoreCase("M")) {
      basePrice = 12;
    } else if (size.equalsIgnoreCase("L")) {
      basePrice = 14;
    }
    
    double toppingPricePerPizza = 0;
    String[] toppingArray = toppings.split(",");
    for (String topping : toppingArray) {
      toppingPricePerPizza += 0.5; 
    }
    
    return (basePrice + toppingPricePerPizza) * numPizzas;
  }

  public static void printReceipt(String size, String toppings, int numPizzas, double price) {
    System.out.println();
    System.out.println("===== RECEIPT =====");
    System.out.println("Size: " + size);
    System.out.println("Toppings: " + toppings); 
    System.out.println("Number of pizzas: " + numPizzas);
    System.out.println("Total price: $" + price);
  }

}
