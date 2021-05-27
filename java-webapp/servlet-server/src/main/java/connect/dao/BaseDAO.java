package connect.dao;

import entity.Entity;

import java.util.List;

/**
 * Provides definitions of CRUD operations
 * @param <E> class of Entity object DAO is working with
 */
public interface BaseDAO<E extends Entity> extends InsertFindDAO<E> {
    public boolean update(E entity);

    public boolean delete(int id);
}
