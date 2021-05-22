package client;

import java.io.IOException;
import java.util.logging.Logger;

public class ClientRunner {
    public static void main(String[] args) {
        try (var client = new Client()) {
            var list = client.findAllAirlines();
            for (var item : list) {
                System.out.println(item.getName());
            }
        } catch (IOException e) {
            Logger.getLogger(ClientRunner.class.getName()).warning(e.getMessage());
        }
    }
}
