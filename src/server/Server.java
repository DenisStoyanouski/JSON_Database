package server;

import java.util.Scanner;

public class Server {

    /*static String method;
    static int index;
    static String value;

    static Scanner scanner = new Scanner(System.in);*/

    public static void main(String[] args) {
        ServerStarter.startServer();
        //makeResponse();

    }

    /*private static void readInputFromConsole() {
        StringBuilder sb = new StringBuilder();
        String[] line = scanner.nextLine().split("\\s+");
        for (int i = 0; i < line.length; i++) {
            if (i == 0) {
                method = line[i];
            }
            if (i == 1) {
                index = Integer.parseInt(line[i]);
            }
            if (i > 1) {
                sb.append(line[i]);
                sb.append(" ");
            }
        }
        value = sb.toString();
    }*/

    /*private static void makeResponse() {
        Database db = new Database();
        while (true) {
            readInputFromConsole();
            switch (method) {
                case "get" : db.getCell(index);
                    break;
                case "set" : db.setCell(index, value);
                    break;
                case "delete" : db.deleteCell(index);
                    break;
                case "exit" : System.exit(0);
                    break;
                default:
                    System.out.println("Unknown command. Try again");
                    break;
            }
        }
    }*/
}
