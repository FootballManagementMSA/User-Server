package sejong.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import sejong.user.entity.Role;
import sejong.user.entity.User;
import sejong.user.entity.UserTeam;
import sejong.user.repository.UserRepository;
import sejong.user.repository.UserTeamRepository;
import sejong.user.service.dto.SizeUserTeamDto;
import sejong.user.service.req.ApplyUserInfoRequestDto;
import sejong.user.service.res.ApplyUsersInfoResponseDto;
import sejong.user.service.res.UsersInfoInTeamResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sejong.user.entity.Role.MEMBER;

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

    public List<ApplyUsersInfoResponseDto> findApplyMember(Long teamId) {
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


    @KafkaListener(topics = "team")
    public void applyMember(ApplyUserInfoRequestDto userInfoRequestDto) {
        // 특정 team에 userId가 속한지 확인
        Optional<UserTeam> userTeam =
                userTeamRepository.findByUserIdAndTeamId(userInfoRequestDto.getUserId(), userInfoRequestDto.getTeamId());
        // 있으면 예외
        if(userTeam.isPresent()){
            throw new IllegalStateException("이미 가입신청중인데 뭘 또 신청해 에러에러");
        }
        User user = userRepository.findById(userInfoRequestDto.getUserId()).orElseThrow(NullPointerException::new);

        UserTeam userTeam1 = UserTeam.builder()
                .accept(false)
                .teamId(userInfoRequestDto.getTeamId())
                .role(MEMBER)
                .user(user)
                .introduce(userInfoRequestDto.getIntroduce())
                .build();
        // 없으면 등록
        userTeamRepository.save(userTeam1);
    }
}
