/*
 * @author Ken 
 */
public class MancalaTest
{
    /**
     * Opens and runs the game starting from the homeview frame (initial screen)
     * @param args Command Line arguments.
     */
    public static void main(String args[])
    {
        HomeView home = new HomeView();
        home.setSize(1100, 340);
        home.setResizable(false);
        home.setVisible(true);
    }
}