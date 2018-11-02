import java.awt.Color;
import java.util.ArrayList;
import javalib.worldimages.*;
import javalib.impworld.WorldScene;

// Represents a single square of the game area
class Cell {
  static final int SIZE = 20;
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  int i; // the indecies of the cell in the board 2D arraylist
  int j;
  String color;
  boolean flooded;

  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // creates a new Cell object
  Cell(int x, int y, int i, int j, String color) {
    this.x = x;
    this.y = y;
    this.i = i;
    this.j = j;
    this.color = color;
    this.flooded = false;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }
  
  void flood(String prevColor) {
    this.flooded = true;
    floodNeighbor(left, prevColor);
    floodNeighbor(top, prevColor);
    floodNeighbor(right, prevColor);
    floodNeighbor(bottom, prevColor);
  }
  
  void floodNeighbor(Cell neighbor, String prevColor) {
    if (neighbor != null && neighbor.color.equals(prevColor)) {
      neighbor.color = this.color;
      if (neighbor.flooded) {
        neighbor.flood(prevColor);
      } else {
        neighbor.flooded = true;
      }
    }
  }

  void setNeighbors(ArrayList<ArrayList<Cell>> board) {
    if (this.i == 0) {
      this.left = null;
    }
    else {
      this.left = board.get(i - 1).get(j);
    }

    if (this.i == board.size() - 1) {
      this.right = null;
    }
    else {
      this.right = board.get(i + 1).get(j);
    }

    if (this.j == 0) {
      this.top = null;
    }
    else {
      this.top = board.get(i).get(j - 1);
    }

    if (this.j == board.size() - 1) {
      this.bottom = null;
    }
    else {
      this.bottom = board.get(i).get(j + 1);
    }
  }

  Color getColor(String color) {
    Color ccolor = Color.RED;
    switch (color) {
    case "red":
      ccolor = Color.RED;
      break;
    case "orange":
      ccolor = Color.ORANGE;
      break;
    case "yellow":
      ccolor = Color.YELLOW;
      break;
    case "blue":
      ccolor = Color.BLUE;
      break;
    case "green":
      ccolor = Color.GREEN;
      break;
    }
    return ccolor;
  }

  WorldImage getImage() {
    return new RectangleImage(SIZE, SIZE, "solid", this.getColor(this.color));
  }

  void addToScene(WorldScene scene) {
    scene.placeImageXY(this.getImage(), this.x + SIZE / 2, this.y + SIZE / 2);
  }

}