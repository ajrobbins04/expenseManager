package src;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;  
import java.time.LocalDate; 
import java.time.Year;

/**
 * Handles all actions to be performed based on the user's
 * input. Is currently able to input and save new expenses,
 * along with retrieve and display those expenses by month.
 * 
 * @param: input - Scanner obj to collect user input
 * @param: expLog - the expense log that stores records
 * @param: expCategories - available expense categories
 */

public class Manager {
    
    Scanner input = new Scanner(System.in);
    ExpenseLog expLog = new ExpenseLog();


    public void enterExpense() {

        // get expense name
        System.out.print("\n  Enter the name of the expense > ");
        String name = input.nextLine();

        // starts w/invalid amount
        double amount = -1.0;

        // continue getting expense amount until it is valid
        do {
            System.out.print("\n  Enter the expense amount > $");
            if (input.hasNextDouble()) {
                amount = input.nextDouble();
            }
            else {
                System.out.println("\n  ERROR: Invalid input entered. Please enter an amount greater than 0.");
            }
            input.nextLine(); // consumes the newline character after invalid input, nextDouble, or nextInt
        } while (amount <= 0.0);


        // create expense obj w/its name and amount for starters
        Expense exp = new Expense(name, amount);

        // add category to the expense
        addCategory(exp);

        // add date to the expense
        addExpenseDate(exp);

        boolean isSaved = expLog.saveToLog(exp);

        // save the expense to the expense log
        if (isSaved) 
            System.out.println("\n  " + name + " for $" + amount + " successfully added to your expenses!");
        else
            System.out.println("\n  ERROR: " + name + " for $" + amount + " could not be added to your expenses.");
  
    }


    public void addCategory(Expense exp) {

        // start with an invalid category number
        int categoryNum = -1;

        System.out.println("\n  Select a category number from the options below:\n");

        // continue getting category number until it is valid
        do {
            // display all categories in the Expense.expCategories array
            exp.displayCategories();

            System.out.print("\n\n  Enter your choice (1 - " + (exp.getCategoriesLength() + 1) + ") > ");

            // check that input is a int before accepting it
            if (input.hasNextInt()) {
                categoryNum = input.nextInt();
                input.nextLine(); 

                if (categoryNum < 1 || categoryNum > (exp.getCategoriesLength() + 1)) {
                    System.out.println("\n  ERROR: Invalid category entered.");
                    System.out.println("Please enter a category number from the options below:\n");
                    input.nextLine();
                }
            } 
            // input wasn't an integer
            else {
                System.out.println("\n  ERROR: Invalid input.");
                System.out.println("Please enter a category number from the options below:\n");
                input.nextLine();  // consume invalid input
            }   
        } while (categoryNum < 1 || categoryNum > exp.getCategoriesLength());

        exp.setCategory(categoryNum);
    }


    public void addExpenseDate(Expense exp) {

        int dateOption = -1;

        do {
            System.out.println("\n  Select the date option to be added to the expense:\n");
            System.out.println("    1 - Use the current date.");
            System.out.println("    2 - Enter a past date.");

            System.out.print("\n  Enter your choice (1-2) > ");

            // check if next input is an integer
            if (input.hasNextInt()) {
                dateOption = input.nextInt();
                input.nextLine();

                // assign current date to expense
                if (dateOption == 1) {
                    LocalDate currDate = LocalDate.now();
                    exp.setCurrentDate(currDate);
        
                // assigns a past date
                } else if (dateOption == 2) {
                    assignPastDate(exp);

                // invalid input entered
                } else {
                    System.out.println("\n  ERROR: Invalid input. Please try again.");
                    input.nextLine();
                }
            }
            // input was not an integer
            else {
                System.out.println("\n  ERROR: Invalid input. Please try again.");
                input.nextLine();  // consume invalid input
            }
        } while (dateOption != 1 && dateOption != 2);
    }


    public void retrieveExpenses() {

        int monthYearValues[] = getMonthYearValues(); 
        int month = monthYearValues[0];
        int year = monthYearValues[1];     
        
        HashMap<LocalDate, List<Expense>> monthlyExpenses = expLog.getMonthlyExpenses(month, year);

        displayMonthlyExpenses(monthlyExpenses, month, year);
    }


    // returns an integer array containing the values for 
    // the month and year selected by the user 
    private int[] getMonthYearValues() {

        int month = -1;
        int year = -1;
        boolean isValid = false;

        do {
            System.out.print("\n  Enter the month name and year (month yyyy) > ");
            String monthYear = input.nextLine();

            // split string up into separate strings containing month and year
            String [] monthYearSplit = monthYear.split(" ");

            if (monthYearSplit.length == 2) {
                String monthName = monthYearSplit[0];
                month = convertMonthToValue(monthName);

                if (isMonthValid(month)) {
                    year = Integer.parseInt(monthYearSplit[1]);

                    if (isYearValid(year)) {
                        isValid = true;
                    } else {
                    System.out.println("\n  ERROR: Invalid year entered. Please try again.");
                    input.nextLine();
                    }
                } else {
                    System.out.println("\n  ERROR: Invalid month entered. Please try again.");
                    input.nextLine();
                }
            } else {
                System.out.println("\n  ERROR: Invalid input entered. Please try again.");
                input.nextLine();
            }
        } while (!isValid);

        int monthYearValues [] = new int[2];
        monthYearValues[0] = month;
        monthYearValues[1] = year;

        return monthYearValues;
    }


    public void displayMonthlyExpenses(HashMap<LocalDate, List<Expense>> monthlyExpenses, int month, int year) {
    
        int numDays = getNumDays(month, year);

        for (int day = 1; day <= numDays; day++) {
            LocalDate date = LocalDate.of(year, month, day);

            if (monthlyExpenses.containsKey(date)) {
                List<Expense> dailyExpenses = monthlyExpenses.get(date);

                int count = 0;
                for (Expense exp : dailyExpenses) {
                    if (count == 0) {
                        System.out.println("\n  --- " + exp.getDateString() + " --- \n");
                    } else {
                        System.out.print("\n");
                    }
                    exp.displayExpense();
                    count++;
                }
            }
        }
    }


    private void assignPastDate(Expense exp) {

        boolean isValidDate = false;

        do {
            System.out.print("\n  Enter the date (mm/dd/yyyy) > ");
            String dateInput = input.nextLine();
            String[] dateComponents = dateInput.split("/");

            if (dateComponents.length == 3) {
                try {
                    // parse components into month, day, year
                    int month = Integer.parseInt(dateComponents[0]);
                    int day = Integer.parseInt(dateComponents[1]);
                    int year = Integer.parseInt(dateComponents[2]);

                    if (isValidDate(month, day, year)) {
                        exp.setPastDate(month, day, year);
                        isValidDate = true;
                    } else {
                        System.out.println("\n  ERROR: An invalid date was entered. Please try again.");
                    }
                } 
                // thrown when string can't be converted to numeric type
                catch (NumberFormatException e) {
                    System.out.println("\n  Invalid date format. Please enter date as mm/dd/yyyy.");
                    input.nextLine();
                }
             } else {
                System.out.println("\n  ERROR: Invalid date format. Please enter date as mm/dd/yyyy.");
                input.nextLine(); // consume invalid input
            }

        } while (!isValidDate);
    }


    private boolean isValidDate(int month, int day, int year) {
        if (isMonthValid(month)) {
            if (isYearValid(year)) {
                if(isDayValid(day, month, year)) {
                    return true;
                }
                else {
                    System.out.println("\n  ERROR: Invalid day entered.");
                    return false;
                }
            }
            else {
                System.out.println("\n  ERROR: Invalid year entered.");
                return false;
            }
        }
        else {
            System.out.println("\n  ERROR: Invalid month entered.");
            return false;
        }
    }


    private boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            return true; // is a leap year
        } else {
            return false; // is not a leap year
        }
    }


    private boolean isMonthValid(int month) {
        return month > 0 && month < 13;
    }


    // expense date must be from the current year, 
    // or a previous one up to 2010 at the earliest.
    private boolean isYearValid(int year) {
        Year currentYear = Year.now();

        if (year >= 2010) {
            Year yearToValidate = Year.of(year);

            // can't check if yearToValidate <= currentYear
            // because Year is not a primitive data type.
            if (yearToValidate.isAfter(currentYear)) {
                System.out.println("\n  ERROR: Cannot enter future years.");
                return false;
            }   
        } else {
            System.out.println("\n  ERROR: The year entered is too early.");
            System.out.println("  System will not accept any years prior to 2010\n"); 
            return false;
        }

        return true;
    }


    private int convertMonthToValue(String month) {

        int monthNum = -1;

        switch (month.toLowerCase()) {
            case "january":
                monthNum = 1;
                break;
            case "february":
                monthNum = 2;
                break;
            case "march":
                monthNum = 3;
                break;
            case "april":
                monthNum = 4;
                break;
            case "may":
                monthNum = 5;
                break;
            case "june":
                monthNum = 6;
                break;
            case "july":
                monthNum = 7;
                break;
            case "august":
                monthNum = 8;
                break;
            case "september":
                monthNum = 9;
                break;
            case "october":
                monthNum = 10;
                break;
            case "november":
                monthNum = 11;
                break;
            case "december":
                monthNum = 12;
                break;
        }
        // returns a -1 if invalid
        return monthNum;
    }


    private int getNumDays(int month, int year) {

        int numDays = -1;

        switch (month) {
            case 1: // January
            case 3: // March
            case 5: // May
            case 7: // July
            case 8: // August
            case 10: // October
            case 12: // December
                return numDays = 31;

            // Months with 30 days
            case 4: // April
            case 6: // June
            case 9: // September
            case 11: // November
                return numDays = 30;

            // February (considering leap years)
            case 2:
                if (isLeapYear(year)) {
                    return numDays = 29;
                } else {
                    return numDays = 28;
                }
        }
        return numDays; // returns -1 if invalid
    }


    // ensure the day is valid based on the
    // given month and year
    private boolean isDayValid(int day, int month, int year) {

        switch (month) {
            // Months with 31 days
            case 1: // January
            case 3: // March
            case 5: // May
            case 7: // July
            case 8: // August
            case 10: // October
            case 12: // December
                return day >= 1 && day <= 31;

            // Months with 30 days
            case 4: // April
            case 6: // June
            case 9: // September
            case 11: // November
                return day >= 1 && day <= 30;

            // February (considering leap years)
            case 2:
                if (isLeapYear(year)) {
                    return day >= 1 && day <= 29;
                } else {
                    return day >= 1 && day <= 28;
                }
        }
        return false; // Default case
    }
}
