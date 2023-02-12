package server;

public class Requester {
    private Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    public String executeRequest() {
       return request.execute();
    }


}
