package src;

import java.time.LocalDate;           // handle Expense obj's LocalDate attribute  

import java.io.File;                  // File objects

import java.io.FileOutputStream;      // write to a File 
import java.io.ObjectOutputStream;    // write in objects
import java.io.FileInputStream;       // read from a File 
import java.io.ObjectInputStream;     // read in objects
import java.io.IOException;           // error handling
import java.io.FileNotFoundException; // file error handling 

import java.util.List;        
import java.util.ArrayList;   // holds retrieved list of Expense objs
import java.util.HashMap;     // holds retrieved expense dates w/corresponding lists of Expenses

/**
 * ExpenseLog stores and retrieves all expense entries
 * from .txt files that are grouped by year and 
 * organized into parent directories by month.
 */

public class ExpenseLog {

    // writes the new expense to a files that are organized by month
    // and placed in directories by year
    public boolean saveToLog(Expense exp) {

        String dirPath = "/Users/AmberRobbins/portfolio/projects/expenseManager/src/logFiles/" + exp.getYear();
        String filePath = "/Users/AmberRobbins/portfolio/projects/expenseManager/src/logFiles/" + exp.getYear() +
            "/" + exp.getMonth() + ".txt";

        // files organized by month
        File file = new File(filePath);

        boolean isSaved = false;
     
        // ensure the directory and file are created before writing to the file
        if (isDirectoryCreated(exp, dirPath) && isFileCreated(exp, file)) {
   
            // check if file already has expense data
            if (file.length() > 0) { 
                try (FileOutputStream fileOut = new FileOutputStream(filePath, true); // 'true' appends additional data into file
                    // must use this class to properly append new expenses to the same output stream as previous expenses
                    AppendObjectOutputStream objectOut = new AppendObjectOutputStream(fileOut)) {  

                    objectOut.writeObject(exp);
                    objectOut.close();
                    fileOut.close();
                    isSaved = true;
                } catch (FileNotFoundException e) {
			        System.out.println("\n  ERROR: File at '" + filePath + "'' not found.");
		        } catch (IOException e) {
			        System.out.println("\n  ERROR: Unable to initialize output stream");
                    System.out.println("  to write to filePath '" + filePath + "'.");
		        } 
            }
            // first expense entry in a file
            else {
                try (FileOutputStream fileOut = new FileOutputStream(filePath); 
                    ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {  // will serialize the Object's data

                    objectOut.writeObject(exp);
                    objectOut.close();
                    fileOut.close();
                    isSaved = true;
                } catch (FileNotFoundException e) {
			        System.out.println("\n  ERROR: File at '" + filePath + "'' not found.");
		        } catch (IOException e) {
			        System.out.println("\n  ERROR: Unable to initialize output stream");
                    System.out.println("  to write to filePath '" + filePath + "'.");
		        } 
            }
        }
        return isSaved;
    }


    // retrieves all expenses for a given month and year
    public HashMap<LocalDate, List<Expense>> getMonthlyExpenses(int month, int year) {

        
        // use the selected month and year to generate the filePath
        // of expenses made during that time
        String filePath = "/Users/AmberRobbins/portfolio/projects/expenseManager/src/logFiles/" + year + "/" + month + ".txt";

        HashMap<LocalDate, List<Expense>> monthlyExpenses = new HashMap<>();

        try (FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
                
            // reads in Expenses until the end of the file is reached
            do {

                // deserializes the object & stores in exp
                Expense exp = (Expense) objectIn.readObject();

                // adds each expense to the monthlyExpenses hashmap
                addToMonthlyExpenses(exp, monthlyExpenses);
            } while (fileIn.available() > 0);
            objectIn.close();
            fileIn.close();

        } catch (FileNotFoundException e) {
            System.out.println("\n  ERROR: File at '" + filePath + "'' not found.");
        } catch (IOException e) {
			System.out.println("\n  ERROR: Unable to initialize input stream");
            System.out.println("  to read from filePath '" + filePath + "'.");
		} catch (ClassNotFoundException e) {
            System.out.println("\n  ERROR: Class not found while reading object.");
        } 

        return monthlyExpenses;
    }


    private void addToMonthlyExpenses(Expense exp, HashMap<LocalDate, List<Expense>> monthlyExpenses) {
  
        // If it's a new date, create a new list to put inside monthlyExpenses
        if (!monthlyExpenses.containsKey(exp.getDate())) {  
            monthlyExpenses.put(exp.getDate(), new ArrayList<Expense>());
        }

        // retrieve the list of expenses for the date
        LocalDate date = exp.getDate();
        List<Expense> dailyExpenses = monthlyExpenses.get(date);

        // add the expense to the dailyExpenses list
        dailyExpenses.add(exp);

    }


    private boolean isDirectoryCreated(Expense exp, String dirPath) {

        // directories organized by year
        File directory = new File(dirPath);

        boolean dirCreated = false;

        // check if directory exists
        if (directory.exists()) {
            dirCreated = true;
        } else {
            try {    
                dirCreated = directory.mkdir(); // create the directory

            } catch (SecurityException e) {
                System.err.println("\n  ERROR: A problem occurred while creating the '" + exp.getYear() + "' directory: ");
                System.err.println("  " + e.getMessage());
            }
        }
        return dirCreated;
    }


    private boolean isFileCreated(Expense exp, File file) {

        boolean fileCreated = false;

        // check if file exists
        if (file.exists()) {
            fileCreated = true;
        } else {
            try { 
                fileCreated = file.createNewFile();  // create new file

            } catch (IOException e) {
                System.err.println("\n  ERROR: A problem occurred while creating the file: " + e.getMessage());
            }
        }
        return fileCreated;
    }
}