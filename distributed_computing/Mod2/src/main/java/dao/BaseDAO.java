package dao;

import entity.Entity;

import java.util.List;

public interface BaseDAO<T extends Entity> {
    public List<T> findAll();
    public T find(int id);
    public boolean create(T entity); //ignores provided id
    public boolean update(T entity);
    public boolean delete(int id);
}
