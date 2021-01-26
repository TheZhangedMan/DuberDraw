import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * [GameServer].java
 * The server for DuberDraw
 * @author Ethan Zhang
 * 2021/1/25
 */
public class GameServer {
  private ServerSocket serverSock; //Server socket for connection
  private static boolean running = true;  //Controls if the server is accepting clients
  private Random random = new Random();
  private SingleLinkedList<String> prompts = new SingleLinkedList<String>();
  private static SingleLinkedList<ConnectionHandler> connections = new SingleLinkedList<ConnectionHandler>();
  private static PriorityQueue<ConnectionHandler> artistQueue = new PriorityQueue<ConnectionHandler>();
  private static String word;
  private static ConnectionHandler artist;
  private static boolean started;
  
  /** main
    * @param args, a String array of arguments from the command line
    */
  public static void main(String[] args) { 
    new GameServer().go();
  }
  
  /** go
    * Starts the server
    */
  public void go() {
    //Load word prompts
    try {
      File file = new File("prompts.txt");
      Scanner reader = new Scanner(file);
      while (reader.hasNext()) {
        prompts.add(reader.next());
      }
      reader.close();
    } catch (IOException e) {
      System.out.println("Error loading prompts...");
    }
    
    Socket client = null; //Holds the client connection
    try {
      serverSock = new ServerSocket(5000);  //Assigns an port to the server
      while (running) { //Loop to accept multiple clients
        client = serverSock.accept();  //Wait for connection
        System.out.println("Client connected!");
        ConnectionHandler ch = new ConnectionHandler(client);
        connections.add(ch);
        Thread t = new Thread(ch); //Create a thread for the new client and pass in the socket
        t.start(); //Start the new thread
      }
    } catch (Exception e) { 
      System.out.println("Error accepting connection...");
      //Close all and quit
      try {
        client.close();
      } catch (Exception e1) { 
        System.out.println("Failed to close socket...");
      }
      System.exit(-1);
    }
  }
  
  /**
   * broadcast
   * Sends the specified object to all connected clients
   * @param object, the Object that is to be broadcasted
   */
  public synchronized void broadcast(Object object) {
    for (int i = 0; i < connections.size(); i++) {
      try {
        connections.get(i).send(object);
      } catch (Exception e) {
        System.out.println("Error broadcasting item...");
      }
    }
  }
  
  /**
   * leaderboard
   * Sorts via bubble sort and sends the leaderboard to all connected clients
   */
  public synchronized void leaderboard() {
    ConnectionHandler[] sorted = new ConnectionHandler[connections.size()];
    
    for (int i = 0; i < sorted.length; i++) {
      sorted[i] = connections.get(i);
    }
    
    ConnectionHandler temp;
    boolean swapped;
    
    do {
      swapped = false;
      for (int i = 0; i < (sorted.length - 1); i++) {
        if (sorted[i].getPoints() < sorted[i + 1].getPoints()) {
          temp = sorted[i];
          sorted[i] = sorted[i + 1];
          sorted[i + 1] = temp;
          swapped = true;
        }
      }
    } while (swapped);
    
    broadcast("#~~~~~~LEADERBOARD~~~~~~#");
    
    for (int i = 0; i < sorted.length; i++) {
      broadcast(sorted[i].getUsername() + ": " + sorted[i].getPoints());
    }
  }
  
  /**
   * [ConnectionHandler].java
   * The instance of a client on the server end
   * implements Runnable
   */
  public class ConnectionHandler implements Runnable { 
    private ObjectOutputStream output; //Assign ObjectOutputStream to network stream
    private ObjectInputStream input; //Stream for network input
    private Socket client;  //Keeps track of the client socket
    private boolean online;
    private boolean running;
    private String username;
    private int points = 0;
    
    /**
     * Creates a connection handler for a client
     * @param s, the Socket belonging to the client's connection
     */    
    ConnectionHandler(Socket s) { 
      this.client = s; //Assign client to this    
      try { //Assign all connections to client
        this.output = new ObjectOutputStream(client.getOutputStream());
        this.input = new ObjectInputStream(client.getInputStream());
      } catch(IOException e) {
        System.out.println("Unable to assign OOS and OIS...");       
      }            
      running = true;
    }
    
    /**
     * send
     * Sends an object to the assigned client
     * @param object, the Object to be sent
     */
    public synchronized void send(Object object) {
      try {
        output.writeInt(1); //Notifies client to start reading
        output.writeObject(object);
        output.writeInt(-1); //Notifies client to stop reading
        output.flush();
      } catch (Exception e) {
        System.out.println("Error sending item...");
      }
    }
    
    /**
     * getUsername
     * Returns the username of the assigned client
     * @return String, the username of the assigned client
     */
    public synchronized String getUsername() {
      return username;
    }
    
    /**
     * getPoints
     * Returns the amount of points the assigned client has earned
     * @return int, the number of points the client has earned
     */
    public synchronized int getPoints() {
      return points;
    }
    
    /* run
     * Starts a loop that waits for client input and then interprets received data
     */
    public void run() {  
      while (!online) {
        try {
          while (input.readInt() != -1) {
            username = (String)input.readObject();
            online = true;
          }
        } catch (Exception e) {
          System.out.println("User disconnected...");
          online = true;
          running = false;
        }
      }
      
      //Get a message from the client
      while (running) {  //Loop until a message is received        
        try {
          while (input.readInt() != -1) { //Check for an incoming messge
            try {
              Object object = input.readObject(); //Get a message from the client
              if (object instanceof SingleLinkedList) {
                broadcast(object);
              } else if (object instanceof String) {
                if (((String)object).equals("/start")) {
                   
                  for (int i = 0; i < connections.size(); i++) { //Fill queue to start game
                    artistQueue.enqueue(connections.get(i), 0);
                  }
                  
                  artist = artistQueue.dequeue().getItem();
                  word = prompts.get(random.nextInt(prompts.size()));
                  artist.send("You are drawing: " + word);
                  artist.send(new Command("onartist"));
                  broadcast(new Command("clr"));
                  broadcast(artist.getUsername() + " is drawing!");
                  broadcast("The word is " + word.length() + " letters long.");
                } else if (((String)object).equalsIgnoreCase(word)) { //Check for correct guess
                  if (this != artist) {
                    broadcast(username + " guessed the word correctly!");
                    points += 1;
                    artist.send(new Command("offartist"));
                    leaderboard();
                    
                    if (artistQueue.isEmpty()) { //Refill queue if empty
                      for (int i = 0; i < connections.size(); i++) {
                        artistQueue.enqueue(connections.get(i), 0);
                      }
                    }
                    
                    artist = artistQueue.dequeue().getItem();
                    word = prompts.get(random.nextInt(prompts.size()));
                    artist.send("You are drawing: " + word);
                    artist.send(new Command("onartist"));
                    broadcast(new Command("clr"));
                    broadcast(artist.getUsername() + " is drawing!");
                    broadcast("The word is " + word.length() + " letters long.");
                  }
                } else {
                  broadcast(username + ": " + (String)object);
                }
              }
            } catch (Exception e) {
              System.out.println("Error reading and sending message...");
            }
          }
        } catch (IOException e) {
          System.out.println("User disconnected...");
          running = false;
        }
      }
      
      //Close the socket
      try {
        connections.remove(this);
        input.close();
        output.close();
        client.close();
      } catch (Exception e) { 
        System.out.println("Failed to close socket...");
      }
    }
  }
}