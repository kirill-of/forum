package pro.ofitserov.forumtest1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pro.ofitserov.forumtest1.entity.Role;

@SpringBootApplication
public class ForumTest1Application {

	public static void main(String[] args) {
		SpringApplication.run(ForumTest1Application.class, args);

		Role role = new Role();
	}
}
