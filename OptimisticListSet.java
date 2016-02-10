import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class OptimisticListSet implements IntSet {
  private Node head;

  public static class Node {
    public Node next;
    public int key;
    public Lock lock;
    public Node(int key) {
      this.key = key;
      this.next = null;
      this.lock = new ReentrantLock();
    }
    public void lock()   {this.lock.lock();}
    public void unlock() {this.lock.unlock();}
  }

  public OptimisticListSet() {
    this.head = new Node(Integer.MIN_VALUE);
    this.head.next = new Node(Integer.MAX_VALUE);
  }

  private boolean validate(Node pred, Node curr) {
    Node node = head;
      while (node.key <= pred.key) {
        if (node == pred)
          return (pred.next == curr);
        node=node.next;
      }
      return false;
   }

  public boolean insert(int x) {
    while (true) {
      Node pred = head;
      Node curr = pred.next;
      while (curr.key < x) {
        pred = curr;
        curr = curr.next;
      }
      pred.lock();
      curr.lock();
      try {
        if (validate(pred,curr)) {
          if (curr.key == x) {
            return false;
          }
          Node node = new Node(x);
          node.next = curr;
          pred.next = node;
          return true;
        }
      } finally{
        pred.unlock();
        curr.unlock();
      }
    } 
  }

  public boolean remove(int x) {
    while (true) {
      Node pred = head;
      Node curr = pred.next;
      while (curr.key < x) {
        pred = curr;
        curr = curr.next;
      }
      pred.lock();
      curr.lock();
      try {
        if (validate(pred, curr)) {
          if (curr.key == x) {
            pred.next = curr.next;
            return true;
          }
          return false;
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    } 
  }

  public boolean contain(int x) {
    while (true) {
      Node pred = head;
      Node curr = pred.next;
      while (curr.key < x) {
        pred = curr;
        curr = curr.next;
      }
      pred.lock();
      curr.lock();
      try {
        if (validate(pred,curr)) {
          return (curr.key == x);
        }
      } finally {
        pred.unlock();
        curr.unlock();
      }
    }
  } 
}
