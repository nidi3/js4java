package stni.js4java.demo;

/**
 *
 */
public class Response {
    private boolean valid;
    private Double growth;

    public Response(boolean valid, Double growth) {
        this.valid = valid;
        this.growth = growth;
    }

    public boolean isValid() {
        return valid;
    }

    public Double getGrowth() {
        return growth;
    }
}
