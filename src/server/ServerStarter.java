package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerStarter {
    private static final int PORT = 23456;

    private static final String SERVER_ADDRESS = "localhost";

    public static void startServer() {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(SERVER_ADDRESS))) {
            System.out.println("Server started!");
            //while (true) {
                Session session = new Session(server.accept());
                session.start();
                // it does not block this thread
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Session extends Thread {
    private final Socket socket;

    public Session(Socket socketForClient) {
        this.socket = socketForClient;
    }

    public void run() {
        try (
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String inputMsg = input.readUTF(); // reading the next client message
            System.out.printf("Received: %s%n", inputMsg);
            int number = getNumberOfRecord(inputMsg);
            String outputMsg = String.format("A record # %d was sent!!", number);
            output.writeUTF(outputMsg); // resend it to the client
            System.out.printf("Sent: %s%n", outputMsg);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNumberOfRecord(String inputMsg) {
        Pattern p = Pattern.compile("# \\d+\\b");
        Matcher m = p.matcher(inputMsg);
        if (m.find()) {
            return Integer.parseInt(m.group().replaceAll("#\\s+",""));
        }
        return 0;
    }
}
