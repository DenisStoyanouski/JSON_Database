package server;

public class DeleteRequest implements Request{

    private Database db;

    private int index;

    public DeleteRequest(Database db, int index) {
        this.db = db;
        this.index = index;
    }

    @Override
    public String execute() {
        db.deleteCell(index);
        return null;
    }
}
