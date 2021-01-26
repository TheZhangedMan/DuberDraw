import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Point;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * [DrawingPanel].java
 * The JPanel responsible for drawing
 * extends JPanel
 * @author Ethan Zhang
 * 2021/1/25
 */
public class DrawingPanel extends JPanel {
  private Mouse mouse;
  private static SingleLinkedList<Stroke> strokes = new SingleLinkedList<Stroke>();
  private static SingleLinkedList<Button> buttons = new SingleLinkedList<Button>();
  private static Color currentColour = Color.BLACK;
  private static int currentTool = 1; //1 = Brush, 2 = Eraser
  private static float currentSize = 5f;
  private Color[] colours = {new Color(255, 255, 255), new Color(0, 0, 0), new Color(193, 193, 193),
    new Color(76, 76, 76), new Color(239, 19, 11), new Color(116, 11, 7),
    new Color(255, 133, 0), new Color(17, 100, 76), new Color(255, 228, 0),
    new Color(232, 162, 0), new Color(0, 204, 0), new Color(0, 85, 16),
    new Color(0, 178, 255), new Color(0, 86, 158), new Color(35, 31, 211),
    new Color(14, 8, 101), new Color(163, 0, 186), new Color(85, 0, 105),
    new Color(211, 124, 170), new Color(167, 85, 116), new Color(160, 82, 45),
    new Color(99, 48, 13)};
  private String[] colourNames = {"white", "black", "lightGrey", "darkGrey", "red", "darkRed", "orange", "darkOrange", 
    "yellow", "darkYellow", "green", "darkGreen", "lightBlue", "blue", "darkBlue", 
    "navy", "purple", "darkPurple", "pink", "darkPink", "brown", "darkBrown"};
  
  /**
   * Creates a drawing pane with the assigned MouseListener
   * @param mouse, the Mouse for listening to mouse inputs
   */
  public DrawingPanel(Mouse mouse) {
    this.mouse = mouse;
    this.setBackground(Color.WHITE);
    
    //Create all buttons
    for (int i = 0; i < colours.length; i++) {
      if (i % 2 != 0) {
        buttons.add(new ColourButton((i * 12) + 88, 725, new Rectangle((i * 12) + 88, 725, 25, 25), 
                                     colourNames[i], colours[i]));
      } else {
        buttons.add(new ColourButton((i * 12) + 100, 700, new Rectangle((i * 12) + 100, 700, 25, 25), 
                                     colourNames[i], colours[i]));
      }
    }
    buttons.add(new ToolButton(375, 700, new Rectangle(375, 700, 50, 50), "draw", 1));
    buttons.add(new ToolButton(425, 700, new Rectangle(425, 700, 50, 50), "erase", 2));
    buttons.add(new UndoButton(475, 700, new Rectangle(475, 700, 50, 50), "undo"));
    buttons.add(new SizeButton(525, 700, new Rectangle(525, 700, 50, 50), "small", 5f));
    buttons.add(new SizeButton(575, 700, new Rectangle(575, 700, 50, 50), "medium", 10f));
    buttons.add(new SizeButton(625, 700, new Rectangle(625, 700, 50, 50), "large", 15f));
    buttons.add(new ClearButton(675, 700, new Rectangle(675, 700, 50, 50), "trash"));
  }
  
  /**
   * paintComponent
   * Draws all visual components onto the JPanel
   * @param g, the Graphics component of the JPanel used to draw
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    setDoubleBuffered(true);
    Graphics2D g2d = (Graphics2D)g;
    
    if (mouse.isHolding()) {
      strokes.get(strokes.size() - 1).add(new Point(mouse.trackCursorX(), mouse.trackCursorY()));
    }
    
    for (int i = 0; i < strokes.size(); i++) {
      SingleLinkedList<Point> currentPoints = strokes.get(i).getPoints();
      g2d.setColor(strokes.get(i).getColour());
      g2d.setStroke(new BasicStroke(strokes.get(i).getSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
      for (int j = 1; j < currentPoints.size(); j++) {
        g2d.draw(new Line2D.Float(currentPoints.get(j - 1), currentPoints.get(j)));
      }
    }
    
    g.setColor(currentColour);
    g.fillRect(25, 700, 50, 50);
    
    for (int i = 0; i < buttons.size(); i++) {
      buttons.get(i).draw(g);
    }
  }
  
  /**
   * getStrokes
   * Returns the list of strokes
   * @return SingleLinkedList<Stroke>, the list of strokes
   */
  public static SingleLinkedList<Stroke> getStrokes() {
    return strokes;
  }
  
  /**
   * setStrokes
   * Sets the list of strokes to the given list of strokes
   * @param _strokes, the new list of strokes
   */
  public static void setStrokes(SingleLinkedList<Stroke> _strokes) {
    strokes = _strokes;
  }
  
  /**
   * getButtons
   * Returns the list of buttons
   * @return SingleLinkedList<Button>, the list of buttons
   */
  public static SingleLinkedList<Button> getButtons() {
    return buttons;
  }
  
  /**
   * getCurrentColour
   * Returns the current brush colour
   * @return Color, the current brush colour
   */
  public static Color getCurrentColour() {
    return currentColour;
  }
  
  /**
   * setCurrentColour
   * Sets the current brush colour to the given brush colour
   * @param _currentColour, the new brush colour
   */
  public static void setCurrentColour(Color _currentColour) {
    currentColour = _currentColour;
  }
  
  /**
   * getCurrentTool
   * Returns the current tool being used by the client
   * @return int, the current tool integer being used by the client
   */
  public static int getCurrentTool() {
    return currentTool;
  }
  
  /**
   * setCurrentTool
   * Sets the current tool to the given tool integer
   * @param float, the new tool integer
   */
  public static void setCurrentTool(int _currentTool) {
    currentTool = _currentTool;
  }
  
  /**
   * getCurrentSize
   * Returns the current brush size
   * @return currentSize, the current brush size
   */
  public static float getCurrentSize() {
    return currentSize;
  }
  
  /**
   * setCurrentSize
   * Sets the current brush size to the given brush size
   * @param _currentSize, the new brush size
   */
  public static void setCurrentSize(float _currentSize) {
    currentSize = _currentSize;
  }
}