package dao;

import entites.Entity;

import java.util.List;

interface BaseDAO<T extends Entity> {
    public List<T> findAll();
    public T find(int id);
    public boolean create(T entity);
    public boolean update(T entity);
    public boolean delete(int id);
}
