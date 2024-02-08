package sejong.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sejong.user.entity.User;
import sejong.user.entity.UserTeam;
import sejong.user.repository.UserRepository;
import sejong.user.repository.UserTeamRepository;
import sejong.user.service.dto.SizeUserTeamDto;
import sejong.user.service.res.ApplyUsersInfoResponseDto;
import sejong.user.service.res.UsersInfoInTeamResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        log.info(Integer.toString(allByTeamId.size()));
        return SizeUserTeamDto.of(allByTeamId.size());
    }

    public List<UsersInfoInTeamResponseDto> findMembersInfoInTeam(Long teamId) {
        List<UserTeam> membersInTeam = userTeamRepository.findMembersInTeamByTeamId(teamId);
        List<User> users = membersInTeam.stream().map(UserTeam::getUser).collect(Collectors.toList());
        return users.stream().map(user ->
                UsersInfoInTeamResponseDto.builder()
                        .teamCnt(users.size())
                        .userName(user.getName())
                        .userId(user.getId())
                        .position(user.getPosition())
                        .age(user.getAge())
                        .build()).collect(Collectors.toList());
    }

    public List<ApplyUsersInfoResponseDto> applyMember(Long teamId) {
        List<UserTeam> applyMembers = userTeamRepository.findApplyMembersInTeamByTeamId(teamId);
        return applyMembers.stream().map(userTeam -> {
            User user = userTeam.getUser();
            return ApplyUsersInfoResponseDto.builder()
                    .userId(user.getId())
                    .age(user.getAge())
                    .teamCnt(applyMembers.size())
                    .introduce(userTeam.getIntroduce())
                    .userName(user.getName())
                    .position(user.getPosition())
                    .build();
        }).collect(Collectors.toList());
    }
}
