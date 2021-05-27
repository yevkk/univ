package connect.dao;

import entity.Entity;

import java.util.List;

public interface InsertFindDAO<E extends Entity> {
    public List<E> findAll();

    public E find(int id);

    public boolean create(E entity);
}
