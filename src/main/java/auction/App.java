package auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import auction.service.InitializationService;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class, args);
		applicationContext.getBean(InitializationService.class).initializeGroups();
		applicationContext.getBean(InitializationService.class).initializeKategorije();
		applicationContext.getBean(InitializationService.class).initializeKorisnike();
		applicationContext.getBean(InitializationService.class).initializeFirme();
	}

}
