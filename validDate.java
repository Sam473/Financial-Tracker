import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

/**
 * Class which creates a Date (java.util) object
 * Note: the date object will have the format:
 * day of week month date of day hours:min:seconds GMT yyyy
 * @author Nick
 */
public class validDate{
    private Date date;

    /**
     * Let the user create a date object in the right format (don't check yet for future or past
     * and return the valid date.
     */
    public validDate(){
        System.out.println("Enter a date in 'dd/MM/yyyy' format:");
        assignDate();
    }

    /**
     * Let the user input until a valid date is given.
     */
    private void assignDate(){
        while(true){
            String userInput = enterDate();
            if(validFormat(userInput)){
                System.out.println("Date added successfully");
                return;
            }
            else{
                System.out.println("Either the format is wrong, or the date does not exist, please try again.");
            }
        }
    }

    /**
     * Method will return the string in the format "dd.mm.yyyy" making use
     * of the format specified at the beginning of the class
     * @return String in the right format for further processing
     */
    public String getDate(){
        String[] elements = date.toString().split(" ");
        return elements[2] + "." + processMonth(elements[1]) + "." + elements[5];
    }

    /**
     * Method which gets passed a month as a String and returns the number of the month

     * @param month String for month EX: Jan for January
     * @return String representing number ex "01" for January
     */
    private String processMonth(String month){
        switch (month){
            case "Jan": return "01";
            case "Feb": return "02";
            case "Mar": return "03";
            case "Apr": return "04";
            case "May": return "05";
            case "Jun": return "06";
            case "Jul": return "07";
            case "Aug": return "08";
            case "Sep": return "09";
            case "Oct": return "10";
            case "Nov": return "11";
            case "Dec": return "12";
            default:
                return "";
        }
    }

    /**
     * Accept input from the user
     * @return String inputted by the user
     */
    private String enterDate(){
        Scanner myObj = new Scanner(System.in);
        return myObj.nextLine();
    }

    /**
     * Method checks if the input is in the right format (dd/MM/yyyy)
     * @param input String the user inputted
     * @return true if the format is correct
     *          false otherwise
     */
    private boolean validFormat(String input){
        // set the format
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try {
            date = format.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Method checks if the date object created in this class is after or before
     * the current date of today.
     * Maybe add another similar method to compare this date (the one created in the method)
     * with a different one passed as an argument.
     * We'll see later
     * @return true if date is in the future
     *          false if it is in the past
     */
    private boolean futureDate(){
        Date currentDate = new Date();
        return date.after(currentDate);
    }

    /**
     * Method accepts input, creates a date object and makes it valid only if it is in the future
     * Good for the future.
     * @return Date object when valid
     */
    private Date checkDate(){
        System.out.println("Enter a date in 'dd/MM/yyyy' format:");
        while(true){
            String userInput = enterDate();
            if (validFormat(userInput)){
                if (futureDate()){
                    return date;
                }else{
                    System.out.println("Date entered is in the past.");
                }
            }else{
                System.out.println("Date does not follow 'dd/MM/yyyy' format.");
            }
        }
    }

    /**
     * Main function for testing only -- created a constructor instead to incorporate the class
     * with the others
     * @param args cmd arguments
     */
    public static void main(String[] args) {
        validDate valid = new validDate();
        //valid.checkDate();
    }
}
