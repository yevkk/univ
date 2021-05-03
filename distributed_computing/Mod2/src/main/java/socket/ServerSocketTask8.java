package socket;

import dao.NationDAO;
import dao.RegionDAO;
import dao.WeatherRecordDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
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
        private Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (var in = new ObjectInputStream(socket.getInputStream()); var out = new ObjectOutputStream(socket.getOutputStream())) {
                String mode = (String) in.readObject();
                String action = (String) in.readObject();
                switch (mode) {
                    case "nations":
                        nationsHelper(in, out, action);
                        break;
                    case "regions":
                        regionsHelper(in, out, action);
                        break;
                    case "weather":
                        weatherHelper(in, out, action);
                        break;
                    default:
                        out.writeObject(Boolean.FALSE);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void nationsHelper (ObjectInputStream in, ObjectOutputStream out, String action) throws IOException {
            var dao = new NationDAO();
            switch (action) {
                default:
                    out.writeObject(Boolean.FALSE);
            }
        }

        private void regionsHelper (ObjectInputStream in, ObjectOutputStream out, String action) throws IOException {
            var dao = new RegionDAO();
            switch (action) {
                default:
                    out.writeObject(Boolean.FALSE);
            }
        }

        private void weatherHelper (ObjectInputStream in, ObjectOutputStream out, String action) throws IOException {
            var dao = new WeatherRecordDAO();
            switch (action) {
                default:
                    out.writeObject(Boolean.FALSE);
            }
        }
    }
}
