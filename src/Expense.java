package src;

// technically, java.lang classes are automatically imported. 
import java.lang.String;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.LocalDate;

// flags that expense objs can be converted and saved in files or
// transmitted over networks by converting it into binary format
import java.io.Serializable;

/**
 * @param: name - what the expense was used to pay for.
 * @param: amount - the monetary amount of the expense.
 * @param: category - the spending category of the expense.
 * @param: date - the date in which the expense was made.
 */


public class Expense implements Serializable {

    private String name;
    private double amount;
    private LocalDate date;
    private String category; 

    private static String[] expCategories = {
        "Housing",
        "Food",
        "Utilities",
        "Medical",
        "Insurance",
        "Household items",
        "Personal",
        "Pets",
        "Retirement",
        "Savings",
        "Education",
        "Gifts/Donations",
        "Travel",
        "Transportation",
        "Unspecified"
    };

    public Expense(String name, double amount) {

        this.name = name;
        this.amount = amount;

        this.category = null;
        this.date = null;
    }

    public void setCategory(int categoryNum) {

        String category = expCategories[categoryNum - 1];
        this.category = category;
    }

    public void setCurrentDate(LocalDate currDate) {
        this.date = currDate;
    }

    public void setPastDate(int month, int day, int year) {
        this.date = LocalDate.of(year, month, day);
    }

    public String getExpenseName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDay() {
        return date.getDayOfMonth();
    }

    public int getMonth() {
        return date.getMonthValue();
    }

    public int getYear() {
        return date.getYear();
    }

    public String getDateString() {
        String dateString = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)); 
        return dateString;
    }

    public String[] getCategoriesArray () {
        return expCategories;
    }

    public int getCategoriesLength() {
        return expCategories.length;
    }

    public void displayCategories() {
        for (int i = 0; i < expCategories.length; i++) {
            System.out.println("    " + (i + 1) + " - " + expCategories[i]);
        }
    }

    public void displayExpense() {

        System.out.println("  Name: " + name);
        System.out.println("  Amount: $" + amount);
        System.out.println("  Category: " + category);
    }
}
