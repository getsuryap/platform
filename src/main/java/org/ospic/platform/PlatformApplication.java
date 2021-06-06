package org.ospic.platform;

import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

@SpringBootApplication(scanBasePackages ={"org.ospic.platform"},
		exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan
@EnableScheduling
public class PlatformApplication implements CommandLineRunner {
	@Resource
	FilesStorageService filesStorageService;
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(PlatformApplication.class);
		application.run( args);
	}
	@Override
	public void run(String... args) throws Exception{
		filesStorageService.init();
	}


}
