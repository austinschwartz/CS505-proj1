public interface IntSet {
  // These methods need to be thread−safe (i.e.well-synchronized).
  public boolean insert(int x);
  public boolean remove(int x);
  public boolean contain(int x);
}
