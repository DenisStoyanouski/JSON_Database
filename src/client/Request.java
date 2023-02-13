package client;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Request {
    @Expose
    private String type;
    @Expose
    private String key;
    @Expose
    private String value;
}
