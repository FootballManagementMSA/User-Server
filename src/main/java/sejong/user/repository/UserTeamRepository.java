package sejong.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.user.entity.UserTeam;

import java.util.List;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam,Long> {
    List<UserTeam> findAllByTeamId(Long teamId);

    @Query("select u from user_team_tb as u where u.accept=true and u.teamId=:teamId")
    List<UserTeam> findMembersInTeamByTeamId(@Param("teamId") Long teamId);

    @Query("select ut from user_team_tb as ut where ut.accept=true and ut.userId=:userId")
    List<UserTeam> findTeamsInUserByUserId(@Param("userId") Long userId);

    UserTeam save(UserTeam userTeam);
}
