package sejong.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.user.entity.UserTeam;
import sejong.user.repository.UserTeamRepository;
import sejong.user.service.dto.SizeOfUsersInTeamDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserTeamService {
    private final UserTeamRepository userTeamRepository;
    public SizeOfUsersInTeamDto findUsersInTeam(Long teamId){
        List<UserTeam> allByTeamId = userTeamRepository.findAllByTeamId(teamId);
        return SizeOfUsersInTeamDto.of(allByTeamId.size());
    }
}
