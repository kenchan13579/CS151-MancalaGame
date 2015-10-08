import javax.swing.JPanel;
import javax.swing.event.*;


public interface GameBoard extends ChangeListener
{
    public void stateChanged(ChangeEvent e);
    void addPitView(int index, JPanel regularPitPanel);
}