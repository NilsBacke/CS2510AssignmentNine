import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents a Flood-It game world
public class FloodItWorld extends World {

  static final int BOARD_SIZE = 22;
  static final int MAX_TURNS = (int) (BOARD_SIZE * 1.6);
  int turns;
  Color prevColor;
  Board board;

  static final int WINDOW_WIDTH = BOARD_SIZE * Cell.SIZE;
  static final int WINDOW_HEIGHT = (BOARD_SIZE + 1) * Cell.SIZE;

  // create a new FloodItWorld
  FloodItWorld() {
    turns = 0;
    board = new Board(BOARD_SIZE);
    prevColor = board.getTopLeftColor();
  }

  // produces the image of this world
  public WorldScene makeScene() {
    return this.board.render(this);
  }

  // when a key is pressed
  // EFFECT: reset the board if the "r" key is pressed
  public void onKeyEvent(String k) {
    if (k.equals("r")) {
      board = new Board(BOARD_SIZE);
      turns = 0;
    }
  }

  // when mouse is clicked
  // EFFECT: flood the board if a different color is clicked
  public void onMouseClicked(Posn pos) {
    if (pos.y < WINDOW_HEIGHT - Cell.SIZE) {
      Cell clickedCell = board.getClickedCell(pos);
      prevColor = board.getTopLeftColor();
      if (!prevColor.equals(clickedCell.color)) {
        board.setTopLeftColor(clickedCell.color);
        turns++;
        board.clearFlooding();
        board.flood(prevColor);
      }
    }
  }

  // runs every game tick
  // EFFECT: run another step of flooding on the board
  public void onTick() {
    board.flood(prevColor);
  }

  // trigger world end
  public WorldEnd worldEnds() {
    // check if user wins
    if (board.allSameColor()) {
      // user wins
      return new WorldEnd(true, this.lastScene("YOU WIN"));
    } else if (turns == MAX_TURNS) {
      // user has used up all turns and hasn't won -> lost
      return new WorldEnd(true, this.lastScene("YOU LOSE"));
    } else {
      // game isn't over yet
      return new WorldEnd(false, this.makeScene());
    }
  }

  // produces the last image of this world by adding a box and text to the image
  public WorldScene lastScene(String s) {
    WorldScene scene = this.makeScene();
    scene.placeImageXY(new RectangleImage(80, 30, "solid", Color.white),
        WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
    scene.placeImageXY(new TextImage(s, Color.red),
        WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
    return scene;
  }
}

// represents data examples, unit tests, and the main function
class FloodItExamples {
  int seed;
  int size;
  Board board1;
  Board board2;
  FloodItWorld world;

  // reset the test conditions
  void reset() {
    seed = 5;
    size = 10;
    board1 = new Board(size, seed);
    ArrayList<ArrayList<Cell>> list = new ArrayList<ArrayList<Cell>>();
    // initialize cells
    for (int i = 0; i < this.size; i++) {
      ArrayList<Cell> sublist = new ArrayList<Cell>();
      for (int j = 0; j < this.size; j++) {
        sublist.add(new Cell(i * Cell.SIZE, j * Cell.SIZE, i, j, Color.green));
      }
      list.add(sublist);
    }
    board2 = new Board(list);

    world = new FloodItWorld();
  }

  // test the getClickedCell method
  void testGetClickedCell(Tester t) {
    reset();
    // Posn must be within the window (20*10, 20*10)
    t.checkExpect(board1.getClickedCell(new Posn(0, 0)), board1.board.get(0).get(0));
    t.checkExpect(board1.getClickedCell(new Posn(195, 195)), board1.board.get(9).get(9));
    t.checkExpect(board1.getClickedCell(new Posn(100, 0)), board1.board.get(5).get(0));
  }

  // test the setTopLeftColor method
  void testSetTopLeftColor(Tester t) {
    reset();
    board1.setTopLeftColor(Color.green);
    t.checkExpect(board1.getTopLeftColor(), Color.green);
    board1.setTopLeftColor(Color.black);
    t.checkExpect(board1.getTopLeftColor(), Color.black);
    board1.setTopLeftColor(Color.red);
    t.checkExpect(board1.getTopLeftColor(), Color.red);
  }

  // test the getTopLeftColor method
  void testGetTopLeftColor(Tester t) {
    reset();
    t.checkExpect(board1.getTopLeftColor(), Color.yellow);
    board1.setTopLeftColor(Color.green);
    t.checkExpect(board1.getTopLeftColor(), Color.green);
  }

  // test the flood method
  void testFlood(Tester t) {
    reset();
    // flood color must not equal topLeft color
    t.checkExpect(board1.board.get(0).get(0).color, Color.yellow);
    t.checkExpect(board1.board.get(0).get(1).color, Color.yellow);
    board1.flood(Color.green);
    t.checkExpect(board1.board.get(0).get(1).color, Color.yellow);
  }

  // test the all same color method
  void testAllSameColor(Tester t) {
    reset();
    t.checkExpect(board1.allSameColor(), false);
    t.checkExpect(board2.allSameColor(), true);
  }

  // test the getRandomColor method
  void testGetRandomColor(Tester t) {
    reset();
    t.checkOneOf(board1.getRandomColor(), board1.getRandomColor(),
        Color.red, Color.orange, Color.yellow, Color.blue, Color.green);
  }

  // test the floodNeighbor method
  void testFloodNeighbor(Tester t) {
    reset();
    t.checkExpect(board1.board.get(0).get(0).color, Color.yellow);
    board1.board.get(0).get(0).floodNeighbor(board1.board.get(0).get(1), Color.green);
    t.checkExpect(board1.board.get(0).get(1).color, Color.yellow);
  }

  // test the setNeighbors method
  void testSetNeighbors(Tester t) {
    reset();
    Cell topLeft = board2.board.get(0).get(0);
    t.checkExpect(topLeft.right, null);
    t.checkExpect(topLeft.top, null);
    t.checkExpect(topLeft.left, null);
    t.checkExpect(topLeft.bottom, null);
    topLeft.setNeighbors(board2.board);
    t.checkExpect(topLeft.right, board2.board.get(1).get(0));
    t.checkExpect(topLeft.top, null);
    t.checkExpect(topLeft.left, null);
    t.checkExpect(topLeft.bottom, board2.board.get(0).get(1));
  }

  // test the getImage method
  void testGetImage(Tester t) {
    reset();
    Cell topLeft = board1.board.get(0).get(0);
    t.checkExpect(topLeft.getImage(), new RectangleImage(20, 20, "solid", Color.YELLOW));
  }

  // test the addToScene method
  void testAddToScene(Tester t) {
    reset();
    Cell topLeft = board1.board.get(0).get(0);
    Cell bottomRight = board1.board.get(board1.size - 1).get(board1.size - 1);
    Cell bottomLeft = board1.board.get(0).get(board1.size - 1);
    Cell topRight = board1.board.get(board1.size - 1).get(0);

    WorldScene testScene = world.getEmptyScene();
    WorldScene referenceScene = world.getEmptyScene();

    topLeft.addToScene(testScene);
    referenceScene.placeImageXY(new RectangleImage(20, 20, "solid", Color.YELLOW),
        Cell.SIZE / 2, Cell.SIZE / 2);
    t.checkExpect(testScene, referenceScene);

    bottomRight.addToScene(testScene);
    referenceScene.placeImageXY(new RectangleImage(20, 20, "solid", Color.RED),
        (board1.size * Cell.SIZE) + (Cell.SIZE / 2),
        (board1.size * Cell.SIZE) + (Cell.SIZE / 2));
    t.checkExpect(testScene, referenceScene);

    bottomLeft.addToScene(testScene);
    referenceScene.placeImageXY(new RectangleImage(20, 20, "solid", Color.RED),
        Cell.SIZE / 2, (board1.size * Cell.SIZE) + (Cell.SIZE / 2));
    t.checkExpect(testScene, referenceScene);

    topRight.addToScene(testScene);
    referenceScene.placeImageXY(new RectangleImage(20, 20, "solid", Color.BLUE),
        (board1.size * Cell.SIZE) + (Cell.SIZE / 2), Cell.SIZE / 2);
    t.checkExpect(testScene, referenceScene);
  }

  // test the makeScene method
  void testMakeScene(Tester t) {
    reset();
  }

  public static void main(String[] argv) {

    // run the game
    FloodItWorld w = new FloodItWorld();
    w.bigBang(FloodItWorld.WINDOW_WIDTH, FloodItWorld.WINDOW_HEIGHT, 0.05);
  }
}
