package sejong.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.user.entity.UserTeam;
import sejong.user.repository.UserTeamRepository;
import sejong.user.service.dto.SizeUserTeamDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserTeamService {
    private final UserTeamRepository userTeamRepository;

    /**
     * 특정 팀에 소속된 사람의 수를 구해주는 메서드
     * @param teamId
     * @return SizeUserTeamDto
     */
    public SizeUserTeamDto countUserTeam(Long teamId) {
        List<UserTeam> allByTeamId = userTeamRepository.findAllByTeamId(teamId).stream()
                .filter(userTeam -> userTeam.getAccept().booleanValue())
                .collect(Collectors.toList());
        return SizeUserTeamDto.of(allByTeamId.size());
    }
}
