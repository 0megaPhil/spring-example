package com.arryved.core;

import com.google.cloud.spanner.Key;
import com.google.cloud.spanner.KeySet;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.Struct;
import com.google.cloud.spring.data.spanner.core.SpannerOperations;
import com.google.cloud.spring.data.spanner.core.SpannerPageableQueryOptions;
import com.google.cloud.spring.data.spanner.core.SpannerQueryOptions;
import com.google.cloud.spring.data.spanner.core.SpannerReadOptions;
import com.google.cloud.spring.data.spanner.core.SpannerTemplate;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public abstract class AbstractSpanner<E> implements SpannerOperations {
  protected final SpannerTemplate spannerTemplate;
  protected final Class<E> entityClass;

  public AbstractSpanner(
      SpannerTemplate spannerTemplate,
      Class<E> entityClass) {
    this.spannerTemplate = spannerTemplate;
    this.entityClass = entityClass;
  }

  @Override
  public long executeDmlStatement(Statement statement) {
    return spannerTemplate.executeDmlStatement(statement);
  }

  @Override
  public long executePartitionedDmlStatement(Statement statement) {
    return spannerTemplate.executePartitionedDmlStatement(statement);
  }

  public E read(Key key) {
    return spannerTemplate.read(entityClass, key);
  }

  @Override
  public <T> T read(Class<T> entityClass, Key key) {
    return spannerTemplate.read(entityClass, key);
  }

  public <T> boolean existsById(Key key) {
    return spannerTemplate.existsById(entityClass, key);
  }

  @Override
  public <T> boolean existsById(Class<T> entityClass, Key key) {
    return spannerTemplate.existsById(entityClass, key);
  }

  public E read(Key key, SpannerReadOptions options) {
    return spannerTemplate.read(entityClass, key,options);
  }

  @Override
  public <T> T read(Class<T> entityClass, Key key, SpannerReadOptions options) {
    return spannerTemplate.read(entityClass, key,options);
  }

  public List<E> read(KeySet keys, SpannerReadOptions options) throws IllegalArgumentException {
    return spannerTemplate.read(entityClass, keys, options);
  }

  @Override
  public <T> List<T> read(Class<T> entityClass, KeySet keys, SpannerReadOptions options) throws IllegalArgumentException {
    return spannerTemplate.read(entityClass, keys, options);
  }

  public List<E> read(KeySet keys) {
    return spannerTemplate.read(entityClass, keys);
  }

  @Override
  public <T> List<T> read(Class<T> entityClass, KeySet keys) {
    return spannerTemplate.read(entityClass, keys);
  }

  @Override
  public <A> List<A> query(Function<Struct, A> rowFunc, Statement statement, SpannerQueryOptions options) {
    return spannerTemplate.query(rowFunc, statement, options);
  }

  public List<E> query(Statement statement, SpannerQueryOptions options) {
    return spannerTemplate.query(entityClass, statement, options);
  }

  @Override
  public <T> List<T> query(Class<T> entityClass, Statement statement, SpannerQueryOptions options) {
    return spannerTemplate.query(entityClass, statement, options);
  }

  public List<E> readAll(SpannerReadOptions options) {
    return spannerTemplate.readAll(entityClass, options);
  }

  @Override
  public <T> List<T> readAll(Class<T> entityClass, SpannerReadOptions options) {
    return spannerTemplate.readAll(entityClass, options);
  }

  public List<E> readAll() {
    return spannerTemplate.readAll(entityClass);
  }

  @Override
  public <T> List<T> readAll(Class<T> entityClass) {
    return spannerTemplate.readAll(entityClass);
  }

  public List<E> queryAll(SpannerPageableQueryOptions options) {
    return spannerTemplate.queryAll(entityClass, options);
  }

  @Override
  public <T> List<T> queryAll(Class<T> entityClass, SpannerPageableQueryOptions options) {
    return spannerTemplate.queryAll(entityClass, options);
  }

  public void delete(Key key) {
    spannerTemplate.delete(entityClass, key);
  }

  @Override
  public <T> void delete(Class<T> entityClass, Key key) {
    spannerTemplate.delete(entityClass, key);
  }

  @Override
  public void delete(Object object) {
    spannerTemplate.delete(object);
  }

  @Override
  public void deleteAll(Iterable<?> objects) {
    spannerTemplate.deleteAll(objects);
  }

  public void delete(KeySet keys) {
    spannerTemplate.delete(entityClass, keys);
  }

  @Override
  public <T> void delete(Class<T> entityClass, KeySet keys) {
    spannerTemplate.delete(entityClass, keys);
  }

  @Override
  public void insert(Object object) {
    spannerTemplate.insert(object);
  }

  @Override
  public void insertAll(Iterable<?> objects) {
    spannerTemplate.insert(objects);
  }

  @Override
  public void update(Object object) {
    spannerTemplate.update(object);
  }

  @Override
  public void updateAll(Iterable<?> objects) {
    spannerTemplate.updateAll(objects);
  }

  @Override
  public void update(Object object, String... includeProperties) {
    spannerTemplate.update(object, includeProperties);
  }

  @Override
  public void update(Object object, Set<String> includeProperties) {
    spannerTemplate.update(object, includeProperties);
  }

  @Override
  public void upsert(Object object) {
    spannerTemplate.upsert(object);
  }

  @Override
  public void upsertAll(Iterable<?> objects) {
    spannerTemplate.upsertAll(objects);
  }

  @Override
  public void upsert(Object object, String... includeProperties) {
    spannerTemplate.upsert(object, includeProperties);
  }

  @Override
  public void upsert(Object object, Set<String> includeProperties) {
    spannerTemplate.upsert(object, includeProperties);
  }

  public long count() {
    return spannerTemplate.count(entityClass);
  }

  @Override
  public <T> long count(Class<T> entityClass) {
    return spannerTemplate.count(entityClass);
  }

  @Override
  public <T> T performReadWriteTransaction(Function<SpannerTemplate, T> operations) {
    return spannerTemplate.performReadWriteTransaction(operations);
  }

  @Override
  public <T> T performReadOnlyTransaction(Function<SpannerTemplate, T> operations, SpannerReadOptions readOptions) {
    return spannerTemplate.performReadOnlyTransaction(operations, readOptions);
  }
}
