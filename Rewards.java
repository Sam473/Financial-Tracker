import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

//trophy

public class Rewards {
    private boolean running;

    /**
     * Will show all options when managing their savings pools and prompt for them to choose one
     */
    public void mainMenu() throws IOException {
        ResultSet rs = RetrieveAndStore.readAllRecords("tblSavings");
        int count = 0;
        ArrayList<String> savingNames = new ArrayList<String>();
        try {
            while (rs.next()) {
                //Store each budget record to print
                double goal = rs.getDouble("SavingsGoal");
                double currentSavings = rs.getDouble("CurrentSavings");

                double leftToGo = goal-currentSavings; // How far they are from their goal in £
                if (currentSavings > goal) {
                    count++;
                    savingNames.add(rs.getString("SavingsAccountName"));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error with DB");
        }
        if (count != 0) {
            System.out.println("\nGood job, you managed to exceed your savings in the following pools:");
            System.out.println(String.join(", ", savingNames));
            System.out.println("\nTrophy Count \uD83C\uDFC6:" + count);
        } else {
            System.out.println("Oh no, looks like you haven't exceeded any savings");
            System.out.println(new Tips().getTipMotivation("tip"));
        }
        System.out.println("Press enter to return to main menu...");
        App.userIn.readLine();
    }
}


