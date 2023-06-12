package com.arryved.core;

public interface Listener {
  <T> void begin(T obj);
  <T> void success(T obj);
  <T> void error(T obj);
  <T> void complete(T obj);
}
