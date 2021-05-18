package appdata;

import entities.Airline;

import java.util.ArrayList;
import java.util.List;

public class AirlineData implements DataStorage<Airline> {
    private final List<Airline> list;
    private int maxIndex;

    public AirlineData(int initIndex, List<Airline> list) {
        this.list = list;
        maxIndex = initIndex;
    }

    public AirlineData() {
        this(0, new ArrayList<>());
    }

    @Override
    public void add(Airline entity) {
        maxIndex++;
        entity.setId(maxIndex);
        list.add(entity);
    }

    @Override
    public void update(Airline entity) {
        for (var airline : list) {
            if (airline.getId() == entity.getId()) {
                airline.setName(entity.getName());
                airline.setCountry(entity.getCountry());
                break;
            }
        }
    }

    @Override
    public void remove(int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                list.remove(i);
                break;
            }
        }
    }

    @Override
    public Airline get(int id) {
        for (var airline : list) {
            if (airline.getId() == id) {
                return airline;
            }
        }
        return null;
    }

    @Override
    public List<Airline> getAll() {
        return list;
    }
}
