import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Tips {
    File inputFile;
    Scanner scanner;

    public String getTipMotivation(String fileName) throws FileNotFoundException {
        String tip = null;
        Random rand = new Random();
        int n = 1;
        File file = new File(System.getProperty("user.dir") + "/" + fileName  + ".txt");
        for(scanner = new Scanner(file); scanner.hasNext(); ++n)
        {
            String line = scanner.nextLine();
            if(rand.nextInt(n) == 0)
                tip = line;
        }
        return tip;
    }
}