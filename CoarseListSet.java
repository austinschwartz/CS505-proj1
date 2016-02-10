import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class CoarseListSet implements IntSet {
  private Node head;
  private Lock lock = new ReentrantLock();

  public static class Node {
    public Node next;
    public int key;
    public Node(int key) {
      this.key = key;
      this.next = null;
    }
  }

  public CoarseListSet() {
    this.head = new Node(Integer.MIN_VALUE);
    this.head.next = new Node(Integer.MAX_VALUE);
  }

  public boolean insert(int x){
    lock.lock();
    Node pred=head;
    try {
      Node curr = head.next;
      while (curr.key < x){
        pred = curr;
        curr = pred.next;
      }
      if (curr.key==x){
        return false;
      } else {
        Node node = new Node(x);
        node.next=curr;
        pred.next=node;
        return true;
      }
    } finally{
      lock.unlock();
    }
  }

  public boolean remove(int x) {
    lock.lock();
    Node pred=head;
    try {
      Node curr = head.next;
      while (curr.key < x){
        pred = curr;
        curr = pred.next;
      }
      if (curr.key==x){
        pred.next = curr.next;
        return true;
      } else {
        return false;
      }
    } finally{
      lock.unlock();
    }
  }

  public boolean contain(int x) {
    lock.lock();
    Node pred=head;
    try {
      Node curr = head.next;
      while (curr.key < x){
        pred = curr;
        curr = pred.next;
      }
      if (curr.key==x){
        return true;
      } else {
        return false;
      }
    } finally{
      lock.unlock();
    }
  }
}
