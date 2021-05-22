package appdata;

import entities.Airline;

import java.util.ArrayList;
import java.util.List;

public class AirlineData implements DataStorage<Airline> {
    private final List<Airline> list;
    private int maxId;

    public AirlineData(int initIndex, List<Airline> list) {
        this.list = list;
        maxId = initIndex;
    }

    public AirlineData() {
        this(0, new ArrayList<>());
    }

    public void correctMaxId() {
        maxId = 0;
        for (var item : list) {
            maxId = Math.max(maxId, item.getId());
        }
    }

    @Override
    public void add(Airline entity) {
        if (entity.getId() == -1) {
            maxId++;
            entity.setId(maxId);
        } else {
            for (var item : list) {
                if (item.getId() == entity.getId()) {
                    return;
                }
            }
        }
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
