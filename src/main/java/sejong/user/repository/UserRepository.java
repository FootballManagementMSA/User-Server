package sejong.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.user.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User entity);

    @Override
    void deleteAll();

    Optional<User> findByStudentId(String studentId);

    @Modifying
    @Query("UPDATE user_tb u SET u.name = :name, u.age = :age, u.height = :height, u.sex = :sex, u.position = :position, u.foot = :foot, u.image = :image WHERE u.studentId = :studentId")
    void modifyUser(@Param("name") String name,
                   @Param("age") Integer age,
                   @Param("height") Integer height,
                   @Param("sex") String sex,
                   @Param("position") String position,
                   @Param("foot") String foot,
                   @Param("image") String image,
                   @Param("studentId") String studentId);
}
