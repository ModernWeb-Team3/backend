package kr.unideal.server.backend.repository;

import kr.unideal.server.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    //존재하는 이메일인지 확인
    boolean existsByEmail(String email);

    //user의 데이터를 email로 찾음
    Optional<User> findByEmail(String email);
}