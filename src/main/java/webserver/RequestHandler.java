package webserver;

import db.MemoryUserRepository;
import http.util.HttpRequestUtils;
import http.util.IOUtils;
import model.User;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Paths;

//클라이언트 요청을 처리하는 스레드 - 연결 소켓 처리
//서버로 들어오는 연결을 처리하고, 클라이언트에게 응답을 반환하는 역할
public class RequestHandler implements Runnable{
    private static final String WEBAPP_PATH = "webapp";
    Socket connection;
    private final MemoryUserRepository userRepository;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());

    public RequestHandler(Socket connection, MemoryUserRepository userRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
    }

    @Override
    public void run() {             //요청을 읽고, 간단한 "Hello World" 응답을 반환
        log.log(Level.INFO, "New Client Connect! Connected IP : " + connection.getInetAddress() + ", Port : " + connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){        //in*out stream
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);



            byte[] body;
            String request = br.readLine();                                 //start Line
            if(request == null){
                return;
            }

            //요청 라인 파싱
            String[] requestTokens = request.split(" ");
            String requestMethod = requestTokens[0];
            String requestUrl = requestTokens[1];

            //루트 경로 처리
            if(requestUrl.equals("/")){
                requestUrl = "/index.html";
            }
            String filePath = WEBAPP_PATH + requestUrl;             //쿼리 스트링 존재할 수 있음

            //회원 가입 페이지로 이동, 버튼 누르는 거니까 해당 위치에서 처리해주는 건 아닌가?
//            if(requestUrl.equals("/user/form.html")){
//                body = Files.readAllBytes(Paths.get(requestUrl));
//                response200Header(dos, body.length);
//                responseBody(dos, body);
//            }

            // 요구사항 2, GET 회원 가입
            if(requestMethod.equals("GET") && requestUrl.equals("/user/signup")){
                String queryString = requestUrl.split("\\?")[1];
                Map<String, String> queryParams = HttpRequestUtils.parseQueryParameter(queryString);        //주어진 쿼리 문자열을 파싱해여 map 형태로 반환

                String userId = queryParams.get("userId");
                String password = queryParams.get("password");
                String name = queryParams.get("name");
                String email = queryParams.get("email");
                User user = new User(userId, password, name, email);            //User 객체 생성

                if (userRepository.findUserById(userId) == null){
                    userRepository.addUser(user);
                }

                response302Header(dos, "/index.html");
            } // 요구사항 3, POST 회원 가입
            else if (requestMethod.equals("POST") && requestUrl.equals("/user/signup")) {
                int requestContentLength = 0;

                while (true) {                                              //해더 영역 스킵
                    String headerLine = br.readLine();
                    if (headerLine.equals("")) {
                        break;
                    }
                    if (headerLine.startsWith("Content-Length")) {          //바디의 길이가 적혀 있는 영역
                        requestContentLength = Integer.parseInt(headerLine.split(": ")[1]);
                    }
                }

                String requestBody = IOUtils.readData(br, requestContentLength);                        //바디 정보 읽어 옴
                Map<String, String> bodyParams = HttpRequestUtils.parseQueryParameter(requestBody);     //주어진 쿼리 문자열 파싱하여 map 형태로 변환


                String userId = bodyParams.get("userId");
                String password = bodyParams.get("password");
                String name = bodyParams.get("name");
                String email = bodyParams.get("email");
                User user = new User(userId, password, name, email);

                if (userRepository.findUserById(userId) == null) {
                    userRepository.addUser(user);
                }

                response302Header(dos, "/index.html");
            }  //요구사항 1  (로그인 후에 파읽을 요청하는 경우를 방지하기 위해 else로 묶어주었음
            else if(Files.exists(Paths.get(filePath))){                      //요청한 파일을 읽어 응답
                body = Files.readAllBytes(Paths.get(filePath));
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
            else{                                                           //요청한 파일이 존재하지 않음
                body = "404 Not Found".getBytes();                   //문자열을 바이트코드로 인코딩
                response404Header(dos, body.length);
                responseBody(dos, body);
            }

        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {     //성공적인 HTTP 응답 헤더를 클라이언트로 전송
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {                      //응답 본문을 클라이언트로 전송
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

}
