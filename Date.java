import java.util.Scanner;

/**
 * Class for a date
 * @author Paul
 */
public class Date {
    //declaring fields
    private static int day, month, year;
    Scanner userIn;

    /**
     * Initialize the Date and make the user input it
     */
    public Date() {
        userIn = new Scanner(System.in);
        selectDay();
        selectMonth();
        selectYear();
    }

    /**
     * Input the day -- not done need to check against weird input (i.e the 40th)
     */
    private void selectDay(){
        System.out.println("Please input a day for your purchase");
        String day = userIn.nextLine();
        try{
            this.day = Integer.parseInt(day);
        } catch (NumberFormatException e){
            System.out.println("Invalid day, we will set the default as 01, you can change this later");
            this.day = 1;
        }
    }

    /**
     * Input the month -- not done need to check against weird input (i.e. the 13th)
     */
    private void selectMonth(){
        System.out.println("Please input the number of the month.");
        String month = userIn.nextLine();
        try {
            this.month = Integer.parseInt(month);
        } catch (NumberFormatException e){
            System.out.println("Invalid month, we will set the default as 01, you can change this later");
            this.month = 1;
        }
    }

    /**
     * Input the year -- not done need to check against weird year (i.e. -2000)
     */
    private void selectYear(){
        System.out.println("Please input the number of the year.");
        String year = userIn.nextLine();
        try {
            this.year = Integer.parseInt(year);
        } catch (NumberFormatException e){
            System.out.println("Invalid year, we will set the default as 2020, you can change this later");
            this.year = 2020;
        }
    }

    public static String addMonths(String originalDate, int monthsToAdd){
        String currentDay = originalDate.substring(0,2);
        int currentMonth = Integer.parseInt(originalDate.substring(3,5));
        int currentYear = Integer.parseInt(originalDate.substring(6));
        int newMonth = currentMonth + monthsToAdd;
        while (newMonth > 12) {
            newMonth -= 12;
            currentYear++;
        }
        return currentDay + "/" + newMonth + "/" + currentYear;

    }

    /**
     * Get the date in a string form
     * @return String in format dd.mm.yyyy
     */
    public static String getTodayDate(){
        return String.valueOf(day) + '/' + (month) + '/' + (year);
    }

}
