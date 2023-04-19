import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Player {
    private Tile[] hand;
    private int numOfTilesInHand;
    private boolean isTurn, wordValid;
    private static Color TILE_COLOR;
    private static Color BLACK;
    private int returnToHandY;
    private int returnToHandX;
    private int score;
    private BagOfTiles bag;
    private Dict dict;

    // constructor
    public Player(BagOfTiles bag, Dict dict) {
        this.dict = dict;
        this.bag = bag;
        this.score = 0;
        this.hand = new Tile[7];
        this.numOfTilesInHand = 0;
        this.returnToHandY = 525;
        this.isTurn = false;
        TILE_COLOR = new Color(227, 207, 170);
        BLACK = Color.BLACK;
        // filling out hand
        for (int i = 0; i < 7; i++) {
            hand[i] = this.bag.PickUpTile();
        }
        this.alphabetizeAndPutInHand();

    }

    public void alphabetizeAndPutInHand() {
        Arrays.sort(hand);
        for (int i = 0; i < numOfTilesInHand; i++) {
            hand[i].setHandX(150 + 30 * i + 5 * i - 15);
            hand[i].setHandY(this.returnToHandY);
            hand[i].changingCords(hand[i].getHandY(), hand[i].getHandX(), false);
            returnToHandX = hand[i].getCol();
            hand[i].falseFakePlaced();
        }
    }

    public int TilesInHand() {
        return this.numOfTilesInHand;
    }

    public void addTile() {
        // Tile in hand
        this.hand[this.numOfTilesInHand] = this.bag.PickUpTile();
        // count it as a tile
        this.numOfTilesInHand++;
        this.alphabetizeAndPutInHand();
    }

    public int onBoardNumber() {
        int num = 0;
        for (int i = 0; i < this.numOfTilesInHand; i++) {
            if (hand[i].placedOnBoard()) {
                num++;
            }
        }
        return num;
    }

    public boolean wordValid(Tile[][] board) {
        // make another array for storage
        Tile[] store = new Tile[onBoardNumber()];
        // remake the board array as a char
        char[][] hold = new char[15][15];
        // set up the checking board
        for (int i = 0; i < hold.length; i++) {
            for (int j = 0; j < hold[i].length; j++) {
                if (board[i][j] != null) {
                    hold[i][j] = board[i][j].getLetter().charAt(0);
                }

            }
        }
        int count = 0;
        for (int i = 0; i < numOfTilesInHand; i++) {
            if (hand[i].placedOnBoard()) {
                store[count] = hand[i];
                count++;
                if (hold[hand[i].boardX()][hand[i].boardY()] == 0) {//no tile at placement
                    hold[hand[i].boardX()][hand[i].boardY()] = hand[i].getLetter().charAt(0);
                } else { // tile is over another tile, can't do that
                    System.out.println("A tile is over another tile, can't do that");
                    return false;
                }
            }
        }
        // ->
        for (int i = 0; i < hold.length; i++) {
            for (int j = 0; j < hold[i].length; j++) {
                char[] collect = new char[15];
                int z = 0;
                if (hold[i][j] != 0) {
                    collect[z] = hold[i][j];
                    z++;
                    while (hold[i][j + z] != 0 && i + z < hold.length) {
                        collect[z] = hold[i][j + z];
                        z++;
                    }
                    
                    String word = new String(collect);
                    if (this.dict.validWord(word) == false) { // the word isnt a word
                        System.out.println(dict.validWord(word));
                        return false;
                    }
                    // go to the end of the word since it has been checked
                    j = j + z - 1;
                }

            }
        }
        // down \/
        for (int i = 0; i < hold.length; i++) {
            for (int j = 0; j < hold[i].length; j++) {
                char[] collect = new char[15];
                int z = 0;
                if (hold[j][i] != 0) {
                    collect[z] = hold[i][j];
                    z++;
                    while (hold[j+z][i] != 0 && j + z < hold.length) {
                        collect[z] = hold[j + z][i];
                        z++;
                    }
                    String word = new String(collect);
                    if (this.dict.validWord(word) == false) { // the word isnt a word

                        System.out.println(dict.validWord(word));
                        return false;
                    }
                    // go to the end of the word since it has been checked
                    i = i + z - 1;
                }

            }
        }
        // everything is a word

        return true;

    }

    public Tile[] placedTiles() {
        int hold = 0;
        Tile[] store = new Tile[this.onBoardNumber()];
        for (int i = 0; i < this.numOfTilesInHand; i++) {
            if (this.hand[i].placedOnBoard()) {
                store[hold] = hand[i];
                hold++;
            }
        }
        return store;
    }

    public void replaceHand() {
        for (int i = 0; i < this.numOfTilesInHand; i++) {
            if (this.hand[i].placedOnBoard()) {
                this.hand[i] = this.bag.PickUpTile();
            }
        }
        this.alphabetizeAndPutInHand();
    }

    public boolean isTurn() {
        return this.isTurn;
    }

    public void switchTurn() {
        isTurn = !isTurn;
    }

    public void printHand() {
        for (int i = 0; i < this.numOfTilesInHand; i++) {
            Tile t = this.hand[i];
            String tPrint = t.toString();
            System.out.println(tPrint);
        }

    }

    public int addToScore(int s) {
        this.score = this.score + s;
        return this.score;
    }

    public int getScore() {
        return score;
    }

    public void shuffleHand() {
        for (int i = 0; i < this.numOfTilesInHand; i++) {
            // generate random number
            int min = i;
            int max = this.numOfTilesInHand - 1;
            int rand = (int) (Math.random() * (max - min + 1) + min);
            // swap the cards
            Tile c = this.hand[i];
            this.hand[i] = this.hand[rand];
            this.hand[rand] = c;
        }
        for (int i = 0; i < this.numOfTilesInHand; i++) {
            hand[i].setHandX(150 + 30 * i + 5 * i - 15);
            hand[i].setHandY(this.returnToHandY);
            hand[i].changingCords(hand[i].getHandY(), hand[i].getHandX(), false);
            hand[i].falseFakePlaced();

        }
    }

    public Tile[] getHand() {
        return hand;
    }

    public void draw(Graphics graphics) {
        for (int i = 0; i < numOfTilesInHand; i++) {
            Tile t = hand[i];
            graphics.setFont(new Font("MonoLisa", Font.PLAIN, 27));
            int adjust = 0;
            // setting the sideways adjust manually so each letter is centralized (i hate
            // this)
            if (t.getLetter().equals("W")) {
                adjust = 2;
            } else if (t.getLetter().equals("M")) {
                adjust = 3;
            } else if (t.getLetter().equals("D") || t.getLetter().equals("G")) {
                adjust = 4;
            } else if (t.getLetter().equals("Q") || t.getLetter().equals("E") || t.getLetter().equals("R")
                    || t.getLetter().equals("O")
                    || t.getLetter().equals("C")) {
                adjust = 5;
            } else if (t.getLetter().equals("U") || t.getLetter().equals("H") || t.getLetter().equals("K")
                    || t.getLetter().equals("V")
                    || t.getLetter().equals("B") || t.getLetter().equals("N")) {
                adjust = 6;
            } else if (t.getLetter().equals("Y") || t.getLetter().equals("X") || t.getLetter().equals("?")
                    || t.getLetter().equals("Z")
                    || t.getLetter().equals("P") || t.getLetter().equals("A") || t.getLetter().equals("S")
                    || t.getLetter().equals("F")
                    || t.getLetter().equals("L")) {
                adjust = 7;
            } else if (t.getLetter().equals("T")) {
                adjust = 8;
            } else if (t.getLetter().equals("J")) {
                adjust = 9;
            } else if (t.getLetter().equals("I")) {
                adjust = 12;
            }
            graphics.setColor(BLACK);

            graphics.drawRect(t.getRow(), t.getCol(), 30, 30);
            graphics.setColor(TILE_COLOR);
            graphics.fillRect(t.getRow(), t.getCol(), 30, 30);
            graphics.setColor(BLACK);
            graphics.drawString(t.getLetter(), t.getRow() + adjust, t.getCol() + 24);
            graphics.setFont(new Font("MonoLisa", Font.PLAIN, 10));
            if (!t.getLetter().equals("?")) {
                graphics.drawString(Integer.toString(t.getValue()), t.getRow(), t.getCol() + 29);
            }

        }
    }
}
