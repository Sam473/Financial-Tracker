import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class RecurringOutgoings {
    private String[][] fileRequirements = {{"userName","defaultUser"},{"incomes","0"},{"outgoings",""}};
    File file = new File("user_data.properties");
    Properties properties = new Properties();
    private boolean running;

    public RecurringOutgoings() {
        openNewFile();
    }

    /**
     * used to load a file into properties object
     *
     */
    private void openNewFile() {
        try {
            if (file.createNewFile()) {
                System.out.println("Created new file");
            } else {
                System.out.println("File already exists...");
            }
            properties.load(new FileInputStream(file)); //load the user_data.properties
            checkFileFormat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * will check all the required keys are in properties file. If not, will create it
     *
     */
    private void checkFileFormat() {
        for (String[] key : fileRequirements){
            if (properties.getProperty(key[0]) == null) {
                properties.setProperty(key[0], key[1]);
            }
        }
        saveProperties();
    }


    /**
     * saves changes to the file so other applications can see it
     *
     */
    private void saveProperties() {
        try {
            FileOutputStream fr = new FileOutputStream(file);
            properties.store(fr, "Properties");
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * will ask for which option on
     */
    public void mainMenu() {
        running = true;
        while (running) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please select an option by using the character in brackets:\n" +
                    "1. Add new regular payment (Monthly)\n" +
                    "2. View all outgoings\n" +
                    "3. Your total outgoings\n" +
                    "4. Remove outgoing\n" +
                    "5. Return to main menu");
            String input = scanner.nextLine();
            System.out.println(inputChecker(input));
        }
    }

    /**
     * takes input from main menu and calls correct method
     * @param input their choice from main menu options
     * @return Success/failure message
     */
    private String inputChecker(String input) {
        switch (input) {
            case "1":
                addOutgoing();
                return "Successfully added new outgoing\n";
            case "2":
                viewOutgoings();
                return "";
            case "3":
                return "Your total outgoings:\n£" + totalOutgoings();
            case "4":
                System.out.println("Please enter the outgoing you would like to remove (exactly as it appears)");
                removeOutgoing();
                return "Successfully removed outgoing";
            case "5":
                running = false;
                return "Exiting to Main Menu...\n\n";
            default:
                return "Not an option. Choose again";

        }
    }


    /**
     * will sum all outgoings
     * @return sum of all outgoings
     */
    private float totalOutgoings()  {
        if (!properties.getProperty("outgoings").equals("")) {
            String[] outgoings = properties.getProperty("outgoings").split(",");
            float totalOutgoings = 0;
            for (String outgoing : outgoings) {
                try {
                    totalOutgoings += Float.parseFloat(outgoing);
                } catch (NumberFormatException e) {
                    System.out.println("Error: " + outgoing + " not a float or integer");
                    properties.setProperty("outgoings",properties.getProperty("outgoings").replaceAll(","+outgoing,""));
                    //will remove the faulty value from the outgoings list
                    saveProperties();
                }
            }
            return totalOutgoings;
        } else {
            return 0;
        }
    }

    private void viewOutgoings() {
        String[] outcomes = properties.getProperty("outgoings").split(",");
        for (String outcome : outcomes) {
            System.out.println(outcome);
        }
    }


    /**
     * will add an outgoing to the outgoings in properties file
     */
    private void addOutgoing() {
        Scanner scannerOutgoing = new Scanner(System.in);
        System.out.println("How much will you be paying per month?");
        properties.setProperty("outgoings",properties.getProperty("outgoings") + "," + scannerOutgoing.nextFloat());
        //add ",{amount}" to end of outgoings
    }

    private void removeOutgoing() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current Outgoings");
        viewOutgoings();
        String value = scanner.nextLine();
        properties.setProperty("outgoings",properties.getProperty("outgoings").replaceAll(","+value,""));
        saveProperties();
    }

    public static void main(String[] args) {
        RecurringOutgoings recurringOutgoings = new RecurringOutgoings();
        recurringOutgoings.mainMenu();
    }
}
