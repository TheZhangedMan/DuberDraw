/**
 * [PriorityQueue].java
 * A priority queue that contains nodes that are doubly linked to each other
 * @author Ethan Zhang
 * 2020/1/25
 */
public class PriorityQueue<E> { 
  private QueueNode<E> head;
  private QueueNode<E> tail;
  
  /**
   * enqueue
   * Inserts an item into the queue based on priority
   * @param item, the item to be added
   * @param priority, the integer representing priority (greater values are higher priority)
   */
  public void enqueue(E item, int priority) {
    if (head == null) {
      head = new QueueNode<E>(item, priority, null, null);
      return;
    } else if (tail == null) {
      if (priority > head.getPriority()) {
        tail = new QueueNode<E>(head.getItem(), head.getPriority(), null, head);
        head = new QueueNode<E>(item, priority, tail, null);
      } else {
        tail = new QueueNode<E>(item, priority, null, head);
        head.setNext(tail);
      }
      return;
    }
    
    if (priority > head.getPriority()) {
      QueueNode<E> node = new QueueNode<E>(head.getItem(), head.getPriority(), head.getNext(), head);
      head.setItem(item);
      head.setPriority(priority);
      head.getNext().setPrevious(node);
      head.setNext(node);
      return;
    }
    
    QueueNode<E> temp = head.getNext();
    
    while (temp.getNext() != null) {
      if (priority > temp.getPriority()) {
        QueueNode<E> node = new QueueNode<E>(item, priority, temp, temp.getPrevious());
        temp.getPrevious().setNext(node);
        temp.setPrevious(node);
        return;
      }
      temp = temp.getNext();
    }
    
    if (priority <= tail.getPriority()) {
      QueueNode<E> node = new QueueNode<E>(tail.getItem(), tail.getPriority(), tail, tail.getPrevious());
      tail.setItem(item);
      tail.setPriority(priority);
      tail.getPrevious().setNext(node);
      tail.setPrevious(node);
    } else {
      QueueNode<E> node = new QueueNode<E>(item, priority, tail, tail.getPrevious());
      tail.getPrevious().setNext(node);
      tail.setPrevious(node);
    }
  }
  
  /**
   * dequeue
   * Removes the node, or head, at the front of the queue
   * @return QueueNode<E>, the removed node
   */
  public QueueNode<E> dequeue() {
    QueueNode<E> dequeued = head;
    QueueNode<E> temp;
    
    if (head.getNext() != null) {
      head.getNext().setPrevious(null);
      if (head.getNext() == tail) {
        temp = tail;
        tail = null;
        head = head.getNext();
      } else {
        head = head.getNext();
      }
    } else {
      head = null;
    }
    
    return dequeued;
  }
  
  /**
   * isEmpty
   * Returns whether the queue is empty or not
   * @return boolean, true when the queue is empty and false otherwise
   */
  public boolean isEmpty() {
    if (head == null) {
      return true;
    } else {
      return false;
    }
  }
}