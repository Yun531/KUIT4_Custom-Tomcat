package Controller;

import db.MemoryUserRepository;
import http.HttpRequest;
import http.HttpResponse;
import http.util.HttpRequestUtils;
import model.User;

import java.io.IOException;
import java.util.Map;

import static Constant.HttpMethodType.*;
import static Constant.Url.INDEX_HTML;
import static Constant.UserQueryKey.*;
import static Constant.UserQueryKey.EMAIL;

public class SignUpController implements Controller{
    private final MemoryUserRepository userRepository = MemoryUserRepository.getInstance();
    @Override
    public void execute(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        if(httpRequest.getMethod().equals(GET.getValue())){
            handleSignUpGet(httpRequest, httpResponse);
        }
        if(httpRequest.getMethod().equals(POST.getValue())){
            handleSignUpPost(httpRequest, httpResponse);
        }

    }
    private void handleSignUpGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String requestUrl = httpRequest.getPath();
        String queryString = requestUrl.split("\\?")[1];
        Map<String, String> queryParams = HttpRequestUtils.parseQueryParameter(queryString);        //주어진 쿼리 문자열을 파싱해여 map 형태로 반환

        User user = User.from(queryParams);            //User 객체 생성

        if (userRepository.findUserById(user.getUserId()) == null){
            userRepository.addUser(user);
        }

        httpResponse.redirect(INDEX_HTML.getMessage());
    }

    private void handleSignUpPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        User user = User.from(httpRequest);

        if (userRepository.findUserById(user.getUserId()) == null) {
            userRepository.addUser(user);
        }

        httpResponse.redirect(INDEX_HTML.getMessage());
    }



}
