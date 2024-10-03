package webserver;

public enum HTTPContentType {
    HTML("text/html"),
    CSS("text/css");

    String code;

    HTTPContentType(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
