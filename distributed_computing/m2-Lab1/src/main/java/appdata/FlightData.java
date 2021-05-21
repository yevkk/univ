package appdata;

import entities.Flight;

import java.util.ArrayList;
import java.util.List;

public class FlightData implements DataStorage<Flight> {
    private final List<Flight> list;
    private int maxIndex;

    public FlightData(int initIndex, List<Flight> list) {
        this.list = list;
        maxIndex = initIndex;
    }

    public FlightData() {
        this(0, new ArrayList<>());
    }

    @Override
    public void add(Flight entity) {
        maxIndex++;
        entity.setId(maxIndex);
        list.add(entity);
    }

    @Override
    public void update(Flight entity) {
        for (var flight : list) {
            if (flight.getId() == entity.getId()) {
                flight.setAirlineId(entity.getAirlineId());
                flight.setDepartureAirport(entity.getDepartureAirport());
                flight.setArrivalAirport(entity.getArrivalAirport());
                flight.setPrice(entity.getPrice());
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
    public Flight get(int id) {
        for (var flight : list) {
            if (flight.getId() == id) {
                return flight;
            }
        }
        return null;
    }

    @Override
    public List<Flight> getAll() {
        return list;
    }
}
