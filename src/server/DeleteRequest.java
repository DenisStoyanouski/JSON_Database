package server;

public class DeleteRequest implements Request{

    private Database db;

    private String key;

    public DeleteRequest(Database db, String key) {
        this.db = db;
        this.key = key;
    }

    @Override
    public Message execute() {
        return db.deleteCell(key);
    }
}
