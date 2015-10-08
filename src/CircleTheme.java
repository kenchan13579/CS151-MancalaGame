public class CircleTheme implements BoardTheme {

    /**
     * Loads the stone image
     *
     * @return the path to the stone image
     */
    public String getStoneImage() {
        String s = "/img/s1.png";
        return s;
    }
    /**
     * Loads the regular pit
     * @return the path to the regular pit image
     */
    @Override
    public String getPitFile() {
        return "/img/pit2.png";
    }
    /**
     * Loads the mancala pit
     *
     * @return the path to the mancala pit image
     */
    @Override
    public String getEndPitFile() {
        return "/img/mancala2.png";
    }

    
}