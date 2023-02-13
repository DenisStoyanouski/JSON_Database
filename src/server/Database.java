package server;

import java.util.Arrays;

class Database {
    private String[] database = new String[1000];

    public Database() {
        Arrays.fill(database, "");
    }

    public Message getCell(int index) {
        Message response = new Message();
        try {
            if (!database[index - 1].isEmpty()) {
                response.setType("OK");
                response.setValue(database[index - 1]);
            } else throw new IndexOutOfBoundsException();
        } catch (IndexOutOfBoundsException e) {
            response.setType("ERROR");
            response.setKey("No such key");
        }
        return response;
    }

    public Message setCell(int index, String value) {
        Message response = new Message();
        try {
            database[index - 1] = value;
            response.setType("OK");
        } catch (IndexOutOfBoundsException e) {
            response.setType("ERROR");
            response.setKey("No such key");
        }
        return response;
    }

    public Message deleteCell(int index) {
        Message response = new Message();
        try {
            database[index - 1] = "";
            response.setType("OK");
        } catch (IndexOutOfBoundsException e) {
            response.setType("ERROR");
            response.setKey("No such key");
        }
        return response;
    }

}
