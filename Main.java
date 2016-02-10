import java.util.*;

public class Main {
  public static void main(String[] args) {
    if (Test(new OptimisticListSet())) System.out.println("Optimistic good");;
    if (Test(new HandOverHandListSet())) System.out.println("Hand over hand good");
    if (Test(new ListSet())) System.out.println("List good");;
    if (Test(new CoarseListSet())) System.out.println("Coarse good");;
    Benchmark(8, 100, 10000, 3000);
  }
  // Benchmark(threads, updateratio, initial size, duration in ms, test)
  public static void Benchmark(int t, int u, int l, int d) {
    (new Benchmark(t, u, l, d, "listset")).performBenchmark();
    (new Benchmark(t, u, l, d, "hoh")).performBenchmark();
    (new Benchmark(t, u, l, d, "coarse")).performBenchmark();
    (new Benchmark(t, u, l, d, "optimistic")).performBenchmark();
  }


  public static boolean Test(IntSet set) {
    boolean good = true;
    set.insert(50);
    set.insert(238);
    set.insert(-1923);
    good |= set.contain(-1923);
    set.remove(-1923);
    good |= !set.contain(-1923);
    good |= set.contain(50);
    good |= !set.contain(51);
    good |= set.contain(238);
    return good;
  }
}
