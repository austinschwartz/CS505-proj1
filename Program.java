public class Program implements Runnable
{
    public static void main(String[] args)
    {
        Program p = new Program();
        p.init();
    }

    public void init()
    {
        // Create a second thread and run it.
        Thread thr2 = new Thread(this);
        System.out.println("Running " +
                thr2.getName() + "...");
        thr2.run();

        // Create a third thread and run it.
        Thread thr3 = new Thread(this);
        System.out.println("Running " +
                thr3.getName() + "...");
        thr3.run();

        // Display the number of active threads.  This
        // count includes the main thread.
        System.out.println("Active threads after run = " +
            Thread.activeCount());
    }

    // Implements Runnable.run()
    public void run()
    {
      System.out.println("cocks");
      for (long i = 0; i < 5; i++)
      {
          System.out.println(counter + " " + Thread.currentThread());
          counter++;
      }
      System.out.println(Thread.currentThread().getName() +
            " has finished executing.");
    }

    private int counter = 0;
}
