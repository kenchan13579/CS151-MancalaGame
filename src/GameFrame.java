import java.awt.*;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

public class GameFrame extends JFrame implements GameBoard {

    private Model model;
    private ArrayList<PitView> pitViews = new ArrayList<>();
    private JButton undoButton;
    private JLabel currentPlayer;
    private JLabel pOneScore;
    private JLabel pTwoScore;
    private BoardTheme theme;

    /**
     * The constructor that loads the model and the theme
     * @param aModel
     * @param theme 
     */
    public GameFrame(Model aModel, BoardTheme theme) {
        this.setBackground(Color.RED);
        this.theme = theme;
        this.model = aModel;
        model.addListener(this);
        JPanel parent = new JPanel();
        parent.setLayout(new BorderLayout());

     
        PitView player1EndPit = new PitView(model, model.getPit(Model.PLAYER_ONE_MANCALA), this.theme);
        PitView player2EndPit = new PitView(model, model.getPit(Model.PLAYER_TWO_MANCALA), this.theme);

        pitViews.add(player1EndPit);
        pitViews.add(player2EndPit);
        parent.add(player1EndPit, BorderLayout.EAST);
        parent.add(player2EndPit, BorderLayout.WEST);

        currentPlayer = new JLabel(Integer.toString(model.getCurrentPlayer()));
        pOneScore = new JLabel("P1: " + Integer.toString(model.getPlayerOneScore()));
        pTwoScore = new JLabel("P2: " + Integer.toString(model.getPlayerTwoScore()));
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        infoPanel.add(pTwoScore);
        infoPanel.add(Box.createRigidArea(new Dimension(450, 0)));
        infoPanel.add(currentPlayer);
        infoPanel.add(Box.createRigidArea(new Dimension(450, 0)));
        infoPanel.add(pOneScore);
        parent.add(infoPanel, BorderLayout.NORTH);
        this.updateCurrentPlayer();

        JPanel undoPanel = new JPanel(new FlowLayout());
        undoButton = new JButton("UNDO");
        undoButton.setEnabled(false);
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked");

                model.undo();
            }
        });
        undoPanel.add(undoButton, BorderLayout.NORTH);
        undoPanel.setBackground(Color.RED);
        parent.add(undoPanel, BorderLayout.SOUTH);

        JPanel regularPitPanel = new JPanel();

       
        this.addPitView(8, regularPitPanel);
        this.addPitView(9, regularPitPanel);
        this.addPitView(10, regularPitPanel);
        this.addPitView(11, regularPitPanel);
        this.addPitView(12, regularPitPanel);
        this.addPitView(13, regularPitPanel);

        this.addPitView(6, regularPitPanel);
        this.addPitView(5, regularPitPanel);
        this.addPitView(4, regularPitPanel);
        this.addPitView(3, regularPitPanel);
        this.addPitView(2, regularPitPanel);
        this.addPitView(1, regularPitPanel);

        SpringUtilities.makeGrid(regularPitPanel, 2, 6, 0, 0, 5, 5);
        parent.add(regularPitPanel, BorderLayout.CENTER);
        add(parent);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 320);
        this.setResizable(false);
        this.setTitle("#ODOT MANCALLA");
        setVisible(true);
    }

    /**
     * Updates who the current player is
     */
    private void updateCurrentPlayer() {
        this.currentPlayer.setText("Current Player: " + Integer.toString(model.getCurrentPlayer()));
    }

    /**
     * Updates the current scores
     */
    private void updateCurrentScores() {
        this.pOneScore.setText("P1: " + Integer.toString(model.getPlayerOneScore()));
        this.pTwoScore.setText("P2: " + Integer.toString(model.getPlayerTwoScore()));
    }

    /**
     * Adds a pitview to a jpanel
     * @param index the index of the pit to be added
     * @param regularPitPanel the panel that houses the pit
     */
    @Override
    public void addPitView(int index, JPanel regularPitPanel) {
        PitView p = new PitView(model, model.getPit(index), theme);
        this.pitViews.add(p);
        regularPitPanel.add(p);
    }

    /**
     * Carries out all necessary actions when an event happens. 
     * @param e the change event 
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        this.model = (Model) e.getSource();

        if (!model.canUndo()) {
            this.undoButton.setEnabled(false);
        } else {
            this.undoButton.setEnabled(true);
        }

        for (PitView pitView : pitViews) {
            pitView.updateView();
        }
        this.updateCurrentPlayer();
        this.updateCurrentScores();
        if (model.checkEndGame()) {
            this.undoButton.setEnabled(false);
        }
        this.repaint();

        if (model.checkEndGame()) {
            //game over
            this.displayerWinningPlayer();
            System.exit(-1);
        }
    }

    /**
     * Shows the game is over and who won the game
     */
    private void displayerWinningPlayer() {
        Pit[] board = model.getBoard();

        int player1Marbles = board[Model.PLAYER_ONE_MANCALA].getStones();
        int player2Marbles = board[Model.PLAYER_TWO_MANCALA].getStones();
        if (player1Marbles > player2Marbles) {
            JOptionPane.showMessageDialog(this, "GAME OVER! \n Player 1 Wins!");
        } else if (player2Marbles > player1Marbles) {
            JOptionPane.showMessageDialog(this, "GAME OVER! \n Player 2 Wins!");
        } else {
            JOptionPane.showMessageDialog(this, "GAME OVER! \n We are all winners!");
        }
    }
}