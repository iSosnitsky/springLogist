package sbat.logist.ru;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sbat.logist.ru.model.User;
import sbat.logist.ru.repositories.UserRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private final UserRepository repository;

	@Autowired
	public DatabaseLoader(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public void run(String... strings) throws Exception {
	    User ed = new User();
//	    ed.setUserRoleID("ADMIN");
//	    ed.setPass
//		this.repository.save();
	}
}
