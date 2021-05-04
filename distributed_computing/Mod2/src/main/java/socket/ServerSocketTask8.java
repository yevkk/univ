package socket;

import dao.NationDAO;
import dao.RegionDAO;
import dao.WeatherRecordDAO;
import entity.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTask8 {
    public static void main(String[] args) {
        try (var serverSocket = new ServerSocket(8030)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                try (var clientSocket = serverSocket.accept()) {
                    new Thread(new ClientHandler(clientSocket)).start();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (var in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream())); var out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
                while(!socket.isClosed()) {
                    String mode = (String) in.readObject();
                    String action = (String) in.readObject();
                    switch (mode) {
                        case "nations" -> nationsHelper(in, out, action);
                        case "regions" -> regionsHelper(in, out, action);
                        case "weather" -> weatherHelper(in, out, action);
                        default -> out.writeObject(null);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.print("");
            }
        }

        private void nationsHelper (ObjectInputStream in, ObjectOutputStream out, String action) throws IOException, ClassNotFoundException {
            var dao = new NationDAO();
            switch (action) {
                case "findAll" -> {
                    out.writeObject(dao.findAll());
                }
                case "find" -> {
                    var id = in.readInt();
                    out.writeObject(dao.find(id));
                }
                case "create" -> {
                    var nation = (Nation) in.readObject();
                    out.writeBoolean(dao.create(nation));
                }
                case "update" -> {
                    var nation = (Nation) in.readObject();
                    out.writeBoolean(dao.update(nation));
                }
                case "delete" -> {
                    var id = in.readInt();
                    out.writeBoolean(dao.delete(id));
                }
                default -> out.writeObject(null);
            }
            out.flush();
        }

        private void regionsHelper (ObjectInputStream in, ObjectOutputStream out, String action) throws IOException, ClassNotFoundException {
            var dao = new RegionDAO();
            switch (action) {
                case "findAll" -> {
                    out.writeObject(dao.findAll());
                }
                case "find" -> {
                    var id = in.readInt();
                    out.writeObject(dao.find(id));
                }
                case "create" -> {
                    var region = (Region) in.readObject();
                    out.writeBoolean(dao.create(region));
                }
                case "update" -> {
                    var region = (Region) in.readObject();
                    out.writeBoolean(dao.update(region));
                }
                case "delete" -> {
                    var id = in.readInt();
                    out.writeBoolean(dao.delete(id));
                }
                default -> out.writeObject(null);
            }
            out.flush();
        }

        private void weatherHelper (ObjectInputStream in, ObjectOutputStream out, String action) throws IOException, ClassNotFoundException {
            var dao = new WeatherRecordDAO();
            switch (action) {
                case "findAll" -> {
                    out.writeObject(dao.findAll());
                }
                case "find" -> {
                    var id = in.readInt();
                    out.writeObject(dao.find(id));
                }
                case "create" -> {
                    var weatherRecord = (WeatherRecord) in.readObject();
                    out.writeBoolean(dao.create(weatherRecord));
                }
                case "update" -> {
                    var weatherRecord = (WeatherRecord) in.readObject();
                    out.writeBoolean(dao.update(weatherRecord));
                }
                case "delete" -> {
                    var id = in.readInt();
                    out.writeBoolean(dao.delete(id));
                }
                case "findByRegionID" -> {
                    var id = in.readInt();
                    out.writeObject(dao.findByRegion(id));
                }
                case "findDatesByRegionWithConditions" -> {
                    var id = in.readInt();
                    var temperature = in.readInt();
                    out.writeObject(dao.findDatesByRegionWithConditions(id, temperature));
                }
                case "findByNationLanguageForLastWeek" -> {
                    var language = (String) in.readObject();
                    out.writeObject(dao.findByNationLanguageForLastWeek(language));
                }
                case "findAvgTempInRegionsWithConditionsForLastWeek" -> {
                    var square = in.readDouble();
                    out.writeObject(dao.findAverageTemperatureInRegionsWithConditionsForLastWeek(square));
                }
                default -> out.writeObject(null);
            }
            out.flush();
        }
    }
}
