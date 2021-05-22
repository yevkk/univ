package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entites.Airline;
import entites.Flight;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Client implements AutoCloseable {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public Client() throws IOException {
        this.socket = new Socket("localhost", 8030);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            Logger.getLogger(Client.class.getName()).warning(e.getMessage());
        }
    }

    public List<Airline> findAllAirlines() throws IOException {
        out.println("airline");
        out.println("findAll");
        return new Gson().fromJson(in.readLine(), new TypeToken<ArrayList<Airline>>(){}.getType());
    }

    public Airline findAirline(int id) throws IOException {
        out.println("airline");
        out.println("find");
        out.println(id);
        return new Gson().fromJson(in.readLine(), Airline.class);
    }

    public boolean createAirline(Airline airline) throws IOException {
        out.println("airline");
        out.println("create");
        out.println(new Gson().toJson(airline));
        return "true".equals(in.readLine());
    }

    public boolean updateAirline(Airline airline) throws IOException {
        out.println("airline");
        out.println("update");
        out.println(new Gson().toJson(airline));
        return "true".equals(in.readLine());
    }

    public boolean deleteAirline(int id) throws IOException {
        out.println("airline");
        out.println("delete");
        out.println(id);
        return "true".equals(in.readLine());
    }

    public List<Flight> findAllFlights() throws IOException {
        out.println("flight");
        out.println("findAll");
        return new Gson().fromJson(in.readLine(), new TypeToken<ArrayList<Flight>>(){}.getType());
    }

    public Flight findFlight(int id) throws IOException {
        out.println("flight");
        out.println("find");
        out.println(id);
        return new Gson().fromJson(in.readLine(), Flight.class);
    }

    public boolean createFlight(Flight flight) throws IOException {
        out.println("flight");
        out.println("create");
        out.println(new Gson().toJson(flight));
        return "true".equals(in.readLine());
    }

    public boolean updateFlight(Flight flight) throws IOException {
        out.println("flight");
        out.println("update");
        out.println(new Gson().toJson(flight));
        return "true".equals(in.readLine());
    }

    public boolean deleteFlight(int id) throws IOException {
        out.println("flight");
        out.println("delete");
        out.println(id);
        return "true".equals(in.readLine());
    }

}
