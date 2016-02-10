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

  public void go(IntSet list) throws InterruptedException {
    for (int i = 0; i < initialListSize; i++) {
      list.insert(getRandomNumber());
    }
    System.out.println("Test started for: " + list.getClass());
    long averageTime = 0;
    for (int i = 0; i < 5; i++) {

      long startTime = System.nanoTime();
      ExecutorService threadPool = Executors.newFixedThreadPool(threads);

      for (int j = 0; j < threads; j++) {
        threadPool.execute(new Runnable() {
          @SuppressWarnings("unused")
          @Override
          public void run() {
            for (int i = 0; i < 500; i++) {
              Integer rand = getRandomNumber();
              list.insert(rand);
            }
          }
        });
      }

      threadPool.shutdown();
      threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

      long entTime = System.nanoTime();
      long totalTime = (entTime - startTime) / 1000000L;
      averageTime += totalTime;
      System.out.println("2500K entried added/retrieved in " + totalTime + " ms");
    }
    System.out.println("For " + list.getClass() + " the average time is " + averageTime / 5 + " ms\n");
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
