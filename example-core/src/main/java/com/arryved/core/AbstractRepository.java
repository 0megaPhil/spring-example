package com.arryved.core;

import com.google.cloud.spring.data.spanner.repository.SpannerRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public abstract class AbstractRepository<T, I> implements PagingAndSortingRepository<T, I>, CrudRepository<T, I> {

  public SpannerRepository<T, I> repository;

  public AbstractRepository(SpannerRepository<T, I> repository) {
    this.repository = repository;
  }

  @Override
  public <S extends T> S save(S entity) {
    return repository.save(entity);
  }

  @Override
  public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
    return repository.saveAll(entities);
  }

  @Override
  public Optional<T> findById(I i) {
    return repository.findById(i);
  }

  @Override
  public boolean existsById(I i) {
    return repository.existsById(i);
  }

  @Override
  public Iterable<T> findAll() {
    return repository.findAll();
  }

  @Override
  public Iterable<T> findAllById(Iterable<I> is) {
    return repository.findAllById(is);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public void deleteById(I i) {
    repository.deleteById(i);
  }

  @Override
  public void delete(T entity) {
    repository.delete(entity);
  }

  @Override
  public void deleteAllById(Iterable<? extends I> is) {
    repository.deleteAllById(is);
  }

  @Override
  public void deleteAll(Iterable<? extends T> entities) {
    repository.deleteAll(entities);
  }

  @Override
  public void deleteAll() {
    repository.deleteAll();
  }

  @Override
  public Iterable<T> findAll(Sort sort) {
    return repository.findAll(sort);
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }
}