import java.awt.Point;
import java.awt.Color;
import java.io.Serializable;

/**
 * [Stroke].java
 * A class that represents a paintbrush stroke
 * implements Serializable
 * @author Ethan Zhang
 * 2021/1/25
 */
public class Stroke implements Serializable {
  private Color colour;
  private float size;
  private SingleLinkedList<Point> points = new SingleLinkedList<Point>();
  
  /**
   * Creates a stroke with a specified colour and brush size
   * @param colour, the colour of the stroke
   * @param size, the float representing the size of the brush
   */
  public Stroke(Color colour, float size) {
    this.colour = colour;
    this.size = size;
  }
  
  /**
   * getColour
   * Returns the colour of the stroke
   * @return Color, the colour of the stroke
   */
  public Color getColour() {
    return colour;
  }
  
  /**
   * getSize
   * Returns the size of the stroke
   * @return float, the size of the stroke
   */
  public float getSize() {
    return size;
  }
  
  /**
   * getPoints
   * Returns the list of points that constitutes the stroke
   * @return SingleLinkedList<Point>, the list of points that constitutes the stroke
   */
  public SingleLinkedList<Point> getPoints() {
    return points;
  }
  
  /**
   * add
   * Adds a new point to the point list
   * @param Point, the point to be added
   */
  public void add(Point point) {
    points.add(point);
  }
}