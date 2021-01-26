import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Point;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * [ClientInterface].java
 * The interface for the client
 * @author Ethan Zhang
 * 2021/1/25
 */
public class ClientInterface {
  //General components
  private static JFrame drawWindow;
  private Mouse mouse;
  private Socket socket;
  private static ObjectOutputStream output;
  private static ObjectInputStream input;
  private boolean running = true;
  private String username;
  
  //Login window components
  private JFrame loginWindow;
  private JPanel loginPanel;
  private JLabel loginLabel;
  private JTextField loginField;
  private JButton loginButton;
  
  //Chat window components
  private JFrame chatWindow;
  private JPanel chatPanel;
  private JTextArea chatArea;
  private JScrollPane chatScroll;
  private JTextField chatField;
  private JButton chatButton;
  
  /**
   * requestLogin
   * Connects to server and creates a JFrame to request a username from the client
   */
  public void requestLogin() {
    connect("127.0.0.1", 5000);
    loginWindow = new JFrame("DuberDraw - Login");
    loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loginPanel = new JPanel();
    loginPanel.setLayout(new GridLayout(3, 0));
    loginLabel = new JLabel("Username:");
    loginField = new JTextField();
    loginField.addActionListener(new LoginListener());
    loginButton = new JButton("Enter");
    loginButton.addActionListener(new LoginListener());
    loginPanel.add(loginLabel);
    loginPanel.add(loginField);
    loginPanel.add(loginButton);
    loginWindow.add(loginPanel);
    loginWindow.setSize(300, 100);
    loginWindow.setVisible(true);
  }
  
  /**
   * launch
   * Creates a drawing pane and chat for the client and connects starts reading from server
   */
  public void launch() {
    drawWindow = new JFrame("DuberDraw - " + username);
    drawWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    drawWindow.setSize(800, 800);
    mouse = new Mouse();
    drawWindow.addMouseListener(mouse);
    drawWindow.add(new DrawingPanel(mouse));
    drawWindow.requestFocusInWindow();
    drawWindow.setVisible(true);
    
    chatWindow = new JFrame("DuberDraw - Chat");
    chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    chatWindow.setSize(400, 800);
    chatPanel = new JPanel();
    chatPanel.setLayout(new GridLayout(0, 2));
    chatArea = new JTextArea();
    chatArea.setEditable(false);
    chatScroll = new JScrollPane(chatArea);
    chatField = new JTextField();
    chatField.addActionListener(new SendListener());
    chatButton = new JButton("Send");
    chatButton.addActionListener(new SendListener());
    chatPanel.add(chatField);
    chatPanel.add(chatButton);
    chatWindow.add(BorderLayout.CENTER, chatScroll);
    chatWindow.add(BorderLayout.SOUTH, chatPanel);
    chatWindow.setVisible(true);
    
    Thread a = new Thread(new Runnable() { 
      public void run() { 
        animate(); 
      }
    });
    a.start();
    
    Thread r = new Thread(new Runnable() {
      public void run() {
        read();
      }
    });
    r.start();
  }
  
  /**
   * connect
   * Attempts to connect to the server and creates the socket and streams
   * @param ip, a String that corresponds to the IP address to be connected to
   * @param port, an integer value that represents the port number of the communication endpoint
   */
  private Socket connect(String ip, int port) { 
    System.out.println("Attempting to make a connection...");
    
    try {
      socket = new Socket("127.0.0.1", 5000); //Attempting socket connection, which will wait until connection is made
      output = new ObjectOutputStream(socket.getOutputStream());
      input = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      System.out.println("Connection to server failed...");
    }
    
    System.out.println("Connection successfully established.");
    return socket;
  }
  
  /**
   * read
   * Starts a loop that waits for server input and then interprets received data
   */
  private void read() {
    while (running) {
      try {
        while (input.readInt() != -1) {
          Object object = input.readObject();
          if (object instanceof SingleLinkedList) {
            //Unchecked cast; the list passed between server and client will always carry Strokes
            DrawingPanel.setStrokes((SingleLinkedList<Stroke>)object);
          } else if (object instanceof String) {
            chatArea.append((String)object + "\n");
          } else if (object instanceof Command) {
            Command c = (Command)object;
            if (c.getCommand().equals("onartist")) {
              mouse.setArtist(true);
            } else if (c.getCommand().equals("offartist")) {
              mouse.setArtist(false);
            } else if (c.getCommand().equals("clr")) {
              if (DrawingPanel.getStrokes().size() != 0) {
                DrawingPanel.getStrokes().clear();
              }
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Failed to receive item from the server...");
        running = false;
      }
    }
    
    try {
      input.close();
      output.close();
      socket.close();
      System.exit(0);
    } catch (Exception e) {
      System.out.println("Failed to close socket...");
    }
  }
  
  /**
   * animate
   * Starts a loop that updates the JPanel at a fixed delay
   */
  public void animate() {
    while (true) {
      try {
        Thread.sleep(5);
      } catch (Exception e) {
        System.out.println("Thread error...");
      }
      drawWindow.repaint();
    }
  }
  
  /**
   * getDrawX
   * Returns the x coordinate of the drawing pane
   * @return int, the x coordinate of the drawing pane
   */
  public static int getDrawX() {
    return drawWindow.getLocationOnScreen().x;
  }
  
  /**
   * getDrawY
   * Returns the y coordinate of the drawing pane
   * @return int, the y coordinate of the drawing pane
   */
  public static int getDrawY() {
    return drawWindow.getLocationOnScreen().y;
  }
  
  /**
   * getOutput
   * Returns the output stream of the client
   * @return ObjectOutputStream, the output stream of the client
   */
  public static ObjectOutputStream getOutput() {
    return output;
  }
  
  /**
   * getInput
   * Returns the input stream of the client
   * @return ObjectInputStream, the input stream of the client
   */
  public static ObjectInputStream getInput() {
    return input;
  }
  
  /**
   * [LoginListener].java
   * The listener for client login
   * implements ActionListener
   * @author Ethan Zhang
   * 2021/1/25
   */
  private class LoginListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (loginField.getText().trim().equals("")) { //Prevents input of only whitespaces
        loginField.setText("");
      } else { //Sends message to the user selected in the whisper box
        try {
          username = loginField.getText();
          output.writeInt(1); //Notifies server to start reading
          output.writeObject(loginField.getText());
          output.writeInt(-1); //Notifies server to stop reading
          output.flush();
        } catch (IOException ex) {
          System.out.println("Error sending message...");
        }
      }
      loginField.setText("");
      loginWindow.dispose();
      launch();
    }
  }
  
  /**
   * [SendListener].java
   * The listener for client chat input
   * implements ActionListener
   * @author Ethan Zhang
   * 2021/1/25
   */
  private class SendListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (chatField.getText().trim().equals("")) { //Prevents input of only whitespaces
        chatField.setText("");
      } else { //Sends message to the user selected in the whisper box
        try {
          output.writeInt(1); //Notifies server to start reading
          output.writeObject(chatField.getText());
          output.writeInt(-1); //Notifies server to stop reading
          output.flush();
          chatField.setText("");
        } catch (IOException ex) {
          System.out.println("Error sending message...");
        }
      }
    }
  }
}