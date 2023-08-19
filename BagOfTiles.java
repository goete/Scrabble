import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class BagOfTiles {
    Scanner input = null;
    private int numOfTiles;
    private Tile[] tiles;
    private int xCord;
    private int yCord;

    public BagOfTiles() {
        try {
            input = new Scanner(new File("values.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.xCord = 2000;
        this.yCord = 2000;
        this.numOfTiles = 0;
        this.tiles = new Tile[100];
        while (input.hasNext()) {
            //pulling from file
            String letter = input.next();
            int pointValue = input.nextInt();
            int number = input.nextInt();
            input.nextLine();
            for (int i = 0; i < number; i++) {
                tiles[numOfTiles] = new Tile(xCord, yCord, letter, pointValue);
                numOfTiles++;
            }

        }
    this.shuffle();
    }

    public int remainingTiles() {
        return numOfTiles;
    }

    // if empty
    public boolean isEmpty() {
        if (numOfTiles == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean exchangeValid(){
        if (numOfTiles >= 7){
            return true;
        } else {
            return false;
        }
    }

    // shuffle the bag
    public void shuffle() {
        for (int i = 0; i < this.numOfTiles; i++) {
            // generate random number
            int min = i;
            int max = this.numOfTiles - 1;
            int rand = (int) (Math.random() * (max - min + 1) + min);
            // swap the cards
            Tile t = this.tiles[i];
            this.tiles[i] = this.tiles[rand];
            this.tiles[rand] = t;
        }

    }

    public Tile PickUpTile() {
        numOfTiles--;
        Tile t = this.tiles[this.numOfTiles];
        t.changingInOutOfBag();
        this.tiles[this.numOfTiles] = null;
        return t;
    }

   public void exchangeAllOfThese(Tile[] balls){
    for (int i = 0; i < balls.length; i++) {
        this.tiles[numOfTiles] = balls[i];
        numOfTiles++;
    }
    this.shuffle();
   }
    // toString
    public String toString() {
        String output = "";
        for (int i = 0; i < this.numOfTiles; i++) {
            // grabbing
            Tile bag = this.tiles[i];
            output = output + bag.toString() + "\n";
        }
        return output;
    }

}
