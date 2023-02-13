package client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Request {
    private String type;
    private String key;
    private String value;
}
