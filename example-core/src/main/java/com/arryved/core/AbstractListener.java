package com.arryved.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public abstract class AbstractListener implements Listener {
  
  private final AtomicInteger beginCounter;
  private final AtomicInteger successCounter;
  private final AtomicInteger completeCounter;
  private final AtomicInteger errorCounter;
  private final AtomicInteger previousTotal;
  private final Map<String, AtomicInteger> objectTotal;
  
  public AbstractListener() {
    this.beginCounter = new AtomicInteger(0);
    this.successCounter = new AtomicInteger(0);
    this.completeCounter = new AtomicInteger(0);
    this.errorCounter = new AtomicInteger(0);
    this.previousTotal = new AtomicInteger(0);
    this.objectTotal = new ConcurrentHashMap<>();
  }
  
  @Override
  public <T> void begin(T obj) {
    beginCounter.incrementAndGet();
    objectTotal.computeIfAbsent(obj.getClass().getSimpleName(),
        k -> new AtomicInteger(0)).incrementAndGet();
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
  
  public void logStats() {
    System.out.println("rate: " + (completeCounter.get() - previousTotal.get()) +
        " begin: " + beginCounter.get() + " success: " + successCounter.get()
        + " error: " + errorCounter.get() + " complete: " + completeCounter.get());
    previousTotal.set(completeCounter.get());
    System.out.println(objectTotal.entrySet().stream()
        .map(entry -> entry.getKey() + ": " + entry.getValue() + " ").collect(Collectors.joining()));
  }
}
