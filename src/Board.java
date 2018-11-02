import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javalib.impworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;

public class Board {
  static final ArrayList<String> colors = new ArrayList<String>(
      Arrays.asList("red", "orange", "yellow", "blue", "green"));
  
  ArrayList<ArrayList<Cell>> board;
  int size;
  Random rand;

  Board(int size) {
    board = new ArrayList<ArrayList<Cell>>(size);
    this.size = size;
    rand = new Random();

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
  
  Cell getClickedCell(Posn pos) {
    return board.get(pos.x / Cell.SIZE).get(pos.y / Cell.SIZE);
  }
  
  void setTopLeftColor(String color) {
    board.get(0).get(0).color = color;
  }
  
  String getTopLeftColor() {
    return board.get(0).get(0).color;
  }
  
  void flood(String prevColor) {
    board.get(0).get(0).flood(prevColor);
  }
  
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

  String getRandomColor() {
    return colors.get(rand.nextInt(colors.size()));
  }
}
