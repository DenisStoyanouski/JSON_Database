package server;

import java.util.Arrays;

class Database {
    private String[] database = new String[1000];

    public Database() {
        Arrays.fill(database, "");
    }

    public String getCell(int index) {
        try {
            if (!database[index - 1].isEmpty()) {
                return database[index - 1];

            } else throw new IndexOutOfBoundsException();
        } catch (IndexOutOfBoundsException e) {
            return "ERROR";
        }
    }

    public String setCell(int index, String value) {
        try {
            database[index - 1] = value;
            return "OK";
        } catch (IndexOutOfBoundsException e) {
            return "ERROR";
        }
    }

    public String deleteCell(int index) {
        try {
            database[index - 1] = "";
            return "OK";
        } catch (IndexOutOfBoundsException e) {
            return "ERROR";
        }
    }

}
