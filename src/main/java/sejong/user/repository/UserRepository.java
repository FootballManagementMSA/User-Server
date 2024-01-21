package sejong.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sejong.user.entity.User;

public interface UserRepository extends JpaRepository<User,String> {
    User save(User entity);

    @Override
    void deleteAll();
}
