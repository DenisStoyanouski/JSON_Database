package server;

import java.util.Arrays;

class Database {
    private String[] database = new String[1000];

    public Database() {
        Arrays.fill(database, "");
    }

    public void getCell(int index) {
        String str = null;
        try {
            if (!database[index - 1].isEmpty()) {
                str = database[index - 1];
                System.out.println(str);
            } else throw new IndexOutOfBoundsException();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR");
        }

    }

    public void setCell(int index, String value) {
        try {
            database[index - 1] = value;
            System.out.println("OK");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR");
        }
    }

    public void deleteCell(int index) {
        try {
            database[index - 1] = "";
            System.out.println("OK");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("ERROR");
        }
    }

}
