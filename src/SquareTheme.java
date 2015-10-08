public class SquareTheme implements BoardTheme {

    /**
     * Loads the stone image
     *
     * @return the path to the stone
     */
    @Override
    public String getStoneImage() {
        return "/img/s2.png";
    }

    /**
     * Loads the mancala pit
     *
     * @return the path to the mancala pit img
     */
    @Override
    public String getEndPitFile() {
        return "/img/mancala1.png";
    }

    /**
     * Loads the regular pits
     *
     * @return the path to the regular pit
     */
    @Override
    public String getPitFile() {
        return "/img/pit1.png";
    }
}