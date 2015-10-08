/**
 * An interface to help get BoardThemes. This follows the strategy pattern
 * @author Ken
 */
public interface BoardTheme {
    public String getStoneImage();
    public String getEndPitFile();
    public String getPitFile();
}