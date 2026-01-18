package uk.ac.cam.cl.dtg.sac92.oop.word_game;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Tile;
import java.util.Scanner;
import java.awt.Point;
import uk.ac.cam.cl.dtg.sac92.oop.word_game.grid.Grid;

public class GameController {
    private ArrayList<Tile> selectedTiles = new ArrayList<Tile>();
    private StringBuilder currentWord = new StringBuilder();
    private int score = 0;
    private List<String> allowedWords = new ArrayList<String>();
    private File file = new File("src\\main\\resources\\words.txt");
    private ArrayList<String> submittedWords = new ArrayList<String>();
    private ArrayList<Point> usedTiles = new ArrayList<Point>();

    public void readAllowed() {
        try (Scanner scanner = new Scanner(file)) {
          while (scanner.hasNextLine()) {
            allowedWords.add(scanner.nextLine());
          }
        } catch (Exception e) {
          System.err.println("Error reading allowed words file.");
          e.printStackTrace();
        }
    }
    public void tileActivated(Tile tile) {
      if (tile == null) return;
      if (!tile.checkActive()) {
          tile.active(true);
          selectedTiles.add(tile);
          currentWord.append(tile.letter());
      } else {
          deactivateFromTile(tile);
      }
    }
    private void deactivateFromTile(Tile tile) {
        int idx = -1;
        for (int i = selectedTiles.size() - 1; i >= 0; i--) {
            if (selectedTiles.get(i) == tile) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return;
        ArrayList<Tile> toDeactivate = new ArrayList<Tile>(selectedTiles.subList(idx, selectedTiles.size()));
        selectedTiles = new ArrayList<Tile>(selectedTiles.subList(0, idx));
        currentWord.setLength(0);
        for (Tile t : selectedTiles) {
            currentWord.append(t.letter());
        }
        for (Tile t : toDeactivate) {
            t.active(false);
        }
    }
    public boolean submitWord(Grid grid) {
        String word = currentWord.toString();
        if (allowedWords.contains(word.toLowerCase())) {
          for (Tile t : selectedTiles) {
              score += t.value();
          }
          score += selectedTiles.size(); //bonus
          submittedWords.add(word);
          for (Tile t : selectedTiles) {
              usedTiles.add(grid.positionOf(t));
          }
          resetSelection();
          selectedTiles.clear();
          return true;
        } else {
          return false;
        }
    }
    public String getCurrentWord() {
        return currentWord.toString();
    }
    public ArrayList<Point> getUsedTiles() {
        return usedTiles;
    }
    public ArrayList<Tile> getSelectedTiles() {
        return selectedTiles;
    }

    public int getScore() {
        return score;
    }
    public List<String> getSubmittedWords() {
        return submittedWords;
    }
    public void resetSelection() {
        for (Tile t : selectedTiles) {
            t.active(false);
        }
        currentWord.setLength(0);
        selectedTiles.clear();
    }
}
