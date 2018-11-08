import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;

// represents the user-facing board of FloodIt
public class Board {
  static final ArrayList<Color> colors = new ArrayList<Color>(
      Arrays.asList(Color.red, Color.orange, Color.yellow, Color.blue, Color.green));

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
  // EFFECT: populate this.board with new Cells
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

  // render the board into a WorldScene
  WorldScene render(FloodItWorld world) {
    WorldScene scene = world.getEmptyScene();
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        board.get(i).get(j).addToScene(scene);
      }
    }
    scene.placeImageXY(
        new TextImage(world.turns + "/" + FloodItWorld.MAX_TURNS, Color.BLACK),
        Cell.SIZE * size / 2, Cell.SIZE * size + Cell.SIZE / 2);
    return scene;
  }

  // returns the cell that corresponds to the given Posn
  Cell getClickedCell(Posn pos) {
    return board.get(pos.x / Cell.SIZE).get(pos.y / Cell.SIZE);
  }

  // sets the top left cell color to the given color
  void setTopLeftColor(Color color) {
    board.get(0).get(0).color = color;
  }

  // returns the color of the top left cell
  Color getTopLeftColor() {
    return board.get(0).get(0).color;
  }

  // floods the board given the previous color of the top left cell
  void flood(Color prevColor) {
    clearVisiting();
    Cell corner = board.get(0).get(0);
    corner.visited = true;
    corner.flood(prevColor);
  }

  // EFFECT: set the flooded boolean of all Cells to false
  void clearFlooding() {
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        board.get(i).get(j).flooded = false;
      }
    }
  }

  // EFFECT: set the visited boolean of all Cells to false
  void clearVisiting() {
    for (int i = 0; i < this.size; i++) {
      for (int j = 0; j < this.size; j++) {
        board.get(i).get(j).visited = false;
      }
    }
  }

  // returns true if all of the cells in the board have the same color
  boolean allSameColor() {
    Color color = board.get(0).get(0).color;
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
  Color getRandomColor() {
    return colors.get(rand.nextInt(colors.size()));
  }
}
