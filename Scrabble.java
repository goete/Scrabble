public class Scrabble {
    // Instance variables
    private Board board;
    private BagOfTiles bag;
    private Player user1;
    private Player user2;
    private Dict dict;

    public Scrabble() {
        // make all the fun stuff and set it all up
        
        bag = new BagOfTiles();
        dict = new Dict();
        user1 = new Player(bag, dict);
        user2 = new Player(bag, dict);
        board = new Board(15, 15, user1, user2);
        bag.shuffle();
        user1.switchTurn();

        //DONT DELETE
        while (user1.TilesInHand() < 7) {
            user1.addTile();
        }
        while (user2.TilesInHand() < 7) {
            user2.addTile();
        }
        
    }

    

    public void playGame() {
      

}
}
