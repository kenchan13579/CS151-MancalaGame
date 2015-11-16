import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author #ODOT
 */
public class PitIcon extends ImageIcon
{
    ArrayList<Point> points;
    int width;
    int height;
    int numMarbs;
    int oldNum;
    int randSeed;
    boolean isEnd;
    boolean firstPaint;
    BufferedImage marbImg;
    BufferedImage pitImg;
    BoardTheme theme = null;

    /**
     * Constructs a pit with a number of marbles.
     *
     * @param numMarbs the number of marbles
     * @param theme the theme that this pit is using
     * @param isEnd true if it is a mancala, else false
     */
    public PitIcon(int numMarbs, boolean isEnd, BoardTheme theme)
    {
        points = new ArrayList<>();
        firstPaint = true;
        this.numMarbs = numMarbs;
        this.oldNum = 0;
        this.theme = theme;
        this.isEnd = isEnd;
        if (isEnd)
        {
            height = 225;
            width = 100;
            try
            {
               pitImg = ImageIO.read(new File(theme.getPitFile()));
                marbImg = ImageIO.read(new File(theme.getStoneImage()));
            } catch (IOException ex)
            {
                Logger.getLogger(PitIcon.class.getName()).log(Level.SEVERE, "couldn't paint icon", ex);
            }
        } else
        {
            height = 100;
            width = 100;
            try
            {
                pitImg = ImageIO.read(new File(theme.getPitFile()));
                marbImg = ImageIO.read(new File(theme.getStoneImage()));
            } catch (IOException ex)
            {
                Logger.getLogger(PitIcon.class.getName()).log(Level.SEVERE, "couldn't paint icon", ex);
            }

        }


    }

    /**
     * paints an icon
     * @param c the component 
     * @param g the graphics parameter
     * @param x the x coordinate
     * @param y the y coordinate
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        super.paintIcon(c, g, x, y);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(pitImg, 0, 0, c);
        paintMarbles(marbImg, c, g);
    }

    /**
     * Helper function for paintIcon draws marbles.
     *
     * @param b the image to be displayed
     * @param c the component to be used
     * @param g the graphics parameter
     */
    public void paintMarbles(BufferedImage b, Component c, Graphics g)
    {
        //If numMarbs has changed, or icon is being created
        if (oldNum != numMarbs || firstPaint)
        {
            points.removeAll(points);
            int yPos = -20;
            int xPos = -30;
            Random rand = new Random();
            for (int i = 0; i < numMarbs; i++)
            {
                xPos = -20 + rand.nextInt(getIconWidth() / 2);
                yPos = -20 + rand.nextInt(getIconHeight() / 2);
                points.add(new Point(xPos, yPos));
                g.drawImage(b, xPos, yPos, c);
            }
            firstPaint = false;
        } else //draw points in same position
        {
            for (Point p : points)
            {
                g.drawImage(b, p.x, p.y, c);
            }
        }
    }

    /**
     * Sets the number of marbles to draw.
     *
     * @param marbs
     */
    public void setNumStones(int marbs)
    {
        this.oldNum = this.numMarbs;
        this.numMarbs = marbs;
    }

    /**
     * Gets the width of the icon
     * @return the icon width
     */
    @Override
    public int getIconWidth()
    {
        return width;
    }

    /**
     * Gets the height of the icon
     * @return the icon height
     */
    @Override
    public int getIconHeight()
    {
        return height;
    }
}