package sejong.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.user.entity.FcmToken;
import sejong.user.entity.Role;
import sejong.user.entity.UserTeam;
import sejong.user.global.exception.FcmTokenNotFoundException;
import sejong.user.repository.FcmTokenRepository;
import sejong.user.repository.UserTeamRepository;
import sejong.user.service.dto.FcmTokenDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;
    private final UserTeamRepository userTeamRepository;
    @Transactional
    public void saveOrUpdateToken(FcmTokenDto fcmTokenDto) {
        fcmTokenRepository.findByUserId(fcmTokenDto.getUserId())
                .ifPresentOrElse(
                        token -> fcmTokenRepository.updateFcmTokenByUserId(fcmTokenDto.getUserId(),
                                fcmTokenDto.getFcmToken()),
                        () -> {
                            FcmToken newToken = FcmToken.builder()
                                    .userId(fcmTokenDto.getUserId())
                                    .fcmToken(fcmTokenDto.getFcmToken())
                                    .build();
                            fcmTokenRepository.save(newToken);
                        }
                );
    }

    public String getFcmToken(Long teamId){
        UserTeam leaderUserTeam = userTeamRepository.findFirstByTeamIdAndRole(teamId, Role.LEADER).get();
        Long userIdOfLeader = leaderUserTeam.getUser().getId();

        return fcmTokenRepository.findByUserId(userIdOfLeader)
                .map(FcmToken::getFcmToken)
                .orElseThrow(() -> new FcmTokenNotFoundException("FCM token not found for user ID: " + userIdOfLeader));
    }
}
