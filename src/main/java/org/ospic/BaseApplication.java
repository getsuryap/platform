package org.ospic;

import org.ospic.authentication.roles.Role;
import org.ospic.authentication.users.User;
import org.ospic.authentication.users.repository.UserRepository;
import org.ospic.fileuploads.service.FilesStorageService;
import org.ospic.authentication.roles.repository.RoleRepository;
import org.ospic.patient.infos.domain.Gender;
import org.ospic.patient.infos.repository.GenderInfoRepository;
import org.ospic.util.enums.BedSizeEnums;
import org.ospic.util.enums.RoleEnums;
import org.ospic.ward.beds.domain.BedSize;
import org.ospic.ward.beds.repository.BedSizeRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Autowired
    GenderInfoRepository genderInfoRepository;

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
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
           List<Gender> genders = new ArrayList<>();
           genders.add(new Gender("Male"));
           genders.add(new Gender("Female"));
           genders.add(new Gender("Other"));
           genders.forEach(g ->{
               if (!genderInfoRepository.existsByName(g.getName())){
                   genderInfoRepository.save(g);
               }
           });

        };
    }


}
