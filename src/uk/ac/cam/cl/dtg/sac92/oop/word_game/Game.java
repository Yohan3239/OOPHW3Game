package uk.ac.cam.cl.dtg.sac92.oop.word_game;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Grid;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.GridGUI;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.TileCollection;

import java.awt.BorderLayout;

/**
 * Game This is a basic class that can be modified to create a word game.
 * 
 * Hint: Can this class be converted into a singleton?
 * 
 * @author Stephen Cummins
 * @version 1.0 Released 11/10/2005
 */
public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	private GameController controller = new GameController();

	/**
	 * Creates an instance of the Game.
	 */
	public Game() {
		// build the GUI as soon as the default constructor is called.
		buildGUI();
	}

	/**
	 * This method will construct each element of the game's GUI
	 */
	public void buildGUI() {
		final JFrame frame = new JFrame("Java Word Game");

		TileCollection collection = new TileCollection();
		final Grid grid = new Grid(6, 6, collection);
		final GridGUI gui = new GridGUI(grid);
		gui.setTileForeground(Color.yellow);
		gui.setTileBackground(Color.blue);

		JPanel controls = new JPanel();
		JPanel wordEntry = new JPanel();
		JPanel buttons = new JPanel();
		JLabel scoreLabel = new JLabel("Score: 0");
		JButton resetButton = new JButton("Reset Selection");
		JButton submitButton = new JButton("Submit Word");
		JLabel submittedWordsPanel = new JLabel("Submitted Words");
		buttons.setLayout(new BorderLayout());
		wordEntry.setLayout(new BorderLayout());
		controls.setLayout(new BorderLayout());
		controls.add(scoreLabel, BorderLayout.NORTH);
		buttons.add(submitButton, BorderLayout.EAST);
		buttons.add(resetButton, BorderLayout.WEST);
		controls.add(buttons, BorderLayout.SOUTH);
		controls.add(wordEntry, BorderLayout.CENTER);
		controls.add(submittedWordsPanel, BorderLayout.EAST);

		controller.readAllowed();
		gui.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {

				Tile source = (Tile) actionEvent.getSource();
				Tile lastTile = controller.getSelectedTiles().isEmpty() ? null : controller.getSelectedTiles().get(controller.getSelectedTiles().size() - 1);
				if (controller.getUsedTiles().contains(grid.positionOf(source))) {
				    return;
				}
				if (lastTile != null && !source.checkActive()) {
				    Point lastPos = grid.positionOf(lastTile);
				    Point sourcePos = grid.positionOf(source);
				    int dx = Math.abs(lastPos.x - sourcePos.x);
				    int dy = Math.abs(lastPos.y - sourcePos.y);
				    if (dx > 1 || dy > 1) {
				        return;
				    }
				}
				controller.tileActivated(source);
				refreshGridGUI(grid, gui, scoreLabel, submittedWordsPanel);
			}
		});
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				controller.resetSelection();
				refreshGridGUI(grid, gui, scoreLabel, submittedWordsPanel);
			}
		});

		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (!controller.submitWord(grid)) System.out.println("Word invalid!");
				refreshGridGUI(grid, gui, scoreLabel, submittedWordsPanel);
			}
		});
		frame.setTitle("Java Word Game");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(gui, BorderLayout.WEST);
		frame.getContentPane().add(controls, BorderLayout.SOUTH);
		frame.pack();
		frame.setResizable(false);
		frame.toFront();

		frame.setBackground(Color.lightGray);
		frame.setVisible(true);
	}
	private void refreshGridGUI(Grid grid, GridGUI gui, JLabel scoreLabel, JLabel submittedWordsPanel) {
	    for (int y = 0; y < grid.height(); y++) {
						for (int x = 0; x < grid.width(); x++) {
								Point position = new Point(x, y);
								Tile tile = grid.tileAt(position);
								gui.setTileBackground(position, (tile.checkActive() ? Color.red : Color.blue));
								if (controller.getUsedTiles().contains(position)) {
								gui.setTileBackground(position, Color.gray);
								}
						}
				}
			scoreLabel.setText("Score: " + controller.getScore() + "   Word: " + controller.getCurrentWord());
			submittedWordsPanel.setText("Submitted Words:" + String.join("\n", controller.getSubmittedWords()));
	}
}
