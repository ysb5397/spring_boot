package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor // 생성자 의존 주입 - DI 처리
@Repository // IoC 대상  + 싱글톤 패턴으로 관리
public class UserRepository {

    private final Logger log = LoggerFactory.getLogger(UserRepository.class);
    private final EntityManager em;

    // 사용자 정보 조회(이름 , 비밀번호 필요)
    // where절로 이름과 비밀번호 확인
    /**
     * 로그인 요청 기능(사용자 정보 조회)
     * @param username
     * @param password
     * @return 성공시 User 엔티티 반환하고, 실패시 null 반환
     */
    public User findByUsernameAndPassword(String username, String password) {
        // JPQL
        log.info("로그인 시도 - 사용자명: {}, 비밀번호: {}", username, password);
        try {
            String jpql = "select u from User u where u.username = :username and u.password = :password ";
            TypedQuery typedQuery = em.createQuery(jpql, User.class);
            typedQuery.setParameter("username", username);
            typedQuery.setParameter("password", password);
            typedQuery.getSingleResult();
            return (User) typedQuery.getSingleResult();
        } catch (Exception e) {
            // 일치하는 사용자가 없거나 에러 발생 시 null 반환
            // 로그인 실패
            return null;
        }
    }

    // 회원정보 저장
    /**
     * 회원 정보 저장 처리
     * @param user(비영속 상태)
     * @return User 엔티티 반환
     */
    @Transactional
    public User save(User user) {
        // 매개변수에 들어오는 유저 정보는 비영속화 상태
        log.info("회원 정보 저장 시작 - 사용자명: {}", user.getUsername());
        em.persist(user); // 영속성 컨텍스트에서 user 객체를 관리하기 시작

        // 트랜잭션 커밋 시점에 실제 insert 쿼리를 실행
        return user;
    }

    // 사용자명 중복 체크용 조회 기능
    public User findByUsername(String username) {
        log.info("사용자명 중복 체크 - 사용자명: {}", username);
        try {
            String jpql = "select u from User u where u.username = :username ";
            return em.createQuery(jpql, User.class).setParameter("username", username).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }


    public User findById(Long id) {
        log.info("회원 정보 조회 시작: {}", id);
        User user = em.find(User.class, id);

        if (user == null) {
            throw new Exception400("사용자를 찾을 수 없습니다.");
        }

        return user;
    }

    @Transactional
    public User updateById(Long id, UserRequest.UpdateDTO updateDTO) {
        log.info("회원 정보 수정 시작 id: {}", id);
        // 조회하고 더티체킹
        User user = findById(id);

        // 객체의 상태값을 행위를 통해 변경
        user.setPassword(updateDTO.getPassword());

        return user;
    }
}
