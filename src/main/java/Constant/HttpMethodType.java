package Constant;

public enum HttpMethodType {
    GET("GET"),
    POST("POST");

    final String value;

    private HttpMethodType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
