import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.lang.Object;

/**
 * Total Savings
 * @author Luke
 */
public abstract class savingsTrue {
    static Scanner  scanner = new Scanner(System.in);

    public static boolean newSavingPool(/*BufferedReader reader */) throws InputMismatchException {
        String today = Instant.now().toString();
        today = today.substring(8,10) + "/" + today.substring(5,7) + "/" + today.substring(0,4);

        try {
            boolean cont = false;

            System.out.println("First, what is the name of the category?");
            String name = scanner.nextLine();
            //String name = reader.readLine();

            System.out.println("Great, how much do you want to save? (in £)");
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

            System.out.println("Awesome, will your be making monthly contributions? (Y/N)");
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
                return false;
            }

            RetrieveAndStore.sqlExecute("INSERT INTO tblSavings (SavingsAccountName, SavingsGoal, CurrentSavings, " +
                    "MonthlyContribution, DateCreated, DateProjected) VALUES ('" + name + "', " + goal + ", " +
                    startingAmount + ", " + monthlyContribution + ", '"  + today + "', '" + dateToAchieve + "')");


            return true;
        } catch (InputMismatchException e) {
            System.out.println("Wrong input given");
            return false;

        }
    }

    public static void printAllSavingPools(){
        ResultSet rs = RetrieveAndStore.readAllRecords("tblSavings");
        try {
            while (rs.next()) //Loop through the resultset
            {
                //Store each budget record to print
                int id = rs.getInt("SavingsAccountID");
                String name = rs.getString("SavingsAccountName");
                double goal = rs.getDouble("SavingsGoal");
                double currentSavings = rs.getDouble("CurrentSavings");
                double contribution = rs.getDouble("MonthlyContribution");
                String dateCreated = rs.getString("DateCreated");
                String dateProjected = rs.getString("DateProjected");

                // print the results
                System.out.format("%d: %s\nPool: £%.2f/£%.2f @ £%.2fpm\n%s - %s\n\n", id, name, currentSavings, goal, contribution,
                        dateCreated, dateProjected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void editSavingPool(){

    }
}
