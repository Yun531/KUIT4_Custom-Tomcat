package webserver;

import http.util.HttpRequestUtils;
import http.util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class HttpBody {
    private final Map<String, String> body;

    private HttpBody(Map<String, String> body) {
        this.body = body;
    }

    public String getBody(String key) {
        return body.get(key);
    }

    public static HttpBody from(BufferedReader br, int contentLength) throws IOException {
        String body = IOUtils.readData(br, contentLength);
        Map<String, String> bodyMap = HttpRequestUtils.parseQueryParameter(body);     //주어진 쿼리 문자열 파싱하여 map 형태로 변환

        return new HttpBody(bodyMap);
    }
}
