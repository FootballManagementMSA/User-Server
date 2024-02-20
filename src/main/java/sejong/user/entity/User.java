package sejong.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import sejong.user.service.dto.UserDto;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String studentId;
    private String password;
    private String grade;
    private String status;
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

    public void updateUser(UserDto.ModifyUserRequest modifyUserDto, String image) {
        this.name = modifyUserDto.getName();
        this.age = modifyUserDto.getAge();
        this.height = modifyUserDto.getHeight();
        this.sex = modifyUserDto.getSex();
        this.position = modifyUserDto.getPosition();
        this.foot = modifyUserDto.getFoot();
        this.image = image;
    }
}