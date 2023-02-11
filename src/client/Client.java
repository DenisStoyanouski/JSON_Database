package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Client {
    @Parameter(names="-t", description = "the type of the request")
    String request;

    @Parameter(names="-i", description = "the index of the cell")
    int index;

    @Parameter(names="-m",variableArity = true, description = "the value to save in the database")
    List<String> value = new ArrayList<>();


    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) {

        Client main = new Client();
        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);
        System.out.println(main.request + " " + main.index + " " + String.join(" ", main.value));


        System.out.println("Client started!");
        try (
                Socket socket = new Socket(InetAddress.getByName(SERVER_ADDRESS), SERVER_PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output  = new DataOutputStream(socket.getOutputStream())
        ) {
            String msg = String.format("%s %s %s", main.request, main.index, String.join(" ", main.value));
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
