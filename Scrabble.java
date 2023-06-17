public class Scrabble {
    // Instance variables
    private Board board;
    private BagOfTiles bag;
    private Player user1;
    private Player user2;
    private Dict dict;
    private boolean hasNotCounted;
    private int add;
    private int subtract;

    public Scrabble() {
        // make all the fun stuff and set it all up
        
        bag = new BagOfTiles();
        dict = new Dict();
        user1 = new Player(bag, dict);
        user2 = new Player(bag, dict);
        board = new Board(15, 15, user1, user2);
        bag.shuffle();
        user1.switchTurn();
        hasNotCounted = true;

        //DONT DELETE
        while (user1.TilesInHand() < 7) {
            user1.addTile();
        }
        while (user2.TilesInHand() < 7) {
            user2.addTile();
        }
        
    }

    

    public void playGame() {
      //giving the losers left over points to the winner
      while(hasNotCounted){
        if(bag.isEmpty()){
            if(user1.TilesInHand() == 0){
                for (int i = 0; i < user2.TilesInHand(); i++) {
                    add += user2.getHand()[i].getValue();
                }
                subtract = add*-1;
                user1.addToScore(add);
                user2.addToScore(subtract);
            } else if(user2.TilesInHand() == 0){
                for (int i = 0; i < user1.TilesInHand(); i++) {
                    add += user1.getHand()[i].getValue();
                }
                subtract = add*-1;
                user2.addToScore(add);
                user1.addToScore(subtract);
            }
        }
      }

      board.gameOver();
}
}
