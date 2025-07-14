package com.tenco.blog.user;

import com.tenco.blog._core.errors.exception.Exception400;
import com.tenco.blog._core.errors.exception.Exception404;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true) // 클래스 레벨에서의 읽기 전용 설정
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserJpaRepository userJpaRepository;


    /**
     * 회원가입 처리 - DTO 변환
     */
    @Transactional // 메서드 레벨에서 쓰기 전용 트랜잭션 활성화
    public UserResponse.JoinDTO join(UserRequest.JoinDTO joinDTO) {
        userJpaRepository.findByUsername(joinDTO.getUsername())
                .ifPresent(user1 -> {
                    throw new Exception400("이미 존재하는 사용자명입니다");
                });

        User savedUser = userJpaRepository.save(joinDTO.toEntity());

        return new UserResponse.JoinDTO(savedUser);
    }

    /**
     * 로그인 처리 - DTO 변환
     */
    public UserResponse.LoginDTO login(UserRequest.LoginDTO loginDTO) {
        User selectedUser = userJpaRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                .orElseThrow(() -> {
                    throw new Exception400("이름 또는 비밀번호가 틀렸습니다.");
                });

        return new UserResponse.LoginDTO(selectedUser);
    }

    /**
     *  사용자 정보 조회
     */
    public UserResponse.DetailDTO findById(Long id) {
        // 권한 검사는 일단 생략

        User selectedUser = userJpaRepository.findById(id).orElseThrow(() -> {
            log.warn("사용자 조회 실패 - ID {}", id);
            return new Exception404("사용자를 찾을 수 없습니다");
        });

        return new UserResponse.DetailDTO(selectedUser);
    }

    /**
     *  회원정보 수정 처리 (더티 체킹)
     */
    @Transactional
    public UserResponse.UpdateDTO updateById(Long userId, UserRequest.UpdateDTO updateDTO) {
        log.info("회원 정보 수정 서비스 처리 시작 - ID : {}", userId);

        User selectedUser = userJpaRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new Exception404("사용자를 찾을 수 없습니다.");
                });

        selectedUser.update(updateDTO);
        return new UserResponse.UpdateDTO(selectedUser);
    }
}
