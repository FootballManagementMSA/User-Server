package sejong.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.user.entity.UserTeam;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam,Long> {
    List<UserTeam> findAllByTeamId(Long teamId);

    UserTeam save(UserTeam userTeam);
}
