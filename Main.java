import java.util.*;

public class Main {
  public static void main(String[] args) {
    int t = 4, u = 50, i = 10000, d = 3000;
    try {
      String s = "coarse";
      for (int k = 0; k < args.length - 1; k++) {
        if (args[k].charAt(0) == '-') {
          switch (args[k].charAt(1)) {
            case 't':
              t = Integer.parseInt(args[k + 1]);
              break;
            case 'u':
              u = Integer.parseInt(args[k + 1]);
              break;
            case 'i':
              i = Integer.parseInt(args[k + 1]);
              break;
            case 'd':
              d = Integer.parseInt(args[k + 1]);
              break;
            case 'b':
              s = args[k + 1];
        }
       }
      }
      (new Benchmark(t, u, i, d, s)).performBenchmark();
    } catch (Exception e) {
      System.out.println("Argument format: java Main -t 8 -d 3000 -u 100 -i 10000 -b coarse");
    }
  }
  /*
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
  */
}
