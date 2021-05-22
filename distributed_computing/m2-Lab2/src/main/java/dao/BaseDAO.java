package dao;

import entities.Entity;

import java.util.List;
import java.util.ResourceBundle;

interface BaseDAO<T extends Entity> {
    public List<T> findAll();
    public T find(int id);
    public boolean create(T entity);
    public boolean update(T entity);
    public boolean delete(int id);
}
