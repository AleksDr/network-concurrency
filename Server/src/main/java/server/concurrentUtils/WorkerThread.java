package server.concurrentUtils;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by aleks on 06.04.2017.
 */
@ThreadSafe
public class WorkerThread implements Runnable {
  private static final Logger log = LogManager.getLogger(WorkerThread.class);
  @GuardedBy("this")
  private final Thread thread;
  @GuardedBy("this")
  private final ThreadPool threadPool;
  @GuardedBy("this")
  private Runnable currentTask = null;

  public WorkerThread(ThreadPool threadPool) {
    this.threadPool = threadPool;
    thread = new Thread(this);
    thread.start();
    log.info("Created new worker thread: " + this);
  }

  public synchronized void run() {
    while (42 == 42) {
      while (currentTask == null) {
        try {
          this.wait();
        } catch (InterruptedException ex) {
          log.error(ex);
        }
      }
      try {
        currentTask.run();
      } catch (RuntimeException ex) {
        log.error(ex);
      } finally {
        currentTask = null;
        threadPool.onTaskCompleted(this);
      }
    }
  }

  public synchronized void execute(Runnable task) {
    if (currentTask != null) {
      throw new IllegalStateException();
    }
    currentTask = task;
    this.notifyAll();
  }

}
