import java.io.IOException;

/**
 * Class for a date
 * @author Paul
 */
public class Date {
    //declaring fields
    private int day, month, year;

    /**
     * Initialize the Date and make the user input it
     * @throws IOException 
     */
    public Date() throws IOException {
        selectDay();
        selectMonth();
        selectYear();
    }

    /**
     * Input the day -- not done need to check against weird input (i.e the 40th)
     * @throws IOException 
     */
    private void selectDay() throws IOException{
        System.out.println("Please input a day for your purchase");
        String day = App.userIn.readLine();
        try{
            this.day = Integer.parseInt(day);
        } catch (NumberFormatException e){
            System.out.println("Invalid day, we will set the default as 01, you can change this later");
            this.day = 1;
        }
    }

    /**
     * Input the month -- not done need to check against weird input (i.e. the 13th)
     * @throws IOException 
     */
    private void selectMonth() throws IOException{
        System.out.println("Please input the number of the month.");
        String month = App.userIn.readLine();
        try {
            this.month = Integer.parseInt(month);
        } catch (NumberFormatException e){
            System.out.println("Invalid month, we will set the default as 01, you can change this later");
            this.month = 1;
        }
    }

    /**
     * Input the year -- not done need to check against weird year (i.e. -2000)
     * @throws IOException 
     */
    private void selectYear() throws IOException{
        System.out.println("Please input the number of the year.");
        String year = App.userIn.readLine();
        try {
            this.year = Integer.parseInt(year);
        } catch (NumberFormatException e){
            System.out.println("Invalid year, we will set the default as 2020, you can change this later");
            this.year = 2020;
        }
    }

    /**
     * Get the date in a string form
     * @return String in format dd.mm.yyyy
     */
    public String getDate(){
        return String.valueOf(day) + '.' + (month) + '.' + (year);
    }

}
