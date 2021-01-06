package org.ospic.platform;

import org.ospic.platform.fileuploads.service.FilesStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Properties;

@SpringBootApplication(scanBasePackages ={"org.ospic"},
		exclude = HibernateJpaAutoConfiguration.class)
@ComponentScan
public class PlatformApplication implements CommandLineRunner {
	@Resource
	FilesStorageService filesStorageService;



	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(PlatformApplication.class);
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
