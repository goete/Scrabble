import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

// 
// Decompiled by Procyon v0.5.36
// 

public class Board extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {
    public static final int X_DIM = 30;
    public static final int Y_DIM = 30;
    public static final int X_OFFSET = 30;
    public static final int Y_OFFSET = 50;
    public static final int MESSAGE_HEIGHT = 80;
    public static final int FONT_SIZE = 14;
    private static final int NUM_LINES = 100;
    public static final Color GRID_COLOR_A;
    public static final Color GRID_COLOR_B;
    public static final Color GRID_COLOR_C;
    public static final Color GRID_COLOR_D;
    public static final Color GRID_COLOR_E;
    public static final Color TILE_COLOR;
    public static final Color LIGHTBLUE;
    public static final Color YELLOW;
    public static final Color BLUE;
    public static final Color CYAN;
    public static final Color GREEN;
    public static final Color BLACK;
    public static final Color WHITE;
    public static final Color RED;
    public static final Color ORANGE;
    private Color[][] grid;
    private Coordinate lastClick;
    private String message;
    private int numLines;
    private int[][] line;
    private int columns;
    private int rows;
    private Player user1;
    private Player user2;
    private Tile[][] onBoard;
    private int numOnBoard;
    private boolean waitingForLetter;
    private int VALUE;

    public Board(final int rows, final int columns, Player user1, Player user2) {
        JFrame frame = new JFrame();
        this.onBoard = new Tile[15][15];
        this.message = "";
        this.user1 = user1;
        this.user2 = user2;
        this.waitingForLetter = false;
        this.numLines = 0;
        this.line = new int[4][100];
        this.VALUE = 30;
        this.columns = columns;
        this.setPreferredSize(new Dimension(60 + 30 * columns, 100 + 30 * (this.rows = rows) + 80));
        frame.setTitle("Board game");
        frame.setResizable(false);
        this.grid = new Color[columns][rows];
        for (int i = 0; i < columns; ++i) {
            for (int j = 0; j < rows; ++j) {
                this.grid[i][j] = this.invisible(i, j);
            }
        }
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        frame.addKeyListener(this);
        frame.setDefaultCloseOperation(3);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);

    }

    private Player currentPlayer() {
        if (user1.isTurn()) {
            return user1;
        } else {
            return user2;
        }
    }

    private void paintText(final Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(30, 50, 30 * this.grid.length, 30 * this.grid[0].length);
        graphics.setFont(new Font(graphics.getFont().getFontName(), 3, 14));
        graphics.clearRect(30, 50 + 30 * this.grid[0].length + 1, 30 + 30 * this.grid.length, 80);
        graphics.drawString(this.message, 30, 100 + 30 * this.grid[0].length);
    }

    private void paintGrid(final Graphics graphics) {
        // i -> vert
        // j -> hor
        for (int i = 0; i < this.grid.length; ++i) {
            for (int j = 0; j < this.grid[i].length; ++j) {
                // making it look like scrabble board
                if (i == 0 && (j == 0 || j == 7 || j == 14) || i == 7 && (j == 0 || j == 14)
                        || i == 14 && (j == 0 || j == 7 || j == 14)) {
                    if (i == j) {// star in the middle
                        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 27));
                        graphics.drawString("☆", 242, 285);

                    }
                    graphics.setColor(Board.GRID_COLOR_B);
                } else if (i == j || 14 - j == i) { // hits both diagnals
                    if (i == 5 || i == 9) {// triple letters
                        graphics.setColor(Board.GRID_COLOR_D);
                    } else if (i == 6 || i == 8) {// double letters
                        graphics.setColor(Board.GRID_COLOR_E);
                    } else {// double words
                        graphics.setColor(Board.GRID_COLOR_C);
                    }
                } else if ((i == 1 || i == 13) && (j == 5 || j == 9) || (j == 1 || j == 13) && (i == 5 || i == 9)) {
                    graphics.setColor(Board.GRID_COLOR_D);
                } else if (i == 2 && (j == 6 || j == 8) || i == 12 && (j == 6 || j == 8) || j == 2 && (i == 6 || i == 8)
                        || j == 12 && (i == 6 || i == 8)) {
                    graphics.setColor(Board.GRID_COLOR_E);
                } else if (j == 7 && (i == 3 || i == 11) || i == 7 && (j == 3 || j == 11)
                        || (i == 0 || i == 14) && (j == 3 || j == 11) || (j == 0 || j == 14) && (i == 3 || i == 11)) {// double
                                                                                                                      // letters
                    graphics.setColor(Board.GRID_COLOR_E);
                } else {
                    graphics.setColor(Board.GRID_COLOR_A);
                }

                graphics.fillRect(30 + 30 * i, 50 + 30 * j, 30, 30);
                graphics.setColor(BLACK);
                graphics.drawRect(30 + 30 * i, 50 + 30 * j, 30, 30);
                final Color color = this.grid[i][j];
                if (color != this.invisible(i, j)) {
                    graphics.setColor(color);
                    graphics.fillOval(30 + 30 * i + 7, 50 + 30 * j + 7, 15, 15);
                }
            }
        }

    }

    private void paintRecallButton(final Graphics graphics) {
        graphics.setColor(WHITE);
        graphics.fillOval(393, 530, 20, 20);
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 25));
        graphics.setColor(BLACK);
        graphics.drawString("↓", 396, 550);
    }

    private void paintShuffleHandButton(final Graphics graphics) {
        graphics.setColor(WHITE);
        graphics.fillOval(93, 530, 20, 20);
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        graphics.setColor(BLACK);
        graphics.drawString("⇄", 95, 547);
    }

    private void paintExchangeButton(final Graphics graphics) {
        graphics.setColor(BLACK);
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        graphics.drawString("EX", 450, 547);
    }

    private void paintSkipTurnButton(final Graphics graphics) {
        graphics.setColor(WHITE);
        graphics.fillOval(33, 530, 20, 20);
        graphics.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        graphics.setColor(BLACK);
        graphics.drawString("S", 35, 547);
    }

    private void paintTilesOnBoard(final Graphics graphics) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (onBoard[i][j] != null) {

                    Tile t = onBoard[i][j];
                    graphics.setFont(new Font("MonoLisa", Font.PLAIN, 27));
                    int adjust = 0;

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
                    if (t.isBlank()) {
                        graphics.setColor(RED);
                    }
                    graphics.drawString(t.getLetter(), t.getRow() + adjust, t.getCol() + 24);
                    graphics.setColor(BLACK);
                    graphics.setFont(new Font("MonoLisa", Font.PLAIN, 10));
                    if (!t.getLetter().equals("?")) {
                        graphics.drawString(Integer.toString(t.getValue()), t.getRow(), t.getCol() + 29);
                    }
                }
            }
        }
    }

    private void printScores(final Graphics graphics) {
        graphics.setColor(GRID_COLOR_E);
        graphics.fillRect(30, 10, 205, 30);
        graphics.fillRect(275, 10, 205, 30);
        graphics.setColor(BLACK);
        graphics.setFont(new Font("MonoLisa", Font.PLAIN, 27));
        if (currentPlayer() == user1) {
            graphics.setColor(BLUE);
            graphics.setFont(new Font("MonoLisa", Font.PLAIN, 28));
        } else {
            graphics.setColor(BLACK);
            graphics.setFont(new Font("MonoLisa", Font.PLAIN, 27));
        }
        graphics.drawString("Player One: " + user1.getScore(), 33, 35);
        if (currentPlayer() == user2) {
            graphics.setColor(BLUE);
            graphics.setFont(new Font("MonoLisa", Font.PLAIN, 28));
        } else {
            graphics.setColor(BLACK);
            graphics.setFont(new Font("MonoLisa", Font.PLAIN, 27));
        }
        graphics.drawString("Player Two: " + user2.getScore(), 278, 35);

    }

    @Override
    public void paintComponent(final Graphics graphics) {
        graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
        this.printScores(graphics);
        this.paintText(graphics);
        this.paintGrid(graphics);
        currentPlayer().draw(graphics);
        this.paintTilesOnBoard(graphics);
        this.paintRecallButton(graphics);
        this.paintShuffleHandButton(graphics);
        this.paintSkipTurnButton(graphics);
        this.paintExchangeButton(graphics);

    }

    private Color invisible(final int n, final int n2) {
        if ((n % 2 == 0 && n2 % 2 != 0) || (n % 2 != 0 && n2 % 2 == 0)) {
            return Board.GRID_COLOR_A;
        }
        return Board.GRID_COLOR_B;
    }

    public Coordinate getClick() {
        Coordinate coordinate = null;
        synchronized (this) {
            this.lastClick = null;
            while (this.lastClick == null) {
                try {
                    this.wait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.exit(-1);
                }
            }
            coordinate = new Coordinate((int) Math.floor((this.lastClick.getRow() - 50) / 30),
                    (int) Math.floor((this.lastClick.getCol() - 30) / 30));
        }
        return coordinate;
    }

    public int getPosition() {
        return this.getClick().getCol();
    }

    public void mouseClicked(final MouseEvent mouseEvent) {
        final int n = (int) mouseEvent.getPoint().getX();
        final int n2 = (int) mouseEvent.getPoint().getY();
        synchronized (this) {
            if (n >= 30 && n <= 30 + this.columns * 30 - 1 && n2 >= 50 && n2 <= 50 + this.rows * 30 - 1) {
                this.lastClick = new Coordinate((int) mouseEvent.getPoint().getY(), (int) mouseEvent.getPoint().getX());
                this.notifyAll();
            }
        }
        if (n >= 393 && n <= 413 && n2 >= 530 && n2 <= 550) {// recall
            currentPlayer().alphabetizeAndPutInHand();

        }
        if (n >= 93 && n <= 113 && n2 >= 530 && n2 <= 550) {// shuffle
            currentPlayer().shuffleHand();
        }
        if (n >= 33 && n <= 53 && n2 >= 530 && n2 <= 550) {// skip
            user1.switchTurn();
            user2.switchTurn();
        }
        if (n2 >= 530 && n2 <= 550 && n >= 450 && n <= 490) { // exchange
            if(currentPlayer().getBagged().exchangeValid()){
                currentPlayer().alphabetizeAndPutInHand();
                currentPlayer().switchExchange();
                System.out.println("Switched exchange");
            }else{
                System.out.println("Not enough Tiles left to exchange");
            }
            
        }
        this.repaint();
    }

    public int getColumns() {
        return this.grid.length;
    }

    public int getRows() {
        return this.grid[0].length;
    }

    public void mouseEntered(final MouseEvent mouseEvent) {
    }

    public void mouseExited(final MouseEvent mouseEvent) {
    }

    public void mousePressed(final MouseEvent mouseEvent) {
        final int n = (int) mouseEvent.getPoint().getX();
        final int n2 = (int) mouseEvent.getPoint().getY();
        for (int i = 0; i < currentPlayer().TilesInHand(); i++) {
            // tile is clicked
            if (n >= currentPlayer().getHand()[i].getRow() && n <= currentPlayer().getHand()[i].getRow() + 30
                    && n2 >= currentPlayer().getHand()[i].getCol()
                    && n2 <= currentPlayer().getHand()[i].getCol() + 30) {
                if (currentPlayer().areWeExchangingButInLimbo()) {
                    if (!currentPlayer().getHand()[i].amILeavingButJustWaiting()) {
                        currentPlayer().getHand()[i].changingX(currentPlayer().getHand()[i].getCol() - 15);
                        currentPlayer().getHand()[i].switchWaitingForEx();
                    } else {
                        currentPlayer().getHand()[i].changingX(currentPlayer().getHand()[i].getCol());
                        currentPlayer().getHand()[i].switchWaitingForEx();
                    }
                } else {
                    currentPlayer().getHand()[i].clickedOn();
                }

            }
        }

    }

    public void mouseDragged(final MouseEvent mouseEvent) {
        final int n = (int) mouseEvent.getPoint().getX();
        final int n2 = (int) mouseEvent.getPoint().getY();

        for (int i = 0; i < currentPlayer().TilesInHand(); i++) {
            if (currentPlayer().getHand()[i].currentlyClicked()) {
                // mouse on board
                if (n >= 30 && n <= 30 + 30 * 15 && n2 >= 50 && n2 <= 50 + 30 * 15) {
                    currentPlayer().getHand()[i].changingCords(n2, n, true);
                } else {// not on board
                    currentPlayer().getHand()[i].changingCords(n2 - 15, n - 15, false);
                }

                repaint();
            }
        }
    }

    public void mouseReleased(final MouseEvent mouseEvent) {
        final int n = (int) mouseEvent.getPoint().getX();
        final int n2 = (int) mouseEvent.getPoint().getY();
        for (int i = 0; i < currentPlayer().TilesInHand(); i++) {
            if (currentPlayer().getHand()[i].currentlyClicked()) {
                if (currentPlayer().getHand()[i].getCol() < 50 || currentPlayer().getHand()[i].getCol() > 50 + 30 * 15
                        || currentPlayer().getHand()[i].getRow() < 30
                        || currentPlayer().getHand()[i].getRow() > 30 + 30 * 15) {
                    currentPlayer().getHand()[i].changingCords(currentPlayer().getHand()[i].getHandY(),
                            currentPlayer().getHand()[i].getHandX(), false);
                    currentPlayer().getHand()[i].blankReturning();
                } else {
                    currentPlayer().getHand()[i].changingCords(n2, n, true);
                    if (currentPlayer().getHand()[i].isBlank()) {
                        this.waitingForLetter = true;
                        System.out.println("here");
                        VALUE = i;
                        System.out.println("Click a letter to set the blank");
                    }
                }
            }
            currentPlayer().getHand()[i].clickedOff();

        }
        repaint();
    }

    public void mouseMoved(final MouseEvent mouseEvent) {
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        int clicked = ke.getKeyChar();
        if (clicked >= 97 && clicked <= 122 && waitingForLetter || clicked >= 65 && clicked <= 90 && waitingForLetter) {
            String key = String.valueOf(ke.getKeyChar());
            currentPlayer().getHand()[VALUE].blankLetterMaking(key);
            waitingForLetter = false;
        } else if (waitingForLetter) {
            System.out.println("That is not acceptable, please try again");
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int clicked = ke.getKeyCode();
        if (clicked == KeyEvent.VK_ENTER) {
            if (currentPlayer().areWeExchangingButInLimbo()) {
                currentPlayer().exchange();
                user1.switchTurn();
                user2.switchTurn();
            } else {

                // checks if word is valid
                if (currentPlayer().wordValid(onBoard)) {
                    int keep = currentPlayer().onBoardNumber();
                    Tile[] hold = new Tile[keep];
                    hold = currentPlayer().placedTiles();
                    for (int i = 0; i < keep; i++) {
                        onBoard[hold[i].boardX()][hold[i].boardY()] = hold[i];
                        numOnBoard++;
                    }
                    currentPlayer().replaceHand();
                    user1.switchTurn();
                    user2.switchTurn();
                } else {
                    System.out.println("Play isn't valid");
                }
            }
        }
        repaint();
    }

    public void keyReleased(KeyEvent ke) {
    }

    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

    static {
        // basic board
        GRID_COLOR_A = new Color(210, 180, 140);
        // triple words
        GRID_COLOR_B = Color.RED;
        // double words
        GRID_COLOR_C = Color.PINK;
        // triple letters
        GRID_COLOR_D = Color.BLUE;
        // double letters
        GRID_COLOR_E = new Color(173, 216, 230);
        // tile color
        TILE_COLOR = new Color(227, 207, 170);
        YELLOW = Color.YELLOW;
        BLUE = Color.BLUE;
        CYAN = Color.CYAN;
        GREEN = Color.GREEN;
        BLACK = Color.BLACK;
        WHITE = Color.WHITE;
        RED = Color.RED;
        ORANGE = Color.ORANGE;
        LIGHTBLUE = new Color(173, 216, 230);

    }

}
