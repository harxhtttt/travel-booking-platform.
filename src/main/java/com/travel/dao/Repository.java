package com.travel.dao;

import java.util.List;

/** Generic repository interface for CRUD operations. */
public interface Repository<T> {
    void save(T obj);
    T findById(int id);
    List<T> findAll();
    void update(T obj);
    void delete(int id);
}
