import java.io.Serializable;

/**
 * [Command].java
 * An object that is passed from the server to clients to force an action
 * implements Serializable
 * @author Ethan Zhang
 * 2021/1/25
 */
public class Command implements Serializable {
  //Possible commands include "onartist", "offartist", and "clr"
  //"onartist" and "offartist" will enable and disable drawing privileges respectively
  //"clr" will clear a client's drawing pane
  private String command;
  
  /**
   * Creates a command with the specified command
   * @param command, the String representing the command
   */
  public Command(String command) {
    this.command = command;
  }
  
  /**
   * getCommand
   * Returns the command held by the command
   * @return String, the command
   */
  public String getCommand() {
    return command;
  }
}