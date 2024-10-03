package webserver;

public enum Url {

    WEBAPP("webapp"),
    INDEX_HTML("/index.html"),
    USER_FORM_HTML("/user/form.html"),
    USER_SIGNUP("/user/signup"),
    USER_LOGIN("/user/login"),
    USER_LOGIN_FAILED_HTML("/user/login_failed.html"),
    USER_USERLIST("/user/userList"),
    USER_LIST_HTML("/user/list.html"),
    URL_HTML(".html"),
    URL_CSS("CSS");


    final String value;

    private Url(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
