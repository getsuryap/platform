package org.ospic;

import org.ospic.security.authentication.roles.domain.Role;
import org.ospic.security.authentication.roles.privileges.domains.Privilege;
import org.ospic.security.authentication.roles.privileges.repository.PrivilegesRepository;
import org.ospic.security.authentication.roles.repository.RoleRepository;
import org.ospic.security.authentication.users.domain.User;
import org.ospic.security.authentication.users.repository.UserRepository;
import org.ospic.fileuploads.service.FilesStorageService;
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
    @Resource
    FilesStorageService filesStorageService;



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


}
