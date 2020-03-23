import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;

public class validDate{
    private Date date;

    private String enterDate(){
        Scanner myObj = new Scanner(System.in);
        return myObj.nextLine();
    }

    private boolean validFormat(String input){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try {
            date = format.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean futureDate(){
        Date currentDate = new Date();
        return date.after(currentDate);
    }

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

    public static void main(String[] args) {
        validDate valid = new validDate();
        valid.checkDate();
    }
}
