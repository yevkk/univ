package dao;

import entities.Airline;

import java.util.List;
import java.util.ResourceBundle;

public class AirlineDAO implements BaseDAO<Airline> {
    private final ResourceBundle queryBundle = ResourceBundle.getBundle("query");

    @Override
    public List<Airline> findAll() {
        return null;
    }

    @Override
    public Airline find(int id) {
        return null;
    }

    @Override
    public boolean create(Airline entity) {
        return false;
    }

    @Override
    public boolean update(Airline entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
