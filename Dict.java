import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dict {
    String[] dictionary;
    int size;
    Scanner input = null;


    public Dict() {
        dictionary = new String[83693];
        this.size = 0;
        try {
            input = new Scanner(new File("words.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(input.hasNext()){
            dictionary[size] = input.next();
            size++;
            input.nextLine();
        }

    }

    public boolean validWord(String w) {
        w = w.toUpperCase();
        for (int j = 0; j < dictionary.length; j++) {
        if (w.equals(dictionary[j])) { 
            return true;
        } 
    }
         return false;
    }

      

}
