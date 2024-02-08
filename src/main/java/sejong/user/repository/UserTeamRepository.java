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

    @Query("select ut from user_team_tb as ut join fetch ut.user where ut.accept=true and ut.teamId=:teamId")
    List<UserTeam> findMembersInTeamByTeamId(@Param("teamId") Long teamId);
    @Query("select ut from user_team_tb as ut join fetch ut.user where ut.accept=false and ut.teamId=:teamId")
    List<UserTeam> findApplyMembersInTeamByTeamId(@Param("teamId") Long teamId);

    UserTeam save(UserTeam userTeam);
}
