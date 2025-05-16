package com.airportapp.dao;
import java.util.List;
public interface GenericDao<E, K> {
    E findById(K id);
    List<E> findAll();
    E save(E e);
    E update(E e);
    void delete(E e);
}