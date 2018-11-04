import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;

// represents the user-facing board of FloodIt
public class Board {
  static final ArrayList<String> colors = new ArrayList<String>(
      Arrays.asList("red", "orange", "yellow", "blue", "green"));
  
  ArrayList<ArrayList<Cell>> board;
  int size;
  Random rand;

  // creates a new Board object
  Board(int size) {
    this.board = new ArrayList<ArrayList<Cell>>(size);
    this.size = size;
    this.rand = new Random();
    this.initialize();
  }
  
  // for testing purposes only
  Board(int size, int seed) {
    this(size);
    this.rand = new Random(seed);
    this.initialize();
  }
  
  // for testing purposes only
  Board(ArrayList<ArrayList<Cell>> list) {
    this.size = list.size();
    this.board = list;
  }
  
  // initializes the board field with a 2D arraylist
  void initialize() {
    this.board = new ArrayList<ArrayList<Cell>>(size);
    // initialize cells
    for (int i = 0; i < this.size; i++) {
      ArrayList<Cell> sublist = new ArrayList<Cell>();
      for (int j = 0; j < this.size; j++) {
        sublist.add(new Cell(i * Cell.SIZE, j * Cell.SIZE, i, j, this.getRandomColor()));
      }
      board.add(sublist);
    }

    // set cell neighbors
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        board.get(i).get(j).setNeighbors(board);
      }
    }
  }

  WorldScene render(FloodItWorld world) {
    WorldScene scene = world.getEmptyScene();
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        board.get(i).get(j).addToScene(scene);
      }
    }
    scene.placeImageXY(new TextImage(world.turns + "/" + FloodItWorld.MAX_TURNS, Color.BLACK), Cell.SIZE * size / 2, Cell.SIZE * size + Cell.SIZE / 2);
    return scene;
  }
  
  // returns the cell that corresponds to the given Posn
  Cell getClickedCell(Posn pos) {
    return board.get(pos.x / Cell.SIZE).get(pos.y / Cell.SIZE);
  }
  
  // sets the top left cell color to the given color
  void setTopLeftColor(String color) {
    board.get(0).get(0).color = color;
  }
  
  // returns the color of the top left cell
  String getTopLeftColor() {
    return board.get(0).get(0).color;
  }
  
  // floods the board given the previous color of the top left cell
  void flood(String prevColor) {
    board.get(0).get(0).flood(prevColor);
  }
  
  // returns true if all of the cells in the board have the same color
  boolean allSameColor() {
    String color = board.get(0).get(0).color;
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        if (!board.get(i).get(j).color.equals(color)) {
          return false;
        }
      }
    }
    return true;
  }

  // returns a random color from the static list of colors
  String getRandomColor() {
    return colors.get(rand.nextInt(colors.size()));
  }
}
