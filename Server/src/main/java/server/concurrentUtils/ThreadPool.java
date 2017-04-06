package server.concurrentUtils;

import java.util.LinkedList;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by aleks on 06.04.2017.
 */
@ThreadSafe
public class ThreadPool {
  private static final Logger log = LogManager.getLogger(ThreadPool.class);
  @GuardedBy("this")
  private final LinkedList<WorkerThread> allWorkers = new LinkedList<WorkerThread>();
  @GuardedBy("this")
  private final Channel freeWorkers;
  private final int maxSize;

  public ThreadPool(int maxSize) {
    this.maxSize = maxSize;
    freeWorkers = new Channel(maxSize);
    WorkerThread workerThread = new WorkerThread(this);
    allWorkers.addLast(workerThread);
    freeWorkers.put(workerThread);
    log.info("Created new thread pool: " + this);
  }


  public void execute(Runnable task) {
    if (freeWorkers.getSize() == 0) {
      if (maxSize > allWorkers.size()) {
        WorkerThread workerThread = new WorkerThread(this);
        allWorkers.addLast(workerThread);
        freeWorkers.put((Runnable) workerThread);
      }
    }
    ((WorkerThread) freeWorkers.take()).execute(task);
  }

  public void onTaskCompleted(WorkerThread workerThread) {
    freeWorkers.put(workerThread);
  }
}
