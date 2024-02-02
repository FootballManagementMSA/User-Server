package sejong.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.user.controller.res.UserSquadResponse;
import sejong.user.entity.User;
import sejong.user.global.exception.BadRequestException;
import sejong.user.global.exception.constant.ExceptionMessageConstant;
import sejong.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserSquadService {
    private final UserRepository userRepository;
    public UserSquadResponse getUserInfoInSquad(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(400,
                        ExceptionMessageConstant.NOT_REGISTER_USER_EXCEPTION_MESSAGE));

        UserSquadResponse userSquadResponse = UserSquadResponse.builder()
                .name(user.getName())
                .image(user.getImage())
                .build();
        return userSquadResponse;
    }

}
