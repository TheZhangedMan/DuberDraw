import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

/**
 * [ColourButton].java
 * A custom button that changes the current brush colour used by the client
 * extends Button
 * @author Ethan Zhang
 * 2021/1/25
 */
public class ColourButton extends Button {
  private Color colour;
  
  /**
   * Creates a colour button at coordinates x and y with the specified hitbox and colour
   * @param x, the integer representing x coordinate of the top left corner of the button
   * @param y, the integer representing y coordinate of the top left corner of the button
   * @param hitbox, the Rectangle representing the hitbox of the button
   * @param name, the String representing name of the button and its corresponding sprite file
   * @param colour, the Color of the button
   */
  public ColourButton(int x, int y, Rectangle hitbox, String name, Color colour) {
    super(x, y, hitbox, name);
    this.colour = colour;
  }
  
  /**
   * getColour
   * Returns the colour held by the button
   * @return Color, the colour of the button
   */
  public Color getColour() {
    return colour;
  }
  
  @Override
  public void execute() {
    DrawingPanel.setCurrentColour(colour);
  }
}