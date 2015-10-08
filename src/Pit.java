
public class Pit {
	private int player;
    private int stones;
    private boolean mancala;

    /**
     * Constructs a pit with a given number of marbles
     * @param numStones the number of stones in the pit
     * @param player the player who has the pit
     * @param isMancala true if it is a mancala, otherwise false
     */
    public Pit(int numStones, int player, boolean isMancala) {
        this.mancala = isMancala;
        this.player = player;
        this.stones = numStones;
    }

    /**
     * Copies  a pit
     * @param other the other pit
     */
    public Pit(Pit other) {
        copyPit(other);
    }
    
    /**
     * copies another pit's player owner, number of stones and mancala pit characteristic
     * @param other the other pit
     */
    public void copyPit(Pit other) {
        this.player = other.player;
        this.stones = other.stones;
        this.mancala = other.mancala;
    }

    /**
     * Returns true if the pit is a mancala, else false
     * @return true if the pit is a mancala, else false
     */
    public boolean isMancala() {

        return mancala;
    }

    /*
     * Sets a pit to be an end pit or not
     * @param isEnd true if it is an end pit, else false
     */
    public void setEnd(boolean isEnd) {
        this.mancala = isEnd;
    }

    /**
     * Gets the player that has this pit
     * @return the player number
     */
    public int getPlayer() {
        return this.player;
    }

    /**
     * Sets the player who has this pit
     * @param player the player who has this pit
     */
    public void setPlayer(int player) {
        this.player = player;
    }

    /**
     * Gets the number of marbles in this pit
     * @return the number of marbles in this pit
     */
    public int getStones() {
        return this.stones;
    }

    /**
     * Set the number of marbles in the pit.
     *
     * @param numStones the number of marbles in the pit to be set
     */
    public void setStones(int numStones) {
        this.stones = numStones;
    }

    /**
     * Adds one stone to the pit.
     */
    public void addStone() {
        this.stones++;
        System.out.println("adding marble " + getStones());
    }

    /**
     * override toString()
     * @return a String that contains information about the pit.
     */
    @Override
    public String toString() {
        return "Pit{" + "player=" + player + ", marbles=" + stones + ", isEnd=" + mancala + '}';
    }
}
