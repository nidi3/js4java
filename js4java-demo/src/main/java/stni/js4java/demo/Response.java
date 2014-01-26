package stni.js4java.demo;

import java.util.List;

/**
 *
 */
public class Response {
    private List<String> messages;
    private boolean valid;
    private Double growth;

    public Response(List<String> messages, boolean valid, Double growth) {
        this.messages = messages;
        this.valid = valid;
        this.growth = growth;
    }

    public Response(boolean valid, Double growth) {
        this(null, valid, growth);
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean isValid() {
        return valid;
    }

    public Double getGrowth() {
        return growth;
    }
}
