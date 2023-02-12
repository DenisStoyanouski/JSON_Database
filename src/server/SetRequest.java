package server;

public class SetRequest implements Request{

    private Database db;

    private int index;

    private String value;

    public SetRequest(Database db, int index, String value) {
        this.db = db;
        this.index = index;
        this.value = value;
    }

    @Override
    public void execute() {
        db.setCell(index, value);
    }
}
