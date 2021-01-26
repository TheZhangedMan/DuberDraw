import java.awt.Rectangle;
import java.awt.Graphics;

/**
 * [SizeButton].java
 * A custom button that changes the current brush size used by the client
 * extends Button
 * @author Ethan Zhang
 * 2021/1/25
 */
public class SizeButton extends Button {
  private float size;
  
  /**
   * Creates a size button at coordinates x and y with the specified hitbox and size
   * @param x, the integer representing x coordinate of the top left corner of the button
   * @param y, the integer representing y coordinate of the top left corner of the button
   * @param hitbox, the Rectangle representing the hitbox of the button
   * @param name, the String representing name of the button and its corresponding sprite file
   * @param size, the float representing the size held by the button
   */
  public SizeButton(int x, int y, Rectangle hitbox, String name, float size) {
    super(x, y, hitbox, name);
    this.size = size;
  }
  
  /**
   * getSize
   * Returns the size of the button
   * @return float, the size held by the button
   */
  public float getSize() {
    return size;
  }
  
  @Override
  public void execute() {
    DrawingPanel.setCurrentSize(size);
  }
}