package client;

import client.ui.GUI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.logging.Logger;

public class ClientRunner {
    public static void main(String[] args) {
        try  {
            GUI.main(null);
        } catch (IOException | NotBoundException e) {
            Logger.getLogger(ClientRunner.class.getName()).warning(e.getMessage());
        }
    }
}
