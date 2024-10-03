package webserver;

public enum StatusCode {
    OK("200 OK"),
    FOUND("302 Found"),
    NOT_FOUND("404 Not Found");


    private final String code;
    StatusCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}
