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
        System.out.println(w);
        for (int j = 0; j < dictionary.length; j++) {
        if (dictionary[j].equals(w)) { 
            return true;
        } 
    }
            return false;
    }

     /* public static void main(String[] args) {
        Dict d = new Dict();
        String test = "GIG";
        System.out.println(d.validWord(test)); 
    }  */

}
