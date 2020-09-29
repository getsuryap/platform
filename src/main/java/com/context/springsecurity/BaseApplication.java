package com.context.springsecurity;

import com.context.springsecurity.ward.beds.domain.BedSize;
import com.context.springsecurity.enums.BedSizeEnums;
import com.context.springsecurity.enums.RoleEnums;
import com.context.springsecurity.domain.Role;
import com.context.springsecurity.repository.RoleRepository;
import com.context.springsecurity.ward.beds.repository.BedSizeRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.context.springsecurity")
@ComponentScan
public class BaseApplication {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BedSizeRepository bedsRepository;

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
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
