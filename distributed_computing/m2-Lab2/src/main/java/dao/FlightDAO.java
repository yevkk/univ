package dao;

import entities.Flight;

import java.util.List;
import java.util.ResourceBundle;

public class FlightDAO implements BaseDAO<Flight> {
    private final ResourceBundle queryBundle = ResourceBundle.getBundle("query");

    @Override
    public List<Flight> findAll() {
        return null;
    }

    @Override
    public Flight find(int id) {
        return null;
    }

    @Override
    public boolean create(Flight entity) {
        return false;
    }

    @Override
    public boolean update(Flight entity) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
