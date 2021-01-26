/**
 * [QueueNode].java
 * A node for a priority queue
 * @author Ethan Zhang
 * 2020/1/25
 */
public class QueueNode<T> { 
  private T item;
  private int priority;
  private QueueNode<T> next;
  private QueueNode<T> previous;
  
  /**
   * Creates a node with reference to other nodes
   * @param item, the item carried by the node
   * @param priority, the integer representing priority
   * @param next, the next node
   * @param previous, the previous node
   */
  QueueNode(T item, int priority, QueueNode<T> next, QueueNode<T> previous) {
    this.item = item;
    this.priority = priority;
    this.next = next;
    this.previous = previous;
  }
  
  /**
   * getItem
   * Returns the item held by the node
   * @return T, the item held by the node
   */
  public T getItem() {
    return item;
  }
  
  /**
   * setItem
   * Sets the item held by the node to the given item
   * @param item, the new item
   */
  public void setItem(T item) {
    this.item = item;
  }
  
  /**
   * getPriority
   * Returns the priority of the node
   * @return int, the priority of the node
   */
  public int getPriority() {
    return priority;
  }
  
  /**
   * setPriority
   * Sets the priority of the node to the given priority
   * @param priority, the new priority for the node
   */
  public void setPriority(int priority) {
    this.priority = priority;
  }
  
  /**
   * getNext
   * Returns the next node
   * @return QueueNode<T>, the next node
   */
  public QueueNode<T> getNext() {
    return next;
  }
  
  /**
   * setNext
   * Sets the next node to the given node
   * @param next, the new next node
   */
  public void setNext(QueueNode<T> next) {
    this.next = next;
  }
  
  /**
   * getPrevious
   * Returns the previous node
   * @return QueueNode<T>, the previous node
   */
  public QueueNode<T> getPrevious() {
    return previous;
  }
  
  /**
   * setPrevious
   * Sets the previous node to the given node
   * @param previous, the new previous node
   */
  public void setPrevious(QueueNode<T> previous) {
    this.previous = previous;
  }
}