package sejong.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.user.entity.Role;
import sejong.user.entity.User;
import sejong.user.entity.UserTeam;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam,Long> {
    List<UserTeam> findAllByTeamId(Long teamId);

    @Query("select ut from user_team_tb as ut join fetch ut.user where ut.accept=true and ut.teamId=:teamId")
    List<UserTeam> findMembersInTeamByTeamId(@Param("teamId") Long teamId);
    @Query("select ut from user_team_tb as ut join fetch ut.user where ut.accept=false and ut.teamId=:teamId")
    List<UserTeam> findApplyMembersInTeamByTeamId(@Param("teamId") Long teamId);

    Optional<UserTeam> findByUserIdAndTeamId(@Param("userId") Long userId, @Param("teamId") Long teamId);

    UserTeam save(UserTeam userTeam);

    @Query("select ut from user_team_tb as ut where ut.user.id=:userId")
    List<UserTeam> findByUserId(@Param(value = "userId") Long userId);

    Optional<UserTeam> findFirstByTeamIdAndRole(Long teamId, Role role);

    List<UserTeam> findAllByUser(User user);
}
