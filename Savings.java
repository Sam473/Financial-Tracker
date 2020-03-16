
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class Savings {

    //takes input from the user
    private static Scanner userInput;

    public static void main(String[] args) {
        userInput = new Scanner(System.in);
        boolean cont = true;
        while(cont){
            System.out.println("Do you have any savings goals?: ");
            //converts string to lowercase so user can input either lower or upper case and no errors will be produced
            String response = userInput.nextLine().toLowerCase();
            if(response.equals("no")){
                //if they user has no savings goals, the code will shut down cleanly
                System.exit(0);
            }
            else if(response.equals("yes")){
                //breaks out of the loop
                cont = false;
            }
            else{
                //if the input is invalid it will repeat the question until a valid input is entered
                System.out.print("Please enter a yes or no response: \n");
            }
        }

        boolean continueLoop = true;
        while(continueLoop){
            System.out.print("Are your goals weekly, monthly, yearly or custom? :\n");
            String timePeriod = userInput.nextLine().toLowerCase();

            //will loop if they dont enter a valid response
            if(timePeriod.equals("weekly") || timePeriod.equals("monthly") || timePeriod.equals("yearly") || timePeriod.equals("custom")){
                savingsGoal(timePeriod);
                continueLoop = false;
            }
            else{
                System.out.println("Please enter the time period: ");
            }
        }
    }

    /**
     * Checks what the users goals are
     *
     * @param timePeriod : weekly/monthly/yearly/custom
     * */
    public static void savingsGoal(String timePeriod){
        boolean cont = true;
        while(cont){
            System.out.print("What are your "+timePeriod+" savings goals £");
            String goalStr = userInput.nextLine();
            try{
                //tests that the number entered is an integer
                int goalInt = Integer.parseInt(goalStr);
                savingsPerDay(timePeriod, goalInt);
                cont = false;
            } catch (NumberFormatException e) {
                System.out.print("Enter in a numerical value with no special characters.\n");
            }
        }
    }



    /**
     * Finds out how much the user needs to save per day/week/month in order to achieve their saving goals
     *
     * @param timePeriod : week/month/year or custom
     * @param goal : how much they wish to save in that time period
     *
     * */
    public static void savingsPerDay(String timePeriod, int goal){
        double savePerDay;
        double savePerWeek;
        double savePerMonth;
        DecimalFormat df = new DecimalFormat("#.##");
        if(timePeriod.equals("weekly")){
            savePerDay = goal/7;
            System.out.print("In order to meet your goal, you need to save £"+df.format(savePerDay)+" per day.");
        }
        else if(timePeriod.equals("monthly")){
            savePerDay = goal/30;
            savePerWeek = goal/4;
            System.out.print("In order to meet your goal, you need to save £"+df.format(savePerDay)+" per day.");
            System.out.print("\nAnd £"+df.format(savePerWeek)+" per week.");
        }
        else if(timePeriod.equals("yearly")){
            savePerDay = goal/365;
            savePerWeek = goal/52;
            savePerMonth = goal/12;
            System.out.print("In order to meet your goal, you need to save £"+df.format(savePerDay)+" per day.");
            System.out.print("\n£"+df.format(savePerWeek)+" per week.");
            System.out.print("\nAnd £"+df.format(savePerMonth)+" per month.");
        }
        else if(timePeriod.equals("custom")){

            /**
             * Custom will only work with the date 25-11-2020 so were gonna use this for the video
             * anything else will restart this function and will continue to do so until this date is entered
             * */

            System.out.print("Please enter the date by which you should have saved up £"+goal+" in a dd-mm-yyyy format: ");
            String endDate = userInput.nextLine();
            dateFormat(endDate, goal);
        }
    }


    /**
     * THE DATE  THAT WE'RE GONNA USE FOR THE VIDEO IS
     * 25/11/2020
     *
     * This checks that the date is numerical and will check that the user has entered
     * 25-11-2020 which is our default date
     *
     * @param date : the date which the user enters in the dd-mm-yyyy format
     * @param goal : the users custom goal
     *
     * */

    private static void dateFormat(String date, int goal){
        String[] dateArray = date.split("-");
        try{
            //checks that the day week and month are numerical
            int dayInt = Integer.parseInt(dateArray[0]);
            int monthInt = Integer.parseInt(dateArray[1]);
            int yearInt = Integer.parseInt(dateArray[2]);
            if (dayInt == 25 && monthInt == 11 && yearInt == 2020){
                compareDate(goal);
            }
        } catch (NumberFormatException e) {
            System.out.print("\nIncorrect format");
            //will call the savingsPerDay method which will then restart the process
            //called when the format is incorrect
            savingsPerDay("custom", goal);
        }

    }


    /**
     * CompareDate takes the current date and finds the difference between the current date and the one entered
     * which is fixed at 25-11-2020 in this case.
     *
     * It will then find out how much the user needs to save per day/week/month etc.
     *
     * @param goal : the goal target to save in the given time frame
     *
     * */
    private static void compareDate(int goal){
        DecimalFormat df = new DecimalFormat("#.##");
        LocalDate currentDate = LocalDate.now();

        //gets the number of days into the year that it is and then subtracts that from 329
        //329 is the number of days into the year that our chosen date is
        int differenceInDays = 329 - currentDate.getDayOfYear();

        int savePerDay = goal/differenceInDays;
        int savePerWeek = (goal/differenceInDays)*7;
        int savePerMonth = (goal/differenceInDays)*30;

        System.out.print("In order to meet your goal, you need to save £"+df.format(savePerDay)+" per day.");
        System.out.print("\n£"+df.format(savePerWeek)+" per week.");
        System.out.print("\nAnd £"+df.format(savePerMonth)+" per month.");


    }
    
}
