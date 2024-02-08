package sejong.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sejong.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User save(User entity);

    @Override
    void deleteAll();
    @Query("select u from user_tb as u where u.id in :usersId")
    List<User> findUsersIn(@Param("usersId") List<Long>usersId);
    Optional<User> findByStudentId(String studentId);
}
