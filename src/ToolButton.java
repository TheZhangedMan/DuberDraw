import java.awt.Rectangle;
import java.awt.Graphics;

/**
 * [ToolButton].java
 * A custom button that changes the current tool used by the client
 * extends Button
 * @author Ethan Zhang
 * 2021/1/25
 */
public class ToolButton extends Button {
  private int tool;
  
  /**
   * Creates a tool button at coordinates x and y with the specified hitbox and tool
   * @param x, the integer representing x coordinate of the top left corner of the button
   * @param y, the integer representing y coordinate of the top left corner of the button
   * @param hitbox, the Rectangle representing the hitbox of the button
   * @param name, the String representing name of the button and its corresponding sprite file
   * @param tool, the integer representing the tool
   */
  public ToolButton(int x, int y, Rectangle hitbox, String name, int tool) {
    super(x, y, hitbox, name);
    this.tool = tool;
  }
  
  /**
   * getTool
   * Returns the integer of the tool held by the button
   * @return int, the tool held by the button
   */
  public int getTool() {
    return tool;
  }
  
  @Override
  public void execute() {
    DrawingPanel.setCurrentTool(tool);
  }
}