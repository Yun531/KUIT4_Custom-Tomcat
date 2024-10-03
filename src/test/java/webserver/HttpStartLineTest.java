package webserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpStartLineTest {
    @Test
    void StartLine_정상_파싱_test(){
        HttpStartLine httpStartLine = HttpStartLine.from("POST /user/create HTTP/1.1");

        assertEquals("POST", httpStartLine.getMethod());
        assertEquals("/user/create", httpStartLine.getPath());
        assertEquals("HTTP/1.1", httpStartLine.getVersion());
    }

}