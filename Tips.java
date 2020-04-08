import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * Tips
 * @author Luke
 */
public class Tips {
    File inputFile;
    Scanner scanner;

    /**
     * Get a random tip or motivational quote
     *
     * @param fileName name of file as it appears in viewer (without .txt)
     * @return Contains the quote/tip
     * @throws FileNotFoundException .
     */
    public String getTipMotivation(String fileName) throws FileNotFoundException {
        String tip = null;
        Random rand = new Random();
        int n = 0;
        // Open the file, passed from other method
        File file = new File(System.getProperty("user.dir") + "/" + fileName  + ".txt");
        for(scanner = new Scanner(file); scanner.hasNext(); ) //loop through lines in file
        {
            ++n;
            String line = scanner.nextLine();
            if(rand.nextInt(n) == 0)
                tip = line;
        }
        return tip;
    }
}