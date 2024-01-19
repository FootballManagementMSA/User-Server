package sejong.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class UserTeam {
    @Id
    @GeneratedValue
    private Long id;
    private Long teamId;
    private Long userId;
    private Boolean accept;
    private Role role;
}
