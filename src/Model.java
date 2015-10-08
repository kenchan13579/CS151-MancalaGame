import java.util.*;

import javax.swing.event.*;

/*
 *      	B13	|	B12	|	B11 |  B10  |   B9  | B8					
 *      A0												B7
 *         A1	|	A2	|	A3	|	A4	|	A5	|	A6	
 */
public class Model {
	private Pit[] board;
    private Pit[] previousBoard = null;
    private ArrayList<ChangeListener> listeners = new ArrayList<>();
    private int currentPlayer = PLAYER_ONE;
    private int previousPlayer = PLAYER_TWO;
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;
    public static final int PLAYER_ONE_MANCALA = 0;
    public static final int PLAYER_TWO_MANCALA = 7;
    public static final int MAX_PIT = 6;
    private int undoCounter = 0;
    
    /**
     * Initialize a model with a specified number of stones.
     *
     * @param Stones number of stones to initialize model with.
     */
    public Model(int Stones) {
        board = new Pit[14];
        createBoard(Stones);
    }

    /**
     * Return the current board.
     *
     * @return Pit[] pit array representing the board.
     */
    public Pit[] getBoard() {
        return this.board;
    }

    /**
     * Create a board with a specified number of Stones.
     *
     * @param numStones number of stones to create the board with.
     */
    private void createBoard(int numStones) {
      
        this.board[PLAYER_ONE_MANCALA] = new Pit(0, PLAYER_ONE, true);
        for (int i = PLAYER_ONE_MANCALA + 1; i < PLAYER_TWO_MANCALA; i++) {
            this.board[i] = new Pit(numStones, PLAYER_ONE, false);
        }

        this.board[PLAYER_TWO_MANCALA] = new Pit(0, PLAYER_TWO, true);
        for (int i = PLAYER_TWO_MANCALA + 1; i < board.length; i++) {
            this.board[i] = new Pit(numStones, PLAYER_TWO, false);
        }

        this.currentPlayer = PLAYER_ONE;
    }

    /**
     * Print the current board.
     */
    public void printBoard() {
        for (Pit p : board) {
            System.out.println(p.getPlayer() + " : " + p.getStones());
        }
    }
    /**
     * If the pit is the current players pit, not and end pit, and has stones,
     * it is valid.
     *
     * @param pit to validate
     * @return true if valid, false if invalid.
     */
    private boolean isValidPit(Pit pit) {
        if (pit.isMancala())
            return false;
         if (pit.getPlayer() != this.currentPlayer) {
           System.out.println("That isn't your pit.");
            return false;
        } 
         return true;
    }
    /**
     * Returns true if the turn was successfully executed, else false.
     *
     * @param pit the pit
     * @return true if the turn was successfully executed, else false.
     */
    public boolean executeTurn(Pit pit) {
      
        if (isValidPit(pit))
        {
        this.distributeStones(pit);
        return true;
        }
        else 
        	 return false;
    }

    /**
     * Adds a listener to notify for changes in the data.
     *
     * @param listener
     */
    public void addListener(ChangeListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Removes a listener from the model.
     *
     * @param listener A ChangeListener.
     */
    public void removeListener(ChangeListener listener) {
        this.listeners.remove(listener);
    }

  

    /**
     * Distributes stones from a specified pit.
     *
     * @param pit the pit to distribute stones from.
     * @return if the stones were distributed correctly.
     */
    private boolean distributeStones(Pit pit) {
        if (pit.getStones() == 0) {
            
            System.out.println("No more stones in this pit.");
            return false;
        }

        setUndo(); // record current board

        int pitIndex = this.getPitIndex(pit);
        int numIterations = pit.getStones();
        pit.setStones(0);

        Pit lastPitVisited = null;
        for (int i = 1; i <= numIterations; i++) {
            
            int currentPitIndex;
            if (lastPitVisited == null) {
                currentPitIndex = this.getEndingIndex(pitIndex, 1);
            } else {
               
                int lastPitIndex = this.getPitIndex(lastPitVisited);
                currentPitIndex = this.getEndingIndex(lastPitIndex, 1);
            }

            Pit currentPit = board[currentPitIndex];
            lastPitVisited = currentPit;

           
            if (currentPit.isMancala() == true) {
               
                if (currentPit.getPlayer() == this.currentPlayer) {
               
                    currentPit.setStones(currentPit.getStones() + 1);
                } else {
                   
                    if (currentPitIndex != 0) {
                        currentPitIndex--;
                    } else {
                        currentPitIndex = board.length - 1;
                    }
                    System.out.println("currentpitindex " + currentPitIndex);
                    lastPitVisited = board[currentPitIndex];
                    currentPit = lastPitVisited;
                    currentPit.setStones(currentPit.getStones() + 1);
                }
            } else {
                //not an end pit just add one
                currentPit.setStones(currentPit.getStones() + 1);
            }
        }


        //iterate over the changelisteners
        if (lastPitVisited.getPlayer() == currentPlayer) {
            this.capture(lastPitVisited);
        }


        if (lastPitVisited.isMancala() == true) {
            System.out.println("free turn!");
            //current player stays the same
        } else {
            int lastPlayer = currentPlayer;

            if (currentPlayer == PLAYER_ONE) {
                currentPlayer = PLAYER_TWO;

            } else {
                currentPlayer = PLAYER_ONE;
            }
            if (lastPlayer != currentPlayer && undoCounter >= 3) {
                undoCounter = 0;
                previousBoard = null;
                System.out.println("UNDO COUNTER RESET");
            }
        }

        System.out.println("Next turn by: " + currentPlayer);

        if (checkEndGame()) {
            this.moveRemainingStones();
        }

        ChangeEvent event = new ChangeEvent(this);
        dispatch(event);
        return true;

    }

    /**
     * Checks if the current player has any undo's left.
     *
     * @return True/False if player can/can't undo.
     */
    public boolean canUndo() {
        if (previousBoard != null && undoCounter < 3) {
            return true;
        }
        return false;
    }

    /**
     * Creates an instance of the current model so that the player can undo and
     * go back to this version of the game.
     */
    private void setUndo() {
        Pit[] temp = new Pit[this.board.length];


        for (int i = 0; i < board.length; i++) {
            Pit p = new Pit(board[i]);
            temp[i] = p;
        }
        this.previousBoard = temp;
        this.previousPlayer = this.currentPlayer;
    }

    /**
     * Notifies change listeners of changes to the model.
     *
     * @param event (Model) A change to the model.
     */
    private void dispatch(ChangeEvent event) {
        for (ChangeListener listener : this.listeners) {
            listener.stateChanged(event);
        }
    }

    /**
     * Tries to capture the pit opposite the empty pit landed in.
     *
     * @param lastPitVisited The pit that was landed in.
     */
    private void capture(Pit lastPitVisited) {
        if (lastPitVisited == null) {
            return;
        }

        if (lastPitVisited.getStones() == 1 && !lastPitVisited.isMancala()) {
            //capture
            int index = this.getPitIndex(lastPitVisited);
            int captureIndex = Math.abs(index - board.length);
            Pit capturePit = this.getPit(captureIndex);

            if (capturePit.getStones() != 0) {
                if (this.currentPlayer == PLAYER_ONE) {
                    Pit player1Mancala = board[PLAYER_ONE_MANCALA];
                    player1Mancala.setStones(player1Mancala.getStones() + capturePit.getStones() + 1);
                } else if (this.currentPlayer == PLAYER_TWO) {
                    Pit player2Mancala = board[PLAYER_TWO_MANCALA];
                    player2Mancala.setStones(player2Mancala.getStones() + capturePit.getStones() + 1);
                }

                capturePit.setStones(0);
                lastPitVisited.setStones(0);

                System.out.println("CAPTURE");
            }
        }
    }

    /**
     * Gets the corresponding index of the pit in the Pit[] array.
     *
     * @param pit Pit that you would like to know the index of.
     * @return int corresponding to the index of the pit.
     */
    private int getPitIndex(Pit pit) {

       
        int pitIndex = 0;
        for (Pit indexPit : board) {
            if (indexPit == pit) {
                return pitIndex;
            }
            pitIndex++;
        }
        return -1;
    }

    /**
     * Provides offset for distributing stones.
     *
     * @param pitIndex
     * @param i
     * @return ending index
     */
    private int getEndingIndex(int pitIndex, int i) {
        int endingIndex = pitIndex - i;
        if (endingIndex < 0) {
           
            endingIndex = endingIndex + board.length;

        }
        return endingIndex;

    }

    /**
     * Gets a string representing the model as a string.
     *
     * @return String representing the current model.
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        int counter = 0;
        for (Pit pit : board) {
            buffer.append(pit.toString()).append("    ").append(counter).append("\n");
            counter++;
        }
        return buffer.toString();
    }

    /**
     * Gets a pit from the Pit[] corresponding to the pit index.
     *
     * @param index the index of the pit to get.
     * @return Pit of the index provided.
     */
    public Pit getPit(int index) {
        if (index < board.length && index >= 0) {
            return board[index];
        }
        return null;
    }

    /**
     * Gets the current player.
     *
     * @return int representing current player in model.
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player.
     *
     * @param currentPlayer int representing current player.
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Gets player one's score.
     *
     * @return int representing stones in mancala.
     */
    public int getPlayerOneScore() {
        return board[PLAYER_ONE_MANCALA].getStones();
    }

    /**
     * Gets player two's score.
     *
     * @return int representing stones in mancala.
     */
    public int getPlayerTwoScore() {
        return board[PLAYER_TWO_MANCALA].getStones();
    }

    /**
     * Checks if the game is over.
     *
     * @return True/False if games is over.
     */
    public boolean checkEndGame() {
        boolean isGameOver;
        int player1StoneCount = this.player1StoneCount();
        int player2StoneCount = this.player2StoneCount();
        if (player1StoneCount == 0 || player2StoneCount == 0) {
            isGameOver = true;
            return isGameOver;
        } else {
            isGameOver = false;
            return isGameOver;
        }
    }

    /**
     * Undoes the move that was just made.
     */
    void undo() {
        if (canUndo()) {
            for (int i = 0; i < board.length; i++) {
                board[i].copyPit(previousBoard[i]);
            }
            previousBoard = null;
            this.currentPlayer = this.previousPlayer;
            ChangeEvent event = new ChangeEvent(this);
            dispatch(event);

            System.out.println("undo method called");
            this.undoCounter++;
        }
    }

    /**
     * Gets the number of stones on player one's side.
     *
     * @return int of the number of stones on player one's side.
     */
    private int player1StoneCount() {
        int counter = 0;
        for (int i = PLAYER_ONE_MANCALA + 1; i < PLAYER_TWO_MANCALA; i++) {
            counter += board[i].getStones();
        }
        return counter;
    }

    /**
     * Gets the number of stones on player two's side.
     *
     * @return int of the number of stones on player two's side.
     */
    private int player2StoneCount() {
        int counter = 0;
        for (int i = PLAYER_TWO_MANCALA + 1; i < board.length; i++) {
            counter += board[i].getStones();
        }
        return counter;
    }

    /**
     * When the game ends, move all of the stones on the respective player's
     * side to their mancala.
     */
    private void moveRemainingStones() {
        int player1Stones = this.player1StoneCount();
        int player2Stones = this.player2StoneCount();

        board[PLAYER_ONE_MANCALA].setStones(board[PLAYER_ONE_MANCALA].getStones() + player1Stones);
        board[PLAYER_TWO_MANCALA].setStones(board[PLAYER_TWO_MANCALA].getStones() + player2Stones);

        for (Pit pit : board) {
            if (pit.isMancala() == false) {
                pit.setStones(0);
            }
        }
    }
}
