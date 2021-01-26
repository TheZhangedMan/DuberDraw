import java.awt.Rectangle;
import java.awt.Graphics;

/**
 * [UndoButton].java
 * A custom button that undoes the last stroke drawn by the user
 * extends Button
 * @author Ethan Zhang
 * 2021/1/25
 */
public class UndoButton extends Button {
  /**
   * Creates an undo button at coordinates x and y with the specified hitbox
   * @param x, the integer representing x coordinate of the top left corner of the button
   * @param y, the integer representing y coordinate of the top left corner of the button
   * @param hitbox, the Rectangle representing the hitbox of the button
   * @param name, the String representing name of the button and its corresponding sprite file
   */
  public UndoButton(int x, int y, Rectangle hitbox, String name) {
    super(x, y, hitbox, name);
  }
  
  @Override
  public void execute() {
    if (DrawingPanel.getStrokes().size() != 0) {
      DrawingPanel.getStrokes().remove(DrawingPanel.getStrokes().get(DrawingPanel.getStrokes().size() - 1));
    }
  }
}