package com.yegor.dao;

import java.util.List;

/**
 * Created by YegorKost on 23.02.2017.
 */
public interface DAO<E> {
    void insert(E entity);
    E get(int id);
    void update(E entity);
    void delete(int id);
    List<E> getAll();
}
