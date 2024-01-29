package sejong.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sejong.user.entity.User;
import sejong.user.entity.UserTeam;
import sejong.user.repository.UserRepository;
import sejong.user.repository.UserTeamRepository;
import sejong.user.service.dto.SizeUserTeamDto;
import sejong.user.service.res.UsersInfoInTeamResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserTeamService {
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;

    /**
     * 특정 팀에 소속된 사람의 수를 구해주는 메서드
     *
     * @param teamId
     * @return SizeUserTeamDto
     */
    public SizeUserTeamDto countUserTeam(Long teamId) {
        List<UserTeam> allByTeamId = userTeamRepository.findAllByTeamId(teamId).stream()
                .filter(userTeam -> userTeam.getAccept().booleanValue())
                .collect(Collectors.toList());
        return SizeUserTeamDto.of(allByTeamId.size());
    }

    public List<UsersInfoInTeamResponseDto> findMembersInfoInTeam(Long teamId) {
        List<UserTeam> membersInTeam = userTeamRepository.findMembersInTeamByTeamId(teamId);

        List<Long> membersId = membersInTeam.stream().map(UserTeam::getUserId).collect(Collectors.toList());
        List<User> usersIn = userRepository.findUsersIn(membersId);
        return usersIn.stream().map(user ->
                UsersInfoInTeamResponseDto.builder()
                        .teamCnt(userTeamRepository.findTeamsInUserByUserId(user.getId()).size())
                        .userName(user.getName())
                        .userId(user.getId())
                        .position(user.getPosition())
                        .age(user.getAge())
                        .build()).collect(Collectors.toList());
    }
}
