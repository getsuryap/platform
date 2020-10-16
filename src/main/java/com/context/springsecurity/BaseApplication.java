package com.context.springsecurity;

import com.context.springsecurity.domain.Role;
import com.context.springsecurity.fileuploads.service.FilesStorageService;
import com.context.springsecurity.repository.RoleRepository;
import com.context.springsecurity.util.enums.BedSizeEnums;
import com.context.springsecurity.util.enums.RoleEnums;
import com.context.springsecurity.ward.beds.domain.BedSize;
import com.context.springsecurity.ward.beds.repository.BedSizeRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

@SpringBootApplication(
        scanBasePackages = "com.context.springsecurity",
exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan
public class BaseApplication implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BedSizeRepository bedsRepository;
    @Resource
    FilesStorageService filesStorageService;

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception{
        //filesStorageService.deleteAll();
        filesStorageService.init();
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            for (RoleEnums roleEnums : RoleEnums.values()) {
                if (!roleRepository.existsByName(roleEnums)) {
                    roleRepository.save(new Role(roleEnums));
                }
            }
            for (BedSizeEnums bedSizeEnums: BedSizeEnums.values()){
                if(!bedsRepository.existsByName(bedSizeEnums)){
                    bedsRepository.save(new BedSize(bedSizeEnums));
                }
            }

        };
    }


}
