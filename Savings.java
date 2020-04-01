import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Total Savings
 * @author Luke
 */
public class Savings {
    private boolean running;
    Scanner scanner = new Scanner(System.in);

    public void mainMenu(){
        running = true;
        while (running) {
            System.out.println("Please select an option by using the number associated with each command:\n" +
                    "1. Add new savings pool\n" +
                    "2. Edit an existing savings pool\n" +
                    "3. Delete a savings pool\n" +
                    "4. View your savings pools\n" +
                    "5. Return to main menu");
            String input = scanner.nextLine();
            inputChecker(input);
        }
    }

    /**
     * takes input from main menu and calls correct method
     * @param input their choice from main menu options
     */
    private void inputChecker(String input) {
        switch (input) {
            case "1":
                newSavingPool();
                break;
            case "2":
                editSavingPool();
                break;
            case "3":
                removeSavingPool();
                break;
            case "4":
                printAllSavingPools();
                break;
            case "5":
                running = false;
                System.out.println("Exiting to Main Menu...\n\n");
                break;
            default:
                System.out.println("Not an option. Choose again");

        }
    }

    public void newSavingPool(/*BufferedReader reader */) throws InputMismatchException {
        String today = Instant.now().toString();
        today = today.substring(8,10) + "/" + today.substring(5,7) + "/" + today.substring(0,4);

        try {
            boolean cont = false;

            System.out.println("First, what is the name of the category?");
            String name = scanner.nextLine();
            //String name = reader.readLine();

            System.out.println("Awesome, how much do you want to save? (in £)");
            double goal = scanner.nextDouble();
            scanner.nextLine();
            //int goal = reader.read();
            while (!cont) {
                if (goal > 0) {
                    cont = true;
                } else {
                    System.out.println("Invalid amount, how much do you want to save?");
                    goal = scanner.nextDouble();
                    scanner.nextLine();
                    //int goal = reader.read();
                }
            }

            System.out.println("And do you have anything saved here already? (Y/N)");
            cont = false;
            double startingAmount = 0;
            String temp = scanner.nextLine();
            if (temp.toLowerCase().equals("y") || temp.toLowerCase().equals("yes")) {
                System.out.println("How much?");
                startingAmount = scanner.nextDouble();
                scanner.nextLine();
                //int startingAmount = reader.read();
                while (!cont) {
                    if (startingAmount > 0) {
                        cont = true;
                    } else {
                        System.out.println("Invalid amount, how much do you want to save?");
                        startingAmount = scanner.nextDouble();
                        scanner.nextLine();
                        //startingAmount = scanner.read();
                    }
                }
            }

            System.out.println("And will your be making monthly contributions? (Y/N)");
            temp = scanner.nextLine();
            double monthlyContribution = 0;
            if (temp.toLowerCase().equals("y") || temp.toLowerCase().equals("yes")) {
                cont = false;
                System.out.println("How much?");
                monthlyContribution = scanner.nextDouble();
                scanner.nextLine();
                //int startingAmount = reader.read();
                while (!cont) {
                    if (monthlyContribution > 0) {
                        cont = true;
                    } else {
                        System.out.println("Invalid amount, how much do you want to save?");
                        monthlyContribution = scanner.nextDouble();
                        scanner.nextLine();
                        //startingAmount = scanner.read();
                    }
                }
            }

            System.out.println("Lastly, when do you want to meet this goal? (DD/MM/YYYY)");
            String dateToAchieve = scanner.nextLine();
            //String dateToAchieve = reader.readLine();
            if (!validDate.validFormat(dateToAchieve) || !validDate.compareStringDates(today,dateToAchieve)) {
                System.out.println("Invalid date!");
                return;
            }

            RetrieveAndStore.sqlExecute("INSERT INTO tblSavings (SavingsAccountName, SavingsGoal, CurrentSavings, " +
                    "MonthlyContribution, DateCreated, DateProjected) VALUES ('" + name + "', " + goal + ", " +
                    startingAmount + ", " + monthlyContribution + ", '"  + today + "', '" + dateToAchieve + "')");

            rowNumberUpdater("tblSavings","ID");
            System.out.println("Great stuff, lets get saving!\n\n");
        } catch (InputMismatchException e) {
            System.out.println("Wrong input given");
        }
    }

    public void printAllSavingPools(){
        ResultSet rs = RetrieveAndStore.readAllRecords("tblSavings");
        try {
            while (rs.next()) //Loop through the resultset
            {
                //Store each budget record to print
                int id = rs.getInt("ID");
                String name = rs.getString("SavingsAccountName");
                double goal = rs.getDouble("SavingsGoal");
                double currentSavings = rs.getDouble("CurrentSavings");
                double contribution = rs.getDouble("MonthlyContribution");
                String dateCreated = rs.getString("DateCreated");
                String dateLimit = rs.getString("DateProjected");

                //calculate projections
                String today = Instant.now().toString();
                today = today.substring(8,10) + "/" + today.substring(5,7) + "/" + today.substring(0,4);
                String projection = "You'll hit your goal ";
                double time;
                double leftToGo = goal-currentSavings;
                if (!(contribution == 0) && currentSavings < goal) {
                    if (leftToGo % contribution == 0) {
                        if ((time = leftToGo / contribution) == 1) {
                            projection += "next month! Good Job";
                        } else {
                            projection += "in " + (int) time + " months, the " + Date.addMonths(today, (int) time)
                                    + "\nTIP: Dont worry if that seems like a long time off, with the right bank " +
                                    "(and possibly the right investments too!) that time can always be reduced!";
                        }
                    } else {
                        if ((time = leftToGo / contribution) < 1) {
                            projection += "next month! Good Job";
                        } else {
                            projection += "in " + (int) time + " months, the " + Date.addMonths(today, (int) time + 1)
                                    + "\nTIP: Dont worry if that seems like a long time off, with the right bank " +
                                    "(and possibly the right investments too!) that time can always be reduced!";
                        }
                    }
                } else if (goal < currentSavings){
                    projection = "Great work, you've already met your savings goal!";
                } else {
                    projection = "Oh no, it looks like you not saving any extra per month.\n" +
                            "TIP: Remember, even the smallest amounts of savings can add up in time, especially in a high interest account!";
                }

                // print the results
                System.out.format("%d: %s\nPool: £%.2f/£%.2f @ £%.2fpm\n%s - %s\n%s\n\n", id+1, name, currentSavings, goal, contribution,
                        dateCreated, dateLimit, projection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editSavingPool() {

        printAllSavingPools();
        System.out.println("Please enter the ID number of the Pool you would like to update:");
        int poolID = scanner.nextInt() - 1;
        scanner.nextLine();
        if (!Validation.isRangeValid(0, RetrieveAndStore.maxID("tblSavings", "ID"), poolID)) return;
        ResultSet rs = RetrieveAndStore.readAllRecords("tblSavings");
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("Great, which entry would you liked to update?");
            for (int i = 1; i < rsmd.getColumnCount(); i++) {
                System.out.println(String.format("%d: %s", i, rsmd.getColumnName(i)));
            }

            int columnInt = scanner.nextInt();
            if (!Validation.isRangeValid(1, rsmd.getColumnCount()-1, columnInt)) return;
            scanner.nextLine();
            String columnName = rsmd.getColumnName(columnInt);
            System.out.println("Please input a new value for " + columnName);

            if (columnInt>=4) {
                int newValue = scanner.nextInt();
                scanner.nextLine();
                RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = %d WHERE %s = %d", "tblSavings",
                        columnName, newValue, "ID", poolID));
            } else if (columnInt == 2 || columnInt == 3){ // for date created
                String newValue = scanner.nextLine();
                String today = Instant.now().toString();
                today = today.substring(8,10) + "/" + today.substring(5,7) + "/" + today.substring(0,4);
                if(columnInt == 2 && validDate.validFormat(newValue) && validDate.compareStringDates(today, newValue, true)){
                    RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = '%s' WHERE %s = %s", "tblSavings",
                            columnName, newValue, "ID", poolID));
                } else if(columnInt == 3 && validDate.validFormat(newValue) && validDate.compareStringDates(today, newValue)){
                    RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = '%s' WHERE %s = %s", "tblSavings",
                            columnName, newValue, "ID", poolID));
                }
            }  else {
                String newValue = scanner.nextLine();
                RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = '%s' WHERE %s = %d", "tblSavings",
                        columnName, newValue, "ID", poolID));
            }
            System.out.println("\n\nSuccessfully update field " + columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeSavingPool(){
        printAllSavingPools();
        System.out.println("Please enter the ID number of the Pool you would like to delete:");
        int poolID = scanner.nextInt();
        scanner.nextLine();
        if (!Validation.isRangeValid(0, RetrieveAndStore.maxID("tblSavings", "ID"), poolID)) return;
        RetrieveAndStore.sqlExecute(String.format("DELETE FROM %s WHERE %s = %d", "tblSavings", "ID", poolID));
        rowNumberUpdater("tblSavings", "ID");
        System.out.println("Done, its deleted, gone forever, turned to smithereens, never to be seen again...");
    }

    public void rowNumberUpdater(String tableName, String columnName){
        ResultSet rs = RetrieveAndStore.readAllRecords(tableName);
        int i = 0;
        try {
            while(rs.next()){
                RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = %d WHERE %s = %d;", tableName, columnName, i, columnName, rs.getInt(columnName)));
                ++i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /* TODO:
        Edit pool needs some InputMismatch stuff
        Use global buffered reader instead of local scanner
        Comments
 */
}
