package server;

public class GetRequest implements Request{
    private Database db;

    private int index;

    public GetRequest(Database db, int index) {
        this.db = db;
        this.index = index;
    }

    @Override
    public Message execute() {
        return db.getCell(index);
    }
}
