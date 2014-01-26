package stni.js4java.demo;

/**
 *
 */
public class Model {
    @ValidEmail
    private String email;
    @ValidAge
    private Double age;
    @ValidHeight
    private Double height;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}
