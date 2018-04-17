package auction.service;

import java.io.IOException;
import java.util.Properties;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.App;
import auction.model.Kategorija;

@Service
public class InitializationService {
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private KategorijaService kategorijaService;

	public static String getServerPort() {
		Properties properties = new Properties();
		
		try {
		    //load a properties file from class path, inside static method
			properties.load(App.class.getClassLoader().getResourceAsStream("application.properties"));

		    //get the property value and print it out
		    System.out.println(properties.getProperty("server.port"));
		    return properties.getProperty("server.port");
		} 
		catch (IOException ex) {
		    ex.printStackTrace();
		}
		
		return null;
	}
	
	
	public void initializeGroups() {
		
		identityService.deleteGroup("korisnik");
		identityService.deleteGroup("firma");
		
		Group korisnikG;
		
		korisnikG = identityService.newGroup("korisnik");
		korisnikG.setName("korisnik");
		korisnikG.setType("assigment");
		identityService.saveGroup(korisnikG);
		
		Group firmaG;
		
		firmaG = identityService.newGroup("firma");
		firmaG.setName("firma");
		firmaG.setType("assigment");
		identityService.saveGroup(firmaG);
		
		System.out.println("\nInicijalizacija grupa korisnika!\n");
			
	}
	
	
	public void initializeKategorije() {
		
		kategorijaService.deleteAll();
		
		kategorijaService.save(new Kategorija("prevoz"));
		kategorijaService.save(new Kategorija("obezbedjenje"));
		kategorijaService.save(new Kategorija("volontiranje"));
		
		System.out.println("\nInicijalizacija kategorija!\n");
			
	}
	
}
