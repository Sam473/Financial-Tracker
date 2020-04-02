import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.util.InputMismatchException;

/**
 * Total Savings
 * @author Luke
 */
public class Savings {
    private boolean running;

    /**
     * Will show all options when managing their savings pools and prompt for them to choose one
     */
    public void mainMenu(){
        running = true;
        while (running) {
            System.out.println("Please select an option by using the number associated with each command:\n" +
                    "1. Add new savings pool\n" +
                    "2. Edit an existing savings pool\n" +
                    "3. Delete a savings pool\n" +
                    "4. View your savings pools\n" +
                    "5. Return to main menu");
            try {
                String input = App.userIn.readLine();
                inputChecker(input);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to take input from console");
            }
        }
    }

    /**
     * takes input from main menu and calls correct method
     * @param input their choice from main menu options
     */
    private void inputChecker(String input) throws IOException{
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

    /**
     * Will prompt for the following and add them as a new pool in table:
     * - Name
     * - Amount to save
     * - starting amount (optional)
     * - monthly contribution (option)
     * - date to save until
     *
     * @throws InputMismatchException security incase they give a int for string, or something similar
     * @throws IOException if we cant read from console
     */
    public void newSavingPool() throws InputMismatchException, IOException {
        boolean cont = false;

        // Name
        System.out.println("First, what is the name of the category?");
        String name = App.userIn.readLine();

        // Amount they want to save in the new pool
        System.out.println("Awesome, how much do you want to save? (in £)");
        String goalString = App.userIn.readLine();
        if (!Validation.isDouble(goalString)) return;
        double goal = Double.parseDouble(goalString);
        while (!cont) { // Will loop until they give a valid input for the amount they want to save
            if (goal > 0) {
                cont = true;
            } else {
                System.out.println("Invalid amount, how much do you want to save?");
                goalString = App.userIn.readLine();
                if (!Validation.isDouble(goalString)) continue;
                goal = Double.parseDouble(goalString);
            }
        }

        // If they already have something saved in the account (Optional)
        System.out.println("And do you have anything saved here already? (Y/N)");
        cont = false;
        double startingAmount = 0;
        String temp = App.userIn.readLine();
        if (temp.toLowerCase().equals("y") || temp.toLowerCase().equals("yes")) {
        // If they say yes or y to haveing something pre-saved
            System.out.println("How much?");
            String startingAmountString = App.userIn.readLine();
            if (!Validation.isDouble(startingAmountString)) return;
            startingAmount = Double.parseDouble(startingAmountString);
            while (!cont) { // Loop until they give a valid amount
                if (startingAmount > 0) {
                    cont = true;
                } else {
                    System.out.println("Invalid amount, how much do you want to save?");
                    startingAmountString = App.userIn.readLine();
                    if (!Validation.isDouble(startingAmountString)) continue;
                    startingAmount = Double.parseDouble(startingAmountString);
                }
            }
        }

        // If they will be making a monthly contribution to their pool (Optional)
        System.out.println("And will your be making monthly contributions? (Y/N)");
        temp = App.userIn.readLine();
        double monthlyContribution = 0;
        if (temp.toLowerCase().equals("y") || temp.toLowerCase().equals("yes")) {
            // If they say yes or y to making monthly contributions
            cont = false;
            System.out.println("How much?");
            String monthlyContributionString = App.userIn.readLine();
            if (!Validation.isDouble(monthlyContributionString)) return;
            monthlyContribution = Double.parseDouble(monthlyContributionString);
            while (!cont) { // Loop until they give a valid amount
                if (monthlyContribution > 0) {
                    cont = true;
                } else {
                    System.out.println("Invalid amount, how much do you want to save?");
                    monthlyContributionString = App.userIn.readLine();
                    if (!Validation.isDouble(monthlyContributionString)) continue;
                    monthlyContribution = Double.parseDouble(monthlyContributionString);
                }
            }
        }


        //will get todays date, for use when creating a new table entry
        String today = Instant.now().toString();
        today = today.substring(8,10) + "/" + today.substring(5,7) + "/" + today.substring(0,4);
        //When they want to have the money saved by
        System.out.println("Lastly, when do you want to meet this goal? (DD/MM/YYYY)");
        String dateToAchieve = App.userIn.readLine();
        // Will check if date is in valid format and it is after today
        if (!validDate.validFormat(dateToAchieve) || !validDate.compareStringDates(today,dateToAchieve)) {
            System.out.println("Invalid date!");
            return;
        }

        RetrieveAndStore.sqlExecute("INSERT INTO tblSavings (ID, SavingsAccountName, SavingsGoal, CurrentSavings, " +
                "MonthlyContribution, DateCreated, DateProjected) VALUES ("
                        + (RetrieveAndStore.maxID("tblSavings", "ID") + 1) + ", '" + name + "', " + goal + ", " +
                startingAmount + ", " + monthlyContribution + ", '"  + today + "', '" + dateToAchieve + "')");

        rowNumberUpdater("tblSavings","ID");
        System.out.println("Great stuff, lets get saving!\n\n");
    }

    /**
     * Allow a user to see all their saving pools
     * Made static so can be used in RequestData
     * @see RequestData
     */
    public static void printAllSavingPools(){
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

                // Work out todays date in dd/mm/yyyy format
                String today = Instant.now().toString();
                today = today.substring(8,10) + "/" + today.substring(5,7) + "/" + today.substring(0,4);
                //calculate projections
                String projection = "You'll hit your goal "; // Pre-write first half of string to stop repeat code
                double time;
                double leftToGo = goal-currentSavings; // How far they are from their goal in £
                // Checks we wont get 0 division and they haven't yet hit their goal
                if (!(contribution == 0) && currentSavings < goal) {
                    // If they'll hit their goal EXACTLY on a payment (extra month needed if they dont)
                    if (leftToGo % contribution == 0) {
                        if ((time = leftToGo / contribution) == 1) {
                            projection += "next month! Good Job";
                        } else { // Tells them how long till they hit their goal
                            projection += "in " + (int) time + " months, the " + Date.addMonths(today, (int) time)
                                    + "\nTIP: Dont worry if that seems like a long time off, with the right bank " +
                                    "(and possibly the right investments too!) that time can always be reduced!";
                        }
                    } else { // If they wont hit it on a payment, an extra month will be needed
                        if ((time = leftToGo / contribution) < 1) { // Check if they will be overpaying
                            projection += "next month! Good Job";
                        } else { // Tells them how long till they hit their goal
                            projection += "in " + (int) time + " months, the " + Date.addMonths(today, (int) time + 1)
                                    + "\nTIP: Dont worry if that seems like a long time off, with the right bank " +
                                    "(and possibly the right investments too!) that time can always be reduced!";
                        }
                    }
                } else if (goal < currentSavings){ // If they hit they hit the saving goal already
                    projection = "Great work, you've already met your savings goal!";
                } else { // If they not contributing offer a friendly reminder that 'every little helps' - Tesco (TM)
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

    /**
     * Allow them to edit any field (except ID) for a pool
     * @throws IOException if we cant read from console
     */
    public void editSavingPool() throws IOException {
        printAllSavingPools(); // Show them all their pools

        System.out.println("Please enter the ID number of the Pool you would like to update:");
        String poolIDString = App.userIn.readLine();
        if (!Validation.isInteger(poolIDString)) return;
        int poolID = Integer.parseInt(poolIDString) - 1; // Must be less than one as row ID's start from 0
        // Check it is a valid ID
        if (!Validation.isRangeValid(0, RetrieveAndStore.maxID("tblSavings", "ID"), poolID)) return;
        ResultSet rs = RetrieveAndStore.readAllRecords("tblSavings");
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("Great, which entry would you liked to update?");
            for (int i = 2; i <= rsmd.getColumnCount(); i++) { // Loop through all columns and print column name
                System.out.println(String.format("%d: %s", i-1, rsmd.getColumnName(i)));
            }
            // They choose one of the columns printed above
            String columnIntString = App.userIn.readLine();
            if (!Validation.isInteger(columnIntString)) return;
            int columnInt = Integer.parseInt(columnIntString)+1;
            if (!Validation.isRangeValid(1, rsmd.getColumnCount()-1, columnInt)) return;
            String columnName = rsmd.getColumnName(columnInt);
            System.out.println("Please input a new value for " + columnName);

            if (3<=columnInt && columnInt<=5) { // If they choose a column which takes an integer (e.g. Savings goal) - Double
                String newValueString = App.userIn.readLine();
                if (!Validation.isDouble(newValueString)) return;
                double newValue = Double.parseDouble(newValueString);
                RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = %d WHERE %s = %d", "tblSavings",
                        columnName, newValue, "ID", poolID));
            } else if (columnInt == 6 || columnInt == 7){ // For date created and date they aim to meet their goal - String, but in date format
                String newValue = App.userIn.readLine();
                String today = Instant.now().toString();
                today = today.substring(8,10) + "/" + today.substring(5,7) + "/" + today.substring(0,4);
                if(columnInt == 6 && validDate.validFormat(newValue)
                        && validDate.compareStringDates(today, newValue, true)
                        && validDate.compareStringDates(newValue, rs.getString("DateProjected"))){
                    // If they change date created, they can choose today as a date.
                    // No need to let them choose an earlier date but cant be later then when they want it to be done
                    RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = '%s' WHERE %s = %s", "tblSavings",
                            columnName, newValue, "ID", poolID));
                } else if(columnInt == 7 && validDate.validFormat(newValue)
                        && validDate.compareStringDates(today, newValue)
                        && validDate.compareStringDates(rs.getString("DateCreated"),newValue)){
                    // Check its after today and after the day the pool was created
                    RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = '%s' WHERE %s = %s", "tblSavings",
                            columnName, newValue, "ID", poolID));
                }
            }  else { // For everything else (i.e. category name) - String
                String newValue = App.userIn.readLine();
                RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = '%s' WHERE %s = %d", "tblSavings",
                        columnName, newValue, "ID", poolID));
            }
            System.out.println("\n\nSuccessfully update field " + columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove a pool from the table
     * @throws IOException if it cant receive input from console
     */
    public void removeSavingPool() throws IOException {
        System.out.println("\n\n");
        printAllSavingPools(); // Show them all their current pools
        System.out.println("Please enter the ID number of the Pool you would like to delete:");
        String poolIDString = App.userIn.readLine();
        if (!Validation.isInteger(poolIDString)) return;
        int poolID = Integer.parseInt(poolIDString)-1;
        if (!Validation.isRangeValid(0, RetrieveAndStore.maxID("tblSavings", "ID"), poolID)) return;
        RetrieveAndStore.sqlExecute(String.format("DELETE FROM %s WHERE %s = %d", "tblSavings", "ID", poolID));
        rowNumberUpdater("tblSavings", "ID");
        System.out.println("Done, its deleted, gone forever, turned to smithereens, never to be seen again...\n\n");
    }

    /**
     * Will update all ID's in a given tables column so it counts from 0 to the maximum row number
     * CAUTION: will overwrite all data in the column
     * @param tableName String, name of table to be edited
     * @param columnName String, name of column to be edited
     */
    public void rowNumberUpdater(String tableName, String columnName){
        ResultSet rs = RetrieveAndStore.readAllRecords(tableName);
        int i = 0;
        try {
            while(rs.next()){ // Loop through all pools in table
                RetrieveAndStore.sqlExecute(String.format("UPDATE %s SET %s = %d WHERE %s = %d;", tableName, columnName, i, columnName, rs.getInt(columnName)));
                ++i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
