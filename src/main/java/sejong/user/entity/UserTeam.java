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
    private Long id;
    private Long teamId;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private Boolean accept;
    @Enumerated(EnumType.STRING)
    private Role role;
}
