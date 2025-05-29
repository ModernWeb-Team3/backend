package kr.unideal.server.backend.domain.user.repository;


import kr.unideal.server.backend.domain.user.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<VerificationCode, Long> {

    Optional<VerificationCode> findByEmailAndCode(String email, String code);
    void deleteByExpiresTimeBefore(LocalDateTime time);

}
