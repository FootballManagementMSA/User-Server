package sejong.user.user.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sejong.user.global.response.BaseResponse;
import sejong.user.global.response.DataResponse;
import sejong.user.user.service.UserAuthService;
import sejong.user.user.service.UserService;

import java.io.IOException;

import static sejong.user.global.response.constant.StatusCodeConstant.OK_STATUS_CODE;
import static sejong.user.global.response.constant.ResponseMessageConstant.SUCCESS;
import static sejong.user.user.dto.UserAuthDto.*;
import static sejong.user.user.dto.UserDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service/users")
public class UserController {
	private final UserAuthService userAuthService;
	private final UserService userService;

	@PostMapping()
	public ResponseEntity<BaseResponse> createUser(@RequestBody UserRegisterRequest request) throws IOException {
		userAuthService.registerUser(request);

		return ResponseEntity.ok().body(new BaseResponse());
	}

	@PutMapping()
	public ResponseEntity<BaseResponse> updateUser(@RequestBody ModifyUserRequest request) throws IOException {
		userService.modifyUser(request);

		return ResponseEntity.ok().body(new BaseResponse());
	}

	@GetMapping()
	public ResponseEntity<DataResponse<GetUserResponse>> getUser(@RequestParam(value = "studentId") String studentId) {
		GetUserResponse response = userService.getUser(studentId);

		return ResponseEntity.ok().body(new DataResponse(OK_STATUS_CODE, SUCCESS, response));
	}

	@DeleteMapping()
	public ResponseEntity<BaseResponse> deleteUser(@RequestParam(value = "studentId") String studentId) {
		userService.deleteUser(studentId);

		return ResponseEntity.ok().body(new BaseResponse());
	}
}
