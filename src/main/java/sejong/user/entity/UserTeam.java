package sejong.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class UserTeam {
    @Id
    @GeneratedValue
    private Long id;
    private Long teamId;
    private String userId;
    private Boolean accept;
    @Enumerated(EnumType.STRING)
    private Role role;
}
