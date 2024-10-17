package sejong.user.user.entity;

import jakarta.persistence.*;
import lombok.*;
import sejong.user.user.dto.UserDto;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_tb")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_seq")
	private Long userSeq;

	@Column(name = "student_id", nullable = false)
	private String studentId;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "grade", nullable = false)
	private String grade;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "major", nullable = false)
	private String major;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "position", nullable = false)
	private String position;

	@Column(name = "foot", nullable = false)
	private String foot;

	@Column(name = "sex", nullable = false)
	private String sex;

	@Column(name = "age", nullable = false)
	private Integer age;

	@Column(name = "height", nullable = false)
	private Integer height;

	@Column(name = "game", nullable = false)
	private Integer game;

	@Column(name = "goal", nullable = false)
	private Integer goal;

	public void updateUser(UserDto.ModifyUserRequest modifyUserDto) {
		this.name = modifyUserDto.getName();
		this.age = modifyUserDto.getAge();
		this.height = modifyUserDto.getHeight();
		this.sex = modifyUserDto.getSex();
		this.position = modifyUserDto.getPosition();
		this.foot = modifyUserDto.getFoot();
	}
}