package sejong.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users extends Player {
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
}