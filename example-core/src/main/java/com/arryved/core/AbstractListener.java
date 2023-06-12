package com.arryved.core;

import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;

@Getter
public abstract class AbstractListener implements Listener {
  private final AtomicInteger beginCounter;
  private final AtomicInteger successCounter;
  private final AtomicInteger completeCounter;
  private final AtomicInteger errorCounter;
  
  public AbstractListener() {
    this.beginCounter = new AtomicInteger(0);
    this.successCounter = new AtomicInteger(0);
    this.completeCounter = new AtomicInteger(0);
    this.errorCounter = new AtomicInteger(0);
  }
  
  @Override
  public <T> void begin(T obj) {
    beginCounter.incrementAndGet();
  }
  
  @Override
  public <T> void success(T obj) {
    successCounter.incrementAndGet();
  }
  
  @Override
  public <T> void error(T obj) {
    errorCounter.incrementAndGet();
  }
  
  @Override
  public <T> void complete(T obj) {
    completeCounter.incrementAndGet();
  }
}
