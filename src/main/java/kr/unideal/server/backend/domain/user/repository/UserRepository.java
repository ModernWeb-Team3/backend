package kr.unideal.server.backend.domain.user.repository;

import kr.unideal.server.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //user의 데이터를 email로 찾음
    Optional<User> findByEmail(String email);
}
