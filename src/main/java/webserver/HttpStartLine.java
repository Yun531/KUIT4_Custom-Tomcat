package webserver;

public class HttpStartLine {
    private final String method;
    private final String path;
    private final String version;

    public HttpStartLine(String method, String path, String version) {
        this.method = method;
        this.path = path;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public static HttpStartLine from(String startLine) {
        String[] tokens = startLine.split(" ");
        if (tokens.length != 3) {
            throw new IllegalArgumentException("Invalid HTTP Start Line");
        }
        return new HttpStartLine(tokens[0], tokens[1], tokens[2]);
    }
}
