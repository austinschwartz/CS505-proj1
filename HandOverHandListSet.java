import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class HandOverHandListSet implements IntSet {
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

  public HandOverHandListSet() {
    this.head = new Node(Integer.MIN_VALUE);
    this.head.next = new Node(Integer.MAX_VALUE);
  }

  public boolean insert(int x) {
    head.lock();
    Node pred = head;
    try {
      Node curr = head.next;
      curr.lock();
      try {
        while (curr.key < x){
          pred.unlock();
          pred = curr;
          curr = pred.next;
          curr.lock();
        }
        if (curr.key == x) {
          return false;
        } else {
          Node node = new Node(x);
          node.next = curr;
          pred.next = node;
          return true;
        } 
      } finally {
        curr.unlock();
      }
    } finally {
      pred.unlock();
    }
  }

  public boolean remove(int x) {
    head.lock();
    Node pred = head;
    try {
      Node curr = pred.next;
      curr.lock();
      try {
        while (curr.key < x) {
          pred.unlock();
          pred = curr;
          curr = pred.next;
          curr.lock();
        }
        if (curr.key == x) {
          pred.next = curr.next;
          return true;
        } else {
          return false;
        }
      } finally {
       curr.unlock();
      }
    } finally {
      pred.unlock();
    } 
  }

  public boolean contain(int x) {
    head.lock();
    Node pred = head;
     try {
       Node curr = head.next;
       curr.lock();
       try {
         while (curr.key < x) {
           pred.unlock();
           pred = curr;
           curr = pred.next;
           curr.lock();
         }
         return (curr.key == x);
       } finally {
         curr.unlock();
       }
     } finally {
       pred.unlock();
     }
  }
}
