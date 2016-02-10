import java.util.concurrent.*;

public class ListSet implements IntSet {
  private static final Object PRESENT = new Object();
  private static ConcurrentSkipListMap<Integer, Object> map;

  public ListSet() {
    this.map = new ConcurrentSkipListMap<Integer, Object>();
  }

  public boolean insert(int x) {
    return map.put(x, PRESENT) == null;
  }

  public boolean remove(int x) {
    return map.remove(x) == PRESENT;
  }

  public boolean contain(int x) {
    return map.containsKey(x);
  }

  @Override
  public String toString() {
    return map.keySet().toString();
  }
}




