package com.telemedicicne.telemedicicne;

import com.telemedicicne.telemedicicne.Config.AppConstants;
import com.telemedicicne.telemedicicne.Entity.Role;
import com.telemedicicne.telemedicicne.Repository.RoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TelemedicicneApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;

	private static final Logger LOG = LoggerFactory.getLogger(TelemedicicneApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(TelemedicicneApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//        System.out.println(this.passwordEncoder.encode("abc"));
		try {

			Role role = new Role();
			role.setId(AppConstants.ADMIN_USER);
			role.setName("ROLE_ADMIN");

			Role role1 = new Role();
			role1.setId(AppConstants.NORMAL_USER);
			role1.setName("ROLE_NORMAL");

			//List<Role> roles = List<role>;
			List<Role> roles = new ArrayList<Role>();
			//List<Role> roles = List.of(role,role1);
			roles.add(role);
			roles.add(role1);
			List<Role> result = this.roleRepo.saveAll(roles);

			result.forEach(r -> {
				System.out.println(r.getName());
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

		printLog();
	}
	private static void printLog() {
		LOG.debug("Debug Message");
		LOG.warn("Warn Message");
		LOG.error("Error Message");
		LOG.info("Info Message");
		LOG.trace("Trace Message");
	}

}
