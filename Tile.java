import javafx.scene.paint.Color;

//check if blank
//  be able to customize which letter it is
//check in bag
//check value
//check what square it is on
//  check if that square has a modifier
//check if placed
//
public class Tile implements Comparable<Tile> {
    private int row, col, handX, handY;
    private boolean inBag, placedOnBoard, isBlank, currentlyClicked, fakePlaced;
    private int value;
    private String letter;

    public Tile(int placedRow, int placedCol, String letter, int value) {
        this.row = placedRow;
        this.col = placedCol;
        this.inBag = true;
        this.placedOnBoard = false;
        this.value = value;
        this.letter = letter;
        this.currentlyClicked = false;
        this.handX = 0;
        this.handY = 0;
        this.fakePlaced = false;
        // checking if it is the blank or not
        if (this.letter == "?") {
            isBlank = true;
        } else {
            isBlank = false;
        }
    }

    // returns the placement
    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public int getHandX() {
        return this.handX;
    }

    public int getHandY() {
        return this.handY;
    }

    public void setHandX(int newHandX) {
        this.handX = newHandX;
    }

    public void setHandY(int newHandY) {
        this.handY = newHandY;
    }

    public boolean inBag() {
        return inBag;
    }

    public void changingInOutOfBag() {
        inBag = !inBag;
    }

    public void onBoard() {
        placedOnBoard = true;
    }

    public void offBoard() {
        placedOnBoard = false;
    }

    public boolean placedOnBoard() {
        return placedOnBoard;
    }

    public int specialSquare() {
        /*
         * 0 - boring tile
         * 1 - double letter
         * 2 - double word
         * 3 - triple letter
         * 4 - triple word
         */
        int i = this.boardX();
        int j = this.boardY();
        // figuring out if a placed tile is somewhere special
        if (i == 0 && (j == 0 || j == 7 || j == 14) || i == 7 && (j == 0 || j == 14)
                || i == 14 && (j == 0 || j == 7 || j == 14)) {

            return 4;
        } else if (i == j || 14 - j == i) { // hits both diagnals
            if (i == 5 || i == 9) {// triple letters
                return 3;
            } else if (i == 6 || i == 8) {// double letters
                return 1;
            } else if (i== 7){
                return 2;
            } else {// double words
                return 2;
            }
        } else if ((i == 1 || i == 13) && (j == 5 || j == 9) || (j == 1 || j == 13) && (i == 5 || i == 9)) {
            return 3;
        } else if (i == 2 && (j == 6 || j == 8) || i == 12 && (j == 6 || j == 8) || j == 2 && (i == 6 || i == 8)
                || j == 12 && (i == 6 || i == 8)) {
            return 1;
        } else if (j == 7 && (i == 3 || i == 11) || i == 7 && (j == 3 || j == 11)
                || (i == 0 || i == 14) && (j == 3 || j == 11) || (j == 0 || j == 14) && (i == 3 || i == 11)) {// double
                                                                                                              // letters
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {
        String name = "";
        name = name + this.letter + this.value;
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getLetter() {
        return letter;

    }

    public void changingCords(int x, int y, boolean board) {
        if (!board) {
            this.col = x;
            this.row = y;
            this.placedOnBoard = false;
        } else {
            // sets it so the corner is in the corner of the square
            this.col = 50 + 30 * ((x - 50) / 30);
            this.row = 30 + 30 * ((y - 30) / 30);
            this.placedOnBoard = true;
        }
    }

    public void changingX(int x) {
        this.col = x;
    }

    public void changingY(int y) {
        this.row = y;
    }

    public boolean isBlank() {
        return isBlank;
    }

    public boolean currentlyClicked() {
        return currentlyClicked;
    }

    public void clickedOn() {
        currentlyClicked = true;
    }

    public void clickedOff() {
        currentlyClicked = false;
    }

    // getting placement on the board
    public int boardX() {
        return (this.col - 50) / 30;
    }

    public int boardY() {
        return (this.row - 30) / 30;
    }

    public void blankLetterMaking(String letter) {
        if (isBlank) {
            this.letter = letter;
        }
    }

    public void blankReturning() {
        if (isBlank) {
            this.letter = "?";
        }
    }

    @Override
    public int compareTo(Tile o) {
        return this.letter.compareTo(o.letter);
    }

}
