import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * [Mouse].java
 * The class that keeps track of all mouse interactions
 * implements MouseListener
 * @author Ethan Zhang
 * 2021/1/25
 */
public class Mouse implements MouseListener {
  private static int cursorX, cursorY;
  private static boolean holding;
  private SingleLinkedList<Point> fillPoints = new SingleLinkedList<Point>();
  private boolean pressed;
  private boolean artist;
  
  @Override
  public void mousePressed(MouseEvent e) {
    if (artist) {
      cursorX = e.getX() - 8;
      cursorY = e.getY() - 30;
      
      for (int i = 0; i < DrawingPanel.getButtons().size(); i++) {
        if (DrawingPanel.getButtons().get(i).getHitbox().contains(cursorX, cursorY)) {
          DrawingPanel.getButtons().get(i).execute();
          pressed = true;
          break; //For efficiency
        }
      }
      
      if (!pressed) {
        holding = true;
        if (DrawingPanel.getCurrentTool() == 1) {
          DrawingPanel.getStrokes().add(new Stroke(DrawingPanel.getCurrentColour(), DrawingPanel.getCurrentSize()));
        } else if (DrawingPanel.getCurrentTool() == 2) {
          DrawingPanel.getStrokes().add(new Stroke(Color.WHITE, DrawingPanel.getCurrentSize()));
        }
      }
      
      pressed = false;
    }
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    if (artist) {
      holding = false;
      try {
        ClientInterface.getOutput().writeInt(1);
        ClientInterface.getOutput().writeObject(DrawingPanel.getStrokes());
        ClientInterface.getOutput().writeInt(-1);
      } catch (Exception ex) {
        System.out.println("Error writing and sending drawing...");
      }
    }
  }
  
  @Override
  public void mouseClicked(MouseEvent e) {}
  
  @Override
  public void mouseExited(MouseEvent e) {}
  
  @Override
  public void mouseEntered(MouseEvent e) {}
  
  /**
   * trackCursorX
   * Accurately tracks the x coordinate of the cursor constantly
   * @return int, the x coordinate of the cursor
   */
  public int trackCursorX() {
    return (int)MouseInfo.getPointerInfo().getLocation().getX() - 8 - ClientInterface.getDrawX();
  }
  
  /**
   * trackCursorY
   * Accurately tracks the y coordinate of the cursor constantly
   * @return int, the y coordinate of the cursor
   */
  public int trackCursorY() {
    return (int)MouseInfo.getPointerInfo().getLocation().getY() - 30 - ClientInterface.getDrawY();
  }
  
  /**
   * isHolding
   * Returns if the mouse is being held down or not
   * @return boolean, true when mouse is held down and false otherwise
   */
  public boolean isHolding() {
    return holding;
  }
  
  /**
   * setArtist
   * Enables or disables drawing privileges
   * @param artist, the boolean that enables or disables drawing privileges
   */
  public void setArtist(boolean artist) {
    this.artist = artist;
  }
}