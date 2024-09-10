package com.ivanbeidel.securitybook;

import com.ivanbeidel.securitybook.enums.AppRole;
import com.ivanbeidel.securitybook.model.AppUser;
import com.ivanbeidel.securitybook.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SecuritybookApplication implements CommandLineRunner {

	@Autowired
	private AppUserRepository appUserRepository;

	public static void main(String[] args) {

		SpringApplication.run(SecuritybookApplication.class, args);
	}

	@Override
	public void run(String... args){
		createUserIfNotExists("admin@admin.admin", AppRole.ADMIN);
		createUserIfNotExists("user@user.user", AppRole.USER);
	}

	public void createUserIfNotExists(String email, AppRole role){
		if(appUserRepository.findByEmail(email).isEmpty()){
			AppUser account = new AppUser();
			account.setEmail(email);
			account.setPassword(new BCryptPasswordEncoder().encode("pass"));
			account.setRole(role);
			appUserRepository.save(account);
		}
	}
}
