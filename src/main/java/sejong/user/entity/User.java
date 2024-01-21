package sejong.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "user_id", updatable = false, nullable = false)
    private String id;
    private String studentId;
    private String grade;
    private String state;
    private String major;
    private String name;

    private String position;
    private String foot;
    private String sex;
    private Integer age;
    private Integer height;
    private String image;
    private Integer game;
    private Integer goal;
}