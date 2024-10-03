package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpHeader {
    private final Map<String, String> headers = new HashMap<>();

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public static HttpHeader from(BufferedReader br) throws IOException {
        HttpHeader httpHeader = new HttpHeader();
        String line;
        while (!(line = br.readLine()).isEmpty()) {                     // 빈 줄을 만나면 헤더가 끝남
            String[] headerTokens = line.split(": ");
            httpHeader.addHeader(headerTokens[0], headerTokens[1]);
        }
        return httpHeader;
    }
}
