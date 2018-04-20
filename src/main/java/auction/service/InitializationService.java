package auction.service;

import java.io.IOException;
import java.util.Properties;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.App;
import auction.model.Kategorija;
import auction.model.Korisnik;

@Service
public class InitializationService {
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private KategorijaService kategorijaService;
	
	@Autowired
	private KorisnikService korisnikService;

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
		
		kategorijaService.save(new Kategorija("proizvodnja pica"));
		kategorijaService.save(new Kategorija("obezbedjenje"));
		kategorijaService.save(new Kategorija("volontiranje"));
		
		System.out.println("\nInicijalizacija kategorija!\n");
			
	}

	public void initializeKorisnike() {
		
		korisnikService.deleteAll();
		
		Korisnik korisnik1 = new Korisnik("John", "McClane", "jm@gmail.com", "aa", "aa", "Los Angeles", "Nakatomi 123/12", "21000", "korisnik", true, null, null, null, 45.267136, 19.833549);
		Korisnik korisnik2 = new Korisnik("Leonardo", "Da Vinci", "ldv@gmail.com", "bb", "bb", "Los Angeles", "Nakatomi 123/12", "21000", "korisnik", true, null, null, null, 45.267136, 20.833549);
		Korisnik korisnik3 = new Korisnik("Tirion", "Lanister", "tl@gmail.com", "cc", "cc", "Los Angeles", "Nakatomi 123/12", "21000", "korisnik", true, null, null, null, 45.267136, 21.833549);

		
		identityService.deleteUser("aa");
		identityService.deleteUser("bb");
		identityService.deleteUser("cc");
		
		korisnikService.save(korisnik1);
		korisnikService.save(korisnik2);
		korisnikService.save(korisnik3);
		
		org.activiti.engine.identity.User activityUser1;
		org.activiti.engine.identity.User activityUser2;
		org.activiti.engine.identity.User activityUser3;
		
		activityUser1 = identityService.newUser(korisnik1.getKorisnickoIme());
		activityUser1.setFirstName(korisnik1.getIme());
		activityUser1.setLastName(korisnik1.getPrezime());
		activityUser1.setEmail(korisnik1.getEmail());
		activityUser1.setPassword(korisnik1.getLozinka());
		identityService.saveUser(activityUser1);
		identityService.createMembership(activityUser1.getId(), korisnik1.getTipKorisnika());
		
		activityUser2 = identityService.newUser(korisnik2.getKorisnickoIme());
		activityUser2.setFirstName(korisnik2.getIme());
		activityUser2.setLastName(korisnik2.getPrezime());
		activityUser2.setEmail(korisnik2.getEmail());
		activityUser2.setPassword(korisnik2.getLozinka());
		identityService.saveUser(activityUser2);
		identityService.createMembership(activityUser2.getId(), korisnik2.getTipKorisnika());
		
		activityUser3 = identityService.newUser(korisnik3.getKorisnickoIme());
		activityUser3.setFirstName(korisnik3.getIme());
		activityUser3.setLastName(korisnik3.getPrezime());
		activityUser3.setEmail(korisnik3.getEmail());
		activityUser3.setPassword(korisnik3.getLozinka());
		identityService.saveUser(activityUser3);
		identityService.createMembership(activityUser3.getId(), korisnik3.getTipKorisnika());
		
		System.out.println("\nInicijalizacija korisnika!\n");	
		
	}
	
	public void initializeFirme() {
		
		Korisnik korisnik1 = new Korisnik("Nektar", "Nektarovic", "nektar@gmail.com", "nektar", "nektar", "Los Angeles", "Nakatomi 123/12", "21000", "firma", true, "Nektar", "proizvodnja pica", 1000.0, 45.267136, 19.833549);
		Korisnik korisnik2 = new Korisnik("Nektar", "Nektarovic", "nektar@gmail.com", "kola", "kola", "Los Angeles", "Nakatomi 123/12", "21000", "firma", true, "Coca Cola", "proizvodnja pica", 1000.0, 45.267136, 20.833549);
		Korisnik korisnik3 = new Korisnik("Nektar", "Nektarovic", "nektar@gmail.com", "next", "next", "Los Angeles", "Nakatomi 123/12", "21000", "firma", true, "Next", "proizvodnja pica", 1000.0, 45.267136, 20.813549);
		Korisnik korisnik4 = new Korisnik("Nektar", "Nektarovic", "nektar@gmail.com", "sky", "sky", "Los Angeles", "Nakatomi 123/12", "21000", "firma", true, "Sky", "proizvodnja pica", 1000.0, 45.267136, 20.833649);

		
		
		identityService.deleteUser("nektar");
		identityService.deleteUser("kola");
		identityService.deleteUser("next");
		identityService.deleteUser("sky");
		
		korisnikService.save(korisnik1);
		korisnikService.save(korisnik2);
		korisnikService.save(korisnik3);
		korisnikService.save(korisnik4);
		
		org.activiti.engine.identity.User activityUser1;
		org.activiti.engine.identity.User activityUser2;
		org.activiti.engine.identity.User activityUser3;
		org.activiti.engine.identity.User activityUser4;
		
		activityUser1 = identityService.newUser(korisnik1.getKorisnickoIme());
		activityUser1.setFirstName(korisnik1.getIme());
		activityUser1.setLastName(korisnik1.getPrezime());
		activityUser1.setEmail(korisnik1.getEmail());
		activityUser1.setPassword(korisnik1.getLozinka());
		identityService.saveUser(activityUser1);
		identityService.createMembership(activityUser1.getId(), korisnik1.getTipKorisnika());
		
		activityUser2 = identityService.newUser(korisnik2.getKorisnickoIme());
		activityUser2.setFirstName(korisnik2.getIme());
		activityUser2.setLastName(korisnik2.getPrezime());
		activityUser2.setEmail(korisnik2.getEmail());
		activityUser2.setPassword(korisnik2.getLozinka());
		identityService.saveUser(activityUser2);
		identityService.createMembership(activityUser2.getId(), korisnik2.getTipKorisnika());
		
		activityUser3 = identityService.newUser(korisnik3.getKorisnickoIme());
		activityUser3.setFirstName(korisnik3.getIme());
		activityUser3.setLastName(korisnik3.getPrezime());
		activityUser3.setEmail(korisnik3.getEmail());
		activityUser3.setPassword(korisnik3.getLozinka());
		identityService.saveUser(activityUser3);
		identityService.createMembership(activityUser3.getId(), korisnik3.getTipKorisnika());
		
		activityUser4 = identityService.newUser(korisnik4.getKorisnickoIme());
		activityUser4.setFirstName(korisnik4.getIme());
		activityUser4.setLastName(korisnik4.getPrezime());
		activityUser4.setEmail(korisnik4.getEmail());
		activityUser4.setPassword(korisnik4.getLozinka());
		identityService.saveUser(activityUser4);
		identityService.createMembership(activityUser4.getId(), korisnik4.getTipKorisnika());
		
		
		System.out.println("\nInicijalizacija firmi!\n");
		
	}
	
}
