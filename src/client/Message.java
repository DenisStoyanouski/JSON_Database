package client;

import com.google.gson.annotations.Expose;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
class Message {
    @Expose
    private String type;
    @Expose
    private String key;
    @Expose
    private String value;
}
