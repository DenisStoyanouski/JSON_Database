package server;

public class Requester {
    private Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    public Message executeRequest() {
       return request.execute();
    }


}
