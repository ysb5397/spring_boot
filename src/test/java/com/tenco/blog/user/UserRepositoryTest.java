package com.tenco.blog.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(UserRepository.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired // DI 처리
    private UserRepository userRepository;

    @Test
    public void save_회원가입_테스트() {
        // 빌드업 패턴(회원가입시 사용할 사용자 정보)
        User user = User.builder()
                .username("testUser")
                .email("a@naver.com")
                .password("asd1234")
                .build();

        User savedUser = userRepository.save(user);

        // id 할당여부
        Assertions.assertThat(savedUser.getId()).isNotNull();

        // 데이터 정상 등록 확인
        Assertions.assertThat(savedUser.getUsername()).isEqualTo("testUser");

        // 원본 user Object와 영속화된 Object가 동일한지 확인
        // 영속성 컨텍스트는 같은 엔티티에 대해 같은 인스턴스 보장
        Assertions.assertThat(savedUser).isSameAs(user);
    }

    @Test
    public void findByUsername_존재하지_않는_사용자_테스트() {
        String username = "nonUser";
        User user = userRepository.findByUsername(username);
        Assertions.assertThat(user).isNull();
    }

    @Test
    public void findByUsernameAndPassword_로그인_테스트() {
        String username = "ssar";
        String password = "1234";
        User user = userRepository.findByUsernameAndPassword(username, password);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getUsername()).isEqualTo(username);
    }
}
