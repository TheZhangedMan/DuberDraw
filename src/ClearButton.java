import java.awt.Rectangle;
import java.awt.Graphics;

/**
 * [ClearButton].java
 * A custom button that clears the drawing made on a JPanel
 * extends Button
 * @author Ethan Zhang
 * 2021/1/25
 */
public class ClearButton extends Button {
  
  /**
   * Creates a clear button at coordinates x and y with the specified hitbox
   * @param x, the integer representing x coordinate of the top left corner of the button
   * @param y, the integer representing y coordinate of the top left corner of the button
   * @param hitbox, the Rectangle representing the hitbox of the button
   * @param name, the String representing name of the button and its corresponding sprite file
   */
  public ClearButton(int x, int y, Rectangle hitbox, String name) {
    super(x, y, hitbox, name);
  }
  
  @Override
  public void execute() {
    DrawingPanel.getStrokes().clear();
  }
}