import javafx.scene.paint.Color;

//check if blank
//  be able to customize which letter it is
//check in bag
//check value
//check what square it is on
//  check if that square has a modifier
//check if placed
//
public class Tile implements Comparable<Tile>{
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

    public int getHandX(){
        return this.handX;
    }

    public int getHandY(){
        return this.handY;
    }

    public void setHandX(int newHandX){
        this.handX = newHandX;
    }

    public void setHandY(int newHandY){
        this.handY = newHandY;
    }


    public boolean inBag() {
        return inBag;
    }

    public void changingInOutOfBag(){
        inBag = !inBag;
    }

    public void onBoard(){
        placedOnBoard = true;
    }

    public void offBoard(){
        placedOnBoard = false;
    }

    public boolean placedOnBoard() {
        return placedOnBoard;
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

    public void changingCords(int x, int y, boolean board){
        if(!board){
        this.col = x;
        this.row = y;
        this.placedOnBoard = false;
    }else{
        //sets it so the corner is in the corner of the square
        this.col = 50 + 30*((x-50)/30);
        this.row = 30 + 30*((y-30)/30);
        this.placedOnBoard = true;
    }
    }

    public void changingX(int x){
        this.col = x;
    }

    public void changingY(int y){
        this.row = y;
    }

    public boolean isBlank() {
        return isBlank;
    }

    public boolean currentlyClicked(){
        return currentlyClicked;
    }

    public void clickedOn(){
        currentlyClicked = true;
    }

    public void clickedOff(){
        currentlyClicked = false;
    }
    
    //getting placement on the board
    public int boardX(){
        return (this.col-50)/30;
    }

    public int boardY(){
        return (this.row-30)/30;
    }

    @Override
    public int compareTo(Tile o) {
        return this.letter.compareTo(o.letter);
    }

}
