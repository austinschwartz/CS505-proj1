import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.*;

public class Benchmark {
  public enum Scheme { COARSE, HOH, OPTIMISTIC, LISTSET };

  private int threads, updateRatios, initialListSize, duration;
  private Scheme schemeType;

  public void performBenchmark(){
    IntSet set = null;
    
    if (schemeType == Scheme.COARSE)
      set = new CoarseListSet();
    if (schemeType == Scheme.HOH)
      set = new HandOverHandListSet();
    if (schemeType == Scheme.OPTIMISTIC)
      set = new OptimisticListSet();
    if (schemeType == Scheme.LISTSET)
      set = new ListSet();
    try {
      go(set); 
    } catch (Exception e) {
      System.out.println("Benchmark broke");
    }
  }

  public int getRandomNumber() {
    return (int) Math.ceil(Math.random() * 
        (2 * initialListSize));
  }

  public class BenchTask implements Runnable {
    public int thread, updateRatios, duration;
    public IntSet list;
    public BenchTask(IntSet list, int thread, int updateRatios, int duration) {
      this.list = list;
      this.thread = thread;
      this.updateRatios = updateRatios;
      this.duration = duration;
    }
    public void run() {
      while ((System.nanoTime() - startTime)/1e6 < duration) {
        if (random.nextInt(100) <= updateRatios) {
          if (random.nextBoolean()) {
            boolean x = list.insert(getRandomNumber());
            if (x) numTrue++;
            else numFalse++;
            numInserts++;
          } else {
            boolean x = list.remove(getRandomNumber());
            if (x) numTrue++;
            else numFalse++;
            numRemoves++;
          }
        } else {
          boolean x = list.contain(getRandomNumber());
          if (x) numTrue++;
          else numFalse++;
          numContains++;
        }
      }
    }
  }

  public static long startTime = 0;
  public static long numInserts, numRemoves, numContains, numTrue, numFalse;
  private final Random random = new Random();

  public void go(IntSet list) throws InterruptedException {
    numInserts = 0;
    numRemoves = 0;
    numContains = 0;

    for (int i = 0; i < initialListSize; i++) {
      list.insert(getRandomNumber());
    }

    System.out.println("Test started for: " + list.getClass() + " with the following parameters:");
    System.out.printf("Threads: %d\nUpdate Ratio: %d\nInitial size: %d\nDuration: %d\n",
                      threads, updateRatios, initialListSize, duration);
    System.out.println("-----------------");
    ExecutorService threadPool = Executors.newFixedThreadPool(threads);
    startTime = System.nanoTime();

    for (int j = 0; j < threads; j++) {
      threadPool.execute(new BenchTask(list, j, updateRatios, duration));
    }

    threadPool.shutdown();
    threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

    System.out.printf("Inserts : %d\n", numInserts);
    System.out.printf("Removes : %d\n", numRemoves);
    System.out.printf("Contains: %d\n", numContains);
    System.out.printf("Returned true: %d\n", numTrue);
    System.out.printf("Returned false: %d\n", numFalse);
    System.out.printf("op/s: %d\n", (numTrue + numFalse) * 1000 / duration);
  }

  public Benchmark(int t, int u, int l, int ms, String scheme) {
    this.threads = t;
    this.updateRatios = u;
    this.initialListSize = l;
    this.duration = ms;
    switch (scheme) {
      case "coarse":
        schemeType = Scheme.COARSE;
        break;
      case "hoh":
        schemeType = Scheme.HOH;
        break;
      case "optimistic":
        schemeType = Scheme.OPTIMISTIC;
        break;
      case "listset":
        schemeType  = Scheme.LISTSET;
        break;
    }
  }
}
