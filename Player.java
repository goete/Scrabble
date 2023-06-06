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
    private boolean isTurn, wordValid, bingoHasBeenCounted;
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
        this.bingoHasBeenCounted = false;
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
            // hand[i].falseFakePlaced();
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

    private boolean touchingAndSameRow(Tile[][] board) {
        if (boardEmpty(board)) {
            boolean hold = false;
            for (int i = 0; i < hand.length; i++) {
                if (hand[i].boardX() == 7 && hand[i].boardY() == 7) {
                    hold = true;
                }
            }
            if (!hold) {
                System.out.println("First play must be on the middle star");
                return false;
            }
        }
        for (int i = 0; i < hand.length; i++) {
            for (int j = 0; j < hand.length; j++) {
                if (hand[i].placedOnBoard() && hand[j].placedOnBoard()) { // both are on board
                    if (hand[i].boardX() == hand[j].boardX() || hand[i].boardY() == hand[j].boardY()) {
                        // this is good, leave blank
                    } else {
                        return false;
                    }
                }
            }
        }

        if (!touchingPreviousPlays(board)) {
            return false;
        }

        return true;
    }

    private boolean touchingPreviousPlays(Tile[][] board) {
        if (boardEmpty(board)) {
            return true;
        } else {
            for (int i = 0; i < hand.length; i++) {
                if (hand[i].placedOnBoard()) {
                    if (hand[i].boardX() - 1 >= 0) {
                        if (board[hand[i].boardX() - 1][hand[i].boardY()] != null) {
                            return true;
                        }
                    }
                    if (hand[i].boardX() + 1 <= 14) {
                        if (board[hand[i].boardX() + 1][hand[i].boardY()] != null) {
                            return true;
                        }
                    }
                    if (hand[i].boardY() - 1 >= 0) {
                        if (board[hand[i].boardX()][hand[i].boardY() - 1] != null) {
                            return true;
                        }
                    }
                    if (hand[i].boardY() + 1 <= 14) {
                        if (board[hand[i].boardX()][hand[i].boardY() + 1] != null) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    private boolean boardEmpty(Tile[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null) { // there's something there
                    return false;
                }
            }
        }
        return true;
    }

    public boolean wordValid(Tile[][] board) {
        // check that each letter is forming the same word
        if (touchingAndSameRow(board)) {
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
            // checks for overlaying tiles
            int count = 0;
            for (int i = 0; i < numOfTilesInHand; i++) {
                if (hand[i].placedOnBoard()) {
                    store[count] = hand[i];
                    count++;
                    if (hold[hand[i].boardX()][hand[i].boardY()] == 0) {// placement spot is open
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
                        while (j + z < hold.length && hold[i][j + z] != 0) {
                            collect[z] = hold[i][j + z];
                            z++;
                        }
                        int p = 1;
                        while (j - p >= 0 && hold[i][j - p] != 0) {
                            for (int k = 1; k < z; k++) {
                                collect[z + k] = collect[z + k - 1];
                            }
                            collect[0] = hold[i][j - p];
                            p++;

                        }
                        if (z > 1 || boardEmpty(board)) {
                            String word = "";
                            for (int k = 0; k < z; k++) {
                                word = word + collect[k];
                            }
                            if (this.dict.validWord(word) == false) { // the word isnt a word
                                return false;
                            }

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
                        collect[z] = hold[j][i];
                        z++;
                        while (j + z < hold.length && hold[j + z][i] != 0) {
                            collect[z] = hold[j + z][i];
                            z++;
                        }
                        int p = 1;
                        while (j - p >= 0 && hold[j - p][i] != 0) {
                            for (int k = 1; k < z; k++) {
                                collect[z + k] = collect[z + k - 1];
                            }
                            collect[0] = hold[j - p][i];
                            p++;

                        }
                        if (z > 1 || boardEmpty(board)) {
                            String word = "";
                            for (int k = 0; k < z; k++) {
                                word = word + collect[k];
                            }
                            if (this.dict.validWord(word) == false) { // the word isnt a word
                                System.out.println("Down fail");
                                return false;
                            }
                        }
                        // go to the end of the word since it has been checked
                        j = j + z - 1;
                    }
                }
            }
            // everything is a word
            scoringSystem(board);
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j] != null) {
                        board[i][j].onBoard();
                    }
                }
            }
        } else {
            // wasn't touching or wasn't all in a line
            System.out.println("touchingAndSameRow");
            return false;
        }
        return true;

    }

    private Tile[][] helpMe(Tile[][] board) {
        Tile[] hold = placedTiles();
        for (int i = 0; i < hold.length; i++) {
            board[hold[i].boardX()][hold[i].boardY()] = hold[i];
        }
        return board;
    }
    private int u = 0; //the dylan fix (don't use u again or it might mess up everything)
    private void helpingSingleTileDown(Tile[][] board, Tile[] hold){
        Tile[] collect = new Tile[15];
        int z = 0;
        int i = hold[0].boardY();
        int j = hold[0].boardX();
        while (j + z < 15 && board[j + z][i] != null) {
            collect[z] = board[j + z][i];
            z++;
        }
        int p = 1;
        while (j - p >= 0 && board[j - p][i] != null) {
            collect[z] = board[j - p][i];
            p++;
            z++;
    }
    System.out.println("I AM VERTICAL");
    scoringASingleWordThatHasBeenFound(collect, hold);
    
    }

    private void helpingSingleTileAccross(Tile[][] board, Tile[] hold){
        Tile[] collect = new Tile[15];
        int z = 0;
        int i = hold[0].boardY();
        int j = hold[0].boardX();
        while (i + z < 15 && board[j][i + z] != null) {
            collect[z] = board[j ][i + z];
            z++;
        }
        int p = 1;
        while (i - p >= 0 && board[j][i- p] != null) {
            collect[z] = board[j][i - p];
            p++;
            z++;
    }
    System.out.println("I AM SIDEWAYS");
    
    scoringASingleWordThatHasBeenFound(collect, hold);
    }

    private void helpAccrossOnVerticalWord(Tile[][]board, Tile[] hold, int which){
        Tile[] collect = new Tile[15];
        int z = 0;
        int i = hold[which].boardY();
        int j = hold[which].boardX();
        while (i + z < 15 && board[j][i + z] != null) {
            collect[z] = board[j ][i + z];
            z++;
        }
        int p = 1;
        while (i - p >= 0 && board[j][i- p] != null) {
            collect[z] = board[j][i - p];
            p++;
            z++;
    }
    System.out.println("I AM SIDEWAYS");
    
    scoringASingleWordThatHasBeenFound(collect, hold);
    }

    private void helpVerticalOnHorizontalWord(Tile[][]board, Tile[] hold, int which){
        Tile[] collect = new Tile[15];
        int z = 0;
        int i = hold[which].boardY();
        int j = hold[which].boardX();
        while (j + z < 15 && board[j + z][i] != null) {
            collect[z] = board[j + z][i];
            z++;
        }
        int p = 1;
        while (j - p >= 0 && board[j - p][i] != null) {
            collect[z] = board[j - p][i];
            p++;
            z++;
    }
    System.out.println("I AM VERTICAL");
    scoringASingleWordThatHasBeenFound(collect, hold);
    }

    private void scoringASingleWordThatHasBeenFound(Tile[] collect, Tile[] hold){
        
    u = 0;
    int holdScore = 0;
    boolean doubleWord = false;
    boolean tripleWord = false;
    boolean doubleWord2 = false;
    boolean tripleWord2 = false;
    boolean doubleWord3 = false;
    boolean tripleWord3 = false;
    while(collect[u] != null && collect[1] != null){
        if(Arrays.stream(hold).anyMatch(x -> x.equals(collect[u]))){
            if(collect[u].specialSquare() == 0){
                holdScore += collect[u].getValue();
        }else if(collect[u].specialSquare() == 1){//double letter
            holdScore += collect[u].getValue() * 2;
        } else if(collect[u].specialSquare() == 3){//triple letter
            holdScore += collect[u].getValue() * 3;
        } else if(collect[u].specialSquare() == 2){//double word
            holdScore += collect[u].getValue();
            if(!doubleWord){
                doubleWord = true;
            }else if(!doubleWord2){
                doubleWord2 = true;
            }else if(!doubleWord3){
                doubleWord3 = true;
            }
        }else if(collect[u].specialSquare() == 4){//triple word
            holdScore += collect[u].getValue();
            if(!tripleWord){
                tripleWord = true;
            }else if(!tripleWord2){
                tripleWord2 = true;
            }else if(!tripleWord3){
                tripleWord3 = true;
            }
        }
    }else{
        holdScore += collect[u].getValue();
    }
    u += 1;
    }
    if(onBoardNumber() == 7 && !this.bingoHasBeenCounted){
        this.score += 50;
        System.out.println("BINGO");
        this.bingoHasBeenCounted = true;
    }
    if(doubleWord){
        holdScore = holdScore*2;
    }
    if(doubleWord2){
        holdScore = holdScore*2;
    }
    if(doubleWord3){
        holdScore = holdScore*2;
    }
    if(tripleWord){
        holdScore = holdScore*3;
    }
    if(tripleWord2){
        holdScore = holdScore*3;
    }
    if(tripleWord3){
        holdScore = holdScore*3;
    }      
    this.score += holdScore;
    System.out.println(holdScore);
    }

    private void scoringSystem(Tile[][] board3) {
        Tile[][] board = helpMe(board3);
        Tile[] hold = placedTiles();
        int fun = whichWayIsWord();
        if (fun == 18) {
            helpingSingleTileDown(board, hold);
            helpingSingleTileAccross(board, hold);
    } else if(fun == 1){
            helpingSingleTileDown(board, hold);
            for (int i = 0; i < hold.length; i++) {
                helpAccrossOnVerticalWord(board, hold, i);
            }
    } else if(fun == 0){
        helpingSingleTileAccross(board, hold);
        for (int i = 0; i < hold.length; i++) {
            helpVerticalOnHorizontalWord(board, hold, i);
        }
    }
    this.bingoHasBeenCounted = false;
}

    private int whichWayIsWord() {
        // let 0 rep vertical word
        // let 1 rep horz. word
        // let 18 rep a single letter was played (single and ready to mingle)
        Tile[] hold = placedTiles();
        if (hold.length == 1) {
            return 18;
        } else if (hold[0].boardX() == hold[1].boardX()) {
            return 0;
        } else {
            return 1;
        }

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
            // hand[i].falseFakePlaced();

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
