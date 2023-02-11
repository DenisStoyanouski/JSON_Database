package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            String msg = "Give me a record # 12";//scanner.nextLine();
            System.out.printf("Sent: %s%n", msg);
            output.writeUTF(msg);
            String receivedMsg = input.readUTF();
            System.out.printf("Received: %s%n",receivedMsg);
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
