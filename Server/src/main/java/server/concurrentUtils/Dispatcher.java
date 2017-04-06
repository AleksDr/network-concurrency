package server.concurrentUtils;

import net.jcip.annotations.ThreadSafe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by aleks on 06.04.2017.
 */
@ThreadSafe
public class Dispatcher implements Runnable {
  private static final Logger log = LogManager.getLogger(Dispatcher.class);
  private final Channel channel;
  private final ThreadPool threadPool;

  public Dispatcher(Channel channel, ThreadPool threadPool) {
    this.channel = channel;
    this.threadPool = threadPool;
    log.info("Created new dispatcher: " + this);
  }

  public void run() {
    while (true) {
      threadPool.execute(channel.take());
    }
  }

  public void start() {
    new Thread(this).start();
  }
}
