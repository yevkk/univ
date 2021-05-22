package server;

import com.google.gson.Gson;
import dao.AppData;
import entites.Airline;
import entites.Flight;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {
    public static void main(String[] args) {
        try (var serverSocket = new ServerSocket(8030)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                var clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            Logger.getLogger("Socket server").warning(e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;
        private final AppData appdata;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            this.appdata = new AppData();
        }

        @Override
        public void run() {
            try {
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var out = new PrintWriter(socket.getOutputStream(), true);
                while (!socket.isClosed()) {
                    var mode = in.readLine();
                    var action = in.readLine();
                    switch (mode) {
                        case "airline" -> airlineHelper(in, out, action);
                        case "flight" -> flightHelper(in, out, action);
                        default -> out.println();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                Logger.getLogger("Socket client handler").warning(e.getMessage());
            }
        }

        private void airlineHelper(BufferedReader in, PrintWriter out, String action) throws IOException, ClassNotFoundException {
            switch (action) {
                case "findAll" -> {
                    out.println(new Gson().toJson(appdata.getAirlineDAO().findAll()));
                }
                case "find" -> {
                    var id = Integer.parseInt(in.readLine());
                    out.println(new Gson().toJson(appdata.getAirlineDAO().find(id)));
                }
                case "create" -> {
                    var airline = new Gson().fromJson(in.readLine(), Airline.class);
                    out.println(appdata.getAirlineDAO().create(airline));
                }
                case "update" -> {
                    var airline = new Gson().fromJson(in.readLine(), Airline.class);
                    out.println(appdata.getAirlineDAO().update(airline));
                }
                case "delete" -> {
                    var id = Integer.parseInt(in.readLine());
                    out.println(appdata.getAirlineDAO().delete(id));
                }
                default -> out.println();
            }
        }

        private void flightHelper(BufferedReader in, PrintWriter out, String action) throws IOException, ClassNotFoundException {
            switch (action) {
               case "findAll" -> {
                    out.println(new Gson().toJson(appdata.getFlightDAO().findAll()));
                }
                case "find" -> {
                    var id = Integer.parseInt(in.readLine());
                    out.println(new Gson().toJson(appdata.getFlightDAO().find(id)));
                }
                case "create" -> {
                    var flight = new Gson().fromJson(in.readLine(), Flight.class);
                    out.println(appdata.getFlightDAO().create(flight));
                }
                case "update" -> {
                    var flight = new Gson().fromJson(in.readLine(), Flight.class);
                    out.println(appdata.getFlightDAO().update(flight));
                }
                case "delete" -> {
                    var id = Integer.parseInt(in.readLine());
                    out.println(appdata.getFlightDAO().delete(id));
                }
                default -> out.println();
            }
        }
    }
}
