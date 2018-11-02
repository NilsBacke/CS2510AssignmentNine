import java.util.ArrayList;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

public class FloodItWorld extends World {

  static final int BOARD_SIZE = 22;
  static final int MAX_TURNS = (int) (BOARD_SIZE * 1.5);
  int turns;
  String prevColor;
  Board board;

  static final int WINDOW_WIDTH = BOARD_SIZE * Cell.SIZE;
  static final int WINDOW_HEIGHT = (BOARD_SIZE + 1) * Cell.SIZE;

  FloodItWorld() {
    turns = 0;
    board = new Board(BOARD_SIZE);
    prevColor = board.getTopLeftColor();
  }

  // produces the image of this world
  @Override
  public WorldScene makeScene() {
    return this.board.render(this);
  }

  public void onKeyEvent(String k) {
    if (k.equals("r")) {
      board = new Board(BOARD_SIZE);
      turns = 0;
    }
  }

  public void onMouseClicked(Posn pos) {
    if (pos.y < WINDOW_HEIGHT - Cell.SIZE) {

      Cell clickedCell = board.getClickedCell(pos);
      prevColor = board.getTopLeftColor();
      board.setTopLeftColor(clickedCell.color);
      if (!prevColor.equals(clickedCell.color)) {
        turns++;
        board.flood(prevColor);
      }

    }
  }
  
  public void onTick() {
    board.flood(prevColor);
  }

  public WorldEnd worldEnds() {
    // check if user wins
    if (board.allSameColor()) {
      // user wins
      return new WorldEnd(true, this.lastScene("YOU WIN"));
    }
    else if (turns == MAX_TURNS) {
      return new WorldEnd(true, this.lastScene("YOU LOSE"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  // produces the last image of this world by adding text to the image
  public WorldScene lastScene(String s) {
    WorldScene scene = this.makeScene();
    scene.placeImageXY(new TextImage(s, Color.red), WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
    return scene;
  }
}

class FloodItExamples {
  public static void main(String[] argv) {

    // run the game
    FloodItWorld w = new FloodItWorld();
    w.bigBang(FloodItWorld.WINDOW_WIDTH, FloodItWorld.WINDOW_HEIGHT, 0.1);
  }
}
