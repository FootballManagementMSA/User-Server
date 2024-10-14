package sejong.user.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sejong.user.user.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User entity);

    @Override
    void deleteAll();

    Optional<User> findByStudentId(String studentId);
}
