package sejong.user.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.user.token.entity.FcmToken;

import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByUserId(Long userId);
    @Modifying
    @Query("UPDATE fcm_token_tb f SET f.fcmToken = :fcmToken WHERE f.userId = :userId")
    void updateFcmTokenByUserId(@Param("userId") Long userId, @Param("fcmToken") String fcmToken);
}
