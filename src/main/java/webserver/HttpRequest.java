package webserver;

import java.io.BufferedReader;
import java.io.IOException;

import static webserver.HttpHeaderType.*;

public class HttpRequest {
    private final HttpStartLine httpStartLine;
    private final HttpHeader httpHeader;
    private final HttpBody httpBody;

    // 생성자
    private HttpRequest(HttpStartLine httpStartLine, HttpHeader httpHeader, HttpBody httpBody) {
        this.httpStartLine = httpStartLine;
        this.httpHeader = httpHeader;
        this.httpBody = httpBody;
    }

    // 정적 팩토리 메서드
    public static HttpRequest from(BufferedReader br) throws IOException {

        HttpStartLine httpStartLine = HttpStartLine.from(br.readLine());
        HttpHeader httpHeader = HttpHeader.from(br);

        HttpBody httpBody = null;
        if (httpHeader.getHeader(CONTENT_LENGTH.getValue()) != null) {                   //body 내용 있는지 확인
            int contentLength = Integer.parseInt(httpHeader.getHeader(CONTENT_LENGTH.getValue()));
            httpBody = HttpBody.from(br, contentLength);                    //body있을 경우 HttpBody 생성
        } else {
            httpBody = HttpBody.from(br, 0);                    //body 없을 경우 빈 문자열로 처리
        }

        return new HttpRequest(httpStartLine, httpHeader, httpBody);
    }

    // Getters
    public String getMethod() {
        return httpStartLine.getMethod();
    }

    public String getPath() {
        return httpStartLine.getPath();
    }

    public String getVersion() {
        return httpStartLine.getVersion();
    }

    public String getHeader(String key) {
        return httpHeader.getHeader(key);
    }

    public String getBody(String key) {
        return httpBody.getBody(key);
    }
}
