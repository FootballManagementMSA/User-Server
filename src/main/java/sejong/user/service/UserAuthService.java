package sejong.user.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import sejong.user.global.exception.NotFoundException;
import sejong.user.service.dto.UserAuthDto;
import sejong.user.repository.UserRepository;

import static sejong.user.global.exception.constant.ExceptionMessageConstant.NOT_REGISTER_USER_EXCEPTION_MESSAGE;
import static sejong.user.global.exception.constant.ExceptionMessageConstant.SEJONG_AUTH_FAIL_EXCEPTION_MESSAGE;
import static sejong.user.global.exception.constant.StatusCodeConstant.NOT_FOUND_STATUS_CODE;
import static sejong.user.service.constant.UserAuthServiceConstant.*;

@Service
@Transactional
@Slf4j
public class UserAuthService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public UserAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    public void userLogin(UserAuthDto.UserAuthRequest userAuthRequest) {
        ResponseEntity<String> response = getUserAuthData(userAuthRequest);
        String responseBody = response.getBody();

        JSONObject jsonObject = new JSONObject(responseBody);
        String resultCode = jsonObject.getJSONObject(RESULT).getString(CODE);
        validateAuthSejongStudent(resultCode, userAuthRequest);
    }

    private ResponseEntity<String> getUserAuthData(UserAuthDto.UserAuthRequest userAuthRequest) {
        String url = SEJONG_AUTH_URL;

        HttpHeaders headers = makeRequestHeader();
        JSONObject requestBody = makeJSONObject(userAuthRequest);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        return restTemplate.postForEntity(url, entity, String.class);
    }

    private HttpHeaders makeRequestHeader(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }

    private JSONObject makeJSONObject(UserAuthDto.UserAuthRequest userAuthRequest){
        JSONObject requestBody = new JSONObject();
        requestBody.put(ID, userAuthRequest.getId());
        requestBody.put(PASSWORD, userAuthRequest.getPw());

        return requestBody;
    }

    private void validateAuthSejongStudent(String resultCode, UserAuthDto.UserAuthRequest userAuthRequest){
        if (resultCode.equals(AUTH_FAIL)) throw new NotFoundException(NOT_FOUND_STATUS_CODE, SEJONG_AUTH_FAIL_EXCEPTION_MESSAGE);
        else if(resultCode.equals(SUCCESS)) validateRegisterUser(userAuthRequest);
    }

    private void validateRegisterUser(UserAuthDto.UserAuthRequest userAuthRequest) {
        userRepository.findByStudentId(userAuthRequest.getId())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_STATUS_CODE, NOT_REGISTER_USER_EXCEPTION_MESSAGE));
    }
}