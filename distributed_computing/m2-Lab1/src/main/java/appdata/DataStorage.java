package appdata;

import entities.Entity;

import java.util.List;

public interface DataStorage<E extends Entity> {
    public void add(E entity);

    public void update(E entity);

    public void remove(int id);

    public E get(int id);

    public List<E> getAll();
}
