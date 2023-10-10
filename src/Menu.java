package src;

import java.util.Scanner;

public class Menu {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        String userChoice = "0";
        Manager manage = new Manager();

        do {
            displayMainMenu();

            // intended to store one of the 5 menu options
            userChoice = input.nextLine();

            switch (userChoice) {
                // create new expense entry
                case "1":
                    manage.enterExpense();
                    break;
                // retrieve expenses by month and year
                case "2":
                    manage.retrieveExpenses();
                    break;
                // quit the expense tracker
                case "q":
                    System.out.println("\n  Goodbye!\n");
                    break;
                default:
                    System.out.println("\n  ERROR: Invalid choice entered.");
                    System.out.println("  Please enter a valid option from the menu below.\n");
                break;
            }
          // can't use != b/c String isn't primitive
        } while (!userChoice.equalsIgnoreCase("q"));

        input.close();
    }

    public static void displayMainMenu() {
        System.out.println("\n  Expense Tracker Options:\n");
        System.out.println("    1 - Create new expense entry.");
        System.out.println("    2 - Retrieve expenses by month and year.");
        System.out.println("    Press 'q' to quit.");

        System.out.print("\n\n  Enter your choice (1-2) > ");
    }
}
