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
public class Channel {
  private static final Logger log = LogManager.getLogger(Channel.class);
  @GuardedBy("this")
  private final LinkedList<Runnable> linkedList = new LinkedList<>();
  @GuardedBy("this")
  private final int maxCounter;

  public Channel(int max) {
    maxCounter = max;
    log.info("Created new channel: " + this);
  }

  public void put(Runnable elem) {
    synchronized (this) {
      while (maxCounter <= linkedList.size()) {
        try {
          this.wait();
        } catch (InterruptedException ex) {
          log.error(ex);
        }
      }
      linkedList.addLast(elem);
      this.notifyAll();
    }

  }

  Runnable take() {
    synchronized (this) {
      while (linkedList.isEmpty()) {
        try {
          this.wait();
        } catch (InterruptedException ex) {
          log.error(ex);
        }
      }
      this.notifyAll();
      return linkedList.removeFirst();
    }
  }

  public synchronized int getSize() {
    return linkedList.size();
  }
}
