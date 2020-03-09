import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class InputSalary {
    private String[][] fileRequirements = {{"userName","defaultUser"},{"incomes","0"},{"directDebits",""},{"outgoings",""}};
    File file = new File("user_data.properties");
    Properties properties = new Properties();

    public InputSalary() {
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
    private void mainMenu() {
        Scanner scanner = new Scanner(System.in);
            System.out.println("Please select an option by using the character in brackets:\n" +
                    "1. Add new income\n" +
                    "2. View all incomes\n" +
                    "3. View total monthly income\n" +
                    "4. Add new regular payment (Monthly)\n" +
                    "5. Your total outgoings\n" +
                    "6. Your net monthly income\n" +
                    "7. Exit budget planner");
            String input = scanner.nextLine();
            System.out.println(inputChecker(input));
    }

    /**
     * takes input from main menu and calls correct method
     * @param input their choice from main menu options
     * @return Success/failure message
     */
    private String inputChecker(String input) {
        switch (input) {
            case "1":
                addIncome();
                return "Successfully added new income\n";
            case "2":
                viewIncomes();
                return "";
            case "3":
                return "Your total monthly income is:\n£" + totalIncome() + "\n";
            case "4":
                addOutgoing();
                return "Successfully added new outgoing\n";
            case "5":
                return "Your total outgoings:\n£" + totalOutgoings();
            case "6":
                return "Your net income every month will be:\n£" + netIncome();
            case "7":
                return "Thank you for using the budget planner";
            default:
                return "Not an option. Choose again";

        }
    }


    /**
     * will calculate difference between incomes and outgoings
     * @return the difference between their incomes and outgoings
     */
    private float netIncome () {
        return totalIncome() - totalOutgoings();
    }

    /**
     * will sum all incomes
     * @return sum of all income streams
     */
    private float totalIncome() {
        String[] incomes = properties.getProperty("incomes").split(",");
        float totalIncome = 0;
        for (String income : incomes) {
            try {
                totalIncome += Float.parseFloat(income);
            } catch (NumberFormatException e){
                System.out.println("Error: not a float or integer");
                properties.setProperty("incomes",properties.getProperty("incomes").replaceAll(","+income, ""));
                //will remove the faulty value from the incomes list
                saveProperties();
            }
        }
        return totalIncome;
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

    private void viewIncomes() {
        String[] incomes = properties.getProperty("incomes").split(",");
        for (String income : incomes) {
            System.out.println(income);
        }
    }

    /**
     * Takes an input on the frequency of the payment and subsequent inputs to calculate monthly income
     * Will add income stream to the properties file
     *
     */
    private void addIncome() {
        Scanner salaryScanner = new Scanner(System.in);
        float customPay;
        float monthlySalary = 0;
        boolean validInput = false; //used to verify they choose a frequency option
        System.out.println("How frequently do you get paid?\n" +
                "(H)ourly\n" +
                "(D)aily\n" +
                "(W)eekly\n" +
                "(M)onthly\n" +
                "(Y)early");
        String frequency = salaryScanner.nextLine(); //take input from console
        while(!validInput) {
            validInput=true;
            switch (frequency) {
                case "h":
                case "H": //if input is hour
                    System.out.println("How many hours a week?");
                    int hoursPerWeek = salaryScanner.nextInt();
                    System.out.println("How much do you get per hour?");
                    customPay = salaryScanner.nextFloat();
                    monthlySalary = (customPay*hoursPerWeek*52)/12; //work out hours worked per week then convert to monthly
                    break;
                case "d":
                case "D":
                    System.out.println("How much do you get per day?");
                    customPay = salaryScanner.nextFloat();
                    monthlySalary = (customPay*5*52)/12; //5 days per week then same as weekly pay
                    break;
                case "w":
                case "W":
                    System.out.println("How much do you get per week?");
                    customPay = salaryScanner.nextFloat();
                    monthlySalary = (customPay*52)/12; //52 weeks in a year, but / by 12 for per month salary
                    break;
                case "m":
                case "M":
                    System.out.println("How much do you get per month?");
                    monthlySalary = salaryScanner.nextFloat(); //leave it as it is
                    break;
                case "y":
                case "Y":
                    System.out.println("How much do you get per year?");
                    customPay = salaryScanner.nextFloat();
                    monthlySalary = customPay/12;
                    break;
                default:
                    validInput = false;
                    System.out.println("Not a valid Input, try again:");
                    frequency = salaryScanner.nextLine();
            }
        }

        properties.setProperty("incomes", properties.getProperty("incomes") + "," + monthlySalary);
        //add ",{amount}" to end of incomes
        saveProperties();
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

    public static void main(String[] args) {
         InputSalary inputSalary = new InputSalary();
         inputSalary.mainMenu();
    }
}