import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * [Button].java
 * A custom button made for JPanel
 * @author Ethan Zhang
 * 2021/1/25
 */
public abstract class Button {
  private int x, y;
  private Rectangle hitbox;
  private BufferedImage sprite;
  private String name;
  
  /**
   * Creates a button at coordinates x and y with the specified hitbox
   * @param x, the integer representing x coordinate of the top left corner of the button
   * @param y, the integer representing y coordinate of the top left corner of the button
   * @param hitbox, the Rectangle representing the hitbox of the button
   * @param name, the String representing name of the button and its corresponding sprite file
   */
  public Button(int x, int y, Rectangle hitbox, String name) {
    this.x = x;
    this.y = y;
    this.hitbox = hitbox;
    this.name = name;
    loadSprite();
  }
  
  /**
   * getX
   * Returns x coordinate of the button
   * @return int, the x coordinate of the button
   */
  public int getX() {
    return x;
  }
  
  /**
   * getY
   * Returns y coordinate of the button
   * @return int, the y coordinate of the button
   */
  public int getY() {
    return y;
  }
  
  /**
   * getHitbox
   * Returns the hitbox of the button
   * @return Rectangle, the hitbox of the button
   */
  public Rectangle getHitbox() {
    return hitbox;
  }
  
  /**
   * getSprite
   * Returns the loaded sprite of the button
   * @return BufferedImage, the sprite of the button
   */
  public BufferedImage getSprite() {
    return sprite;
  }
  
  /**
   * getName
   * Returns name of the button
   * @return String, the name of the button
   */
  public String getName() {
    return name;
  }
  
  /**
   * loadSprite
   * Attempts to load a sprite based on the button's given name
   */
  private void loadSprite() {
    try {
      sprite = ImageIO.read(new File(name + ".png"));
    } catch (Exception e) {
      System.out.println("Error loading button sprite...");
    }
  }
  
  /**
   * draw
   * Draws the sprite of the button
   * @param g, the Graphics component of the JPanel used to draw
   */
  public void draw(Graphics g) {
    g.drawImage(sprite, x, y, null);
  }
  
  /**
   * execute
   * Executes the button's set action
   */
  public abstract void execute();
}