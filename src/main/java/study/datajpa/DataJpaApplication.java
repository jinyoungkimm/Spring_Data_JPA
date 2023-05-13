package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing // Spring Data JPA가 제공하는 Auditing을 사용하기 위해서는 꼭 필요하다.
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {

		SpringApplication.run(DataJpaApplication.class, args);

	}

	@Bean
	public AuditorAware<String> auditorProvider(){
		return ()-> Optional.of(UUID.randomUUID().toString()); // 등록자, 수정자의 정보(보통의 경우, 회원의 id)를 담고 있는 빈이라고 생각하면 된다.
	}					// 보통 위와 같이 정보를 담지 않는다. HTTP seession으로 부터 user의 id를 추출하는 방식이 일반적!

}
