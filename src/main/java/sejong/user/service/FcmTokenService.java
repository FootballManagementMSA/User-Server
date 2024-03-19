package sejong.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.user.entity.FcmToken;
import sejong.user.repository.FcmTokenRepository;
import sejong.user.service.dto.FcmTokenDto;

@Service
@RequiredArgsConstructor
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;
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
}
