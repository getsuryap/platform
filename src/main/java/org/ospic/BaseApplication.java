package org.ospic;

import org.ospic.security.authentication.roles.domain.Role;
import org.ospic.security.authentication.roles.repository.RoleRepository;
import org.ospic.security.authentication.users.domain.User;
import org.ospic.security.authentication.users.repository.UserRepository;
import org.ospic.fileuploads.service.FilesStorageService;
import org.ospic.util.enums.RoleEnums;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

@SpringBootApplication(scanBasePackages ={"org.ospic"},
exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan
public class BaseApplication implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Resource
    FilesStorageService filesStorageService;
    @Autowired
    UserRepository userRepository;


    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BaseApplication.class);
        Properties properties = new Properties();

        LocalDate date = LocalDate.now();

        String logFile = String.format("logs/%2d-%2d-%2d.log", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        properties.setProperty("spring.main.banner-mode", "log");
        properties.setProperty("logging.level.root", "warn");
        properties.setProperty("logging.level.org.hibernate","error");
        properties.setProperty("logging.file", logFile);
        application.setDefaultProperties(properties);
        application.run( args);
    }
    @Override
    public void run(String... args) throws Exception{
        filesStorageService.init();
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            for (RoleEnums roleEnums : RoleEnums.values()) {
                if (!roleRepository.existsByName(roleEnums)) {
                    roleRepository.save(new Role(roleEnums, roleEnums.name()));
                }
            }
           if (!userRepository.existsByUsername("admin")){
               User user = new User();
               user.setUsername("admin");
               user.setPassword(passwordEncoder.encode("password"));
               user.setEmail("admin@test.com");
               List<Role> roleList = roleRepository.findAll();
               user.setRoles(roleList);
               userRepository.save(user);
           }

        };
    }


}
