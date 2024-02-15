package sejong.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User save(User entity);

    @Override
    void deleteAll();
    Optional<User> findByStudentId(String studentId);
}
