package com.arryved.example;

import com.arryved.core.AbstractListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class ExampleEchoListener extends AbstractListener {
  
  public ExampleEchoListener(ScheduledExecutorService scheduledExecutorService) {
    super();
    scheduledExecutorService
        .scheduleAtFixedRate(this::logStats, 1, 1, TimeUnit.SECONDS);
  }
  
  @Override
  public void logStats() {
    super.logStats();
  }
}
