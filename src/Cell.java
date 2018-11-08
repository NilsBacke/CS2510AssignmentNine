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
  int i; // the indices of the cell in the board 2D arraylist
  int j;
  Color color;
  boolean flooded;
  boolean visited;

  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  // creates a new Cell object
  Cell(int x, int y, int i, int j, Color color) {
    this.x = x;
    this.y = y;
    this.i = i;
    this.j = j;
    this.color = color;
    this.flooded = false;
    this.visited = false;
    this.left = null;
    this.top = null;
    this.right = null;
    this.bottom = null;
  }

  // EFFECT: floods each of the cell's neighbors
  void flood(Color prevColor) {
    floodNeighbor(left, prevColor);
    floodNeighbor(top, prevColor);
    floodNeighbor(right, prevColor);
    floodNeighbor(bottom, prevColor);
  }

  // EFFECT: possibly change neighbor flooded boolean or color,
  // as well as having them flood their neighbors
  void floodNeighbor(Cell neighbor, Color prevColor) {
    // is neighbor not an edge and not yet visited?
    if ((neighbor != null) && (!neighbor.visited)) {
      neighbor.visited = true;
      if (neighbor.color.equals(prevColor)) {
        if (neighbor.flooded) {
          neighbor.color = color;
          neighbor.flood(prevColor);
        } else {
          neighbor.flooded = true;
        }
      } else if (neighbor.flooded) {
        neighbor.flood(prevColor);
      }
    }
  }

  // EFFECT: initializes all of the neighbors of this cell with the given board
  void setNeighbors(ArrayList<ArrayList<Cell>> board) {
    if (this.i == 0) {
      this.left = null;
    } else {
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

  // returns a WorldImage that represents this cell
  WorldImage getImage() {
    return new RectangleImage(SIZE, SIZE, "solid", this.color);
  }

  // EFFECT: adds this cell's WorldImage to the given scene
  void addToScene(WorldScene scene) {
    scene.placeImageXY(this.getImage(), this.x + SIZE / 2, this.y + SIZE / 2);
  }

}
