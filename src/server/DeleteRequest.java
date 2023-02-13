package server;

public class DeleteRequest implements Request{

    private Database db;

    private int index;

    public DeleteRequest(Database db, int index) {
        this.db = db;
        this.index = index;
    }

    @Override
    public Message execute() {
        return db.deleteCell(index);
    }
}
