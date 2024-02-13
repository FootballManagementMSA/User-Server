package sejong.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.user.entity.User;
import sejong.user.entity.UserTeam;
import sejong.user.repository.UserRepository;
import sejong.user.repository.UserTeamRepository;
import sejong.user.service.dto.SizeUserTeamDto;
import sejong.user.service.req.ApplyUserTeamInfoRequestDto;
import sejong.user.service.req.ConfirmApplicationRequestDto;
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
        List<UserTeam> membersInTeam = userTeamRepository.findMembersInTeamByTeamId(teamId); // 특정팀에 가입대기열


        List<User> users = membersInTeam.stream()
                .filter(userTeam -> userTeam.getAccept().equals(true))
                .map(UserTeam::getUser)
                .collect(Collectors.toList()); // 특정팀에 속한 사용자

        return users.stream().map(user ->
                UsersInfoInTeamResponseDto.builder()
                        .teamCnt(getTeamCnt(user))
                        .userName(user.getName())
                        .userId(user.getId())
                        .position(user.getPosition())
                        .age(user.getAge())
                        .build()).collect(Collectors.toList());
    }

    private int getTeamCnt(User user) {
        return userTeamRepository.findByUserId(user.getId())
                .stream()
                .filter(userTeam -> userTeam.getAccept().equals(true))
                .collect(Collectors.toList()).size();
    }

    public List<ApplyUsersInfoResponseDto> findApplicantInTeam(Long teamId) {
        List<UserTeam> applyMembers = userTeamRepository.findApplyMembersInTeamByTeamId(teamId);
        return applyMembers.stream().filter(userTeam -> userTeam.getAccept() == false)
                .map(userTeam -> {
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


    @KafkaListener(topics = "team", groupId = "group_1")
    public void applyMember(ApplyUserTeamInfoRequestDto userInfoRequestDto) {
        // 특정 team에 userId가 속한지 확인
        Optional<UserTeam> userTeam =
                userTeamRepository.findByUserIdAndTeamId(userInfoRequestDto.getUserId(), userInfoRequestDto.getTeamId());
        // 있으면 예외
        if (userTeam.isPresent()) {
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

    @Transactional
    @KafkaListener(topics = "team_confirm", groupId = "group_1")
    public void confirmApplicant(ConfirmApplicationRequestDto requestDto) {
        // 특정 team에 userId가 속한지 확인
        UserTeam userTeam =
                userTeamRepository.findByUserIdAndTeamId(requestDto.getUserId(), requestDto.getTeamId()).orElseThrow(NullPointerException::new);
        if (userTeam.getAccept()) {
            throw new IllegalStateException("이미 가입된사람을 가입취소요청이 왜와");
        }
        if (!requestDto.isApprove()) {
            userTeamRepository.delete(userTeam);
        }
        userTeam.approve();
    }
}
