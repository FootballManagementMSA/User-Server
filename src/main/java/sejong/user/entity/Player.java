package sejong.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Builder
public class Player {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "player_id", updatable = false, nullable = false)
    private String id;

    private String position;
    private String foot;
    private String sex;
    private Integer age;
    private Integer height;
    private String image;
    private Integer game;
    private Integer goal;
}