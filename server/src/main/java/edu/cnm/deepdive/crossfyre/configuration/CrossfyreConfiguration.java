package edu.cnm.deepdive.crossfyre.configuration;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "crossfyre")
public class CrossfyreConfiguration {

  /**
   * Message polling properties.
   */
  private final Polling polling;

  @ConstructorBinding
  public CrossfyreConfiguration(Polling polling) {
    this.polling = polling;
  }

  /**
   * Get message polling properties.
   */
  public Polling getPolling() {
    return polling;
  }

  @SuppressWarnings("ClassCanBeRecord")
  public static class Polling {

    /**
     * Polling timeout, with a default unit of milliseconds. After timeout, a response with an empty
     * array of messages is returned.
     */
    private final Duration timeout;
    /**
     * Polling interval, with a default unit of milliseconds. When polling for messages, a simple
     * query is executed at the end of each interval, to check for new messages in a given channel.
     */
    private final Duration interval;
    /**
     * Size (number of threads) in executor service thread pool used for polling.
     */
    private final int poolSize;

    @ConstructorBinding
    public Polling(Duration timeout, Duration interval, int poolSize) {
      this.timeout = timeout;
      this.interval = interval;
      this.poolSize = poolSize;
    }

    /**
     * Get polling timeout.
     */
    public Duration getTimeout() {
      return timeout;
    }

    /**
     * Get polling interval.
     */
    public Duration getInterval() {
      return interval;
    }

    /**
     * Get polling thread pool size.
     */
    public int getPoolSize() {
      return poolSize;
    }

  }

}
