package conconccc.schnofiticationbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // createdAt, updatedAt 자동 관리 활성화
public class SchNofiticationBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchNofiticationBeApplication.class, args);
    }

}
