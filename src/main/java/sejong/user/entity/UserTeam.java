package sejong.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sejong.user.global.entity.BaseTimeEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity(name = "user_team_tb")
public class UserTeam extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_team_id")
    private Long id;
    private Long teamId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * 신청중이면 false, 신청승인됬으면 true, 신청거부면 삭제
     */
    private Boolean accept;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String introduce;
    public void approve(){
        this.accept = true;
    }
}
