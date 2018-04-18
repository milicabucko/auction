package auction.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import auction.model.Constants;
import auction.model.Korisnik;
import auction.model.ZahtevZaNabavku;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private KorisnikService korisnikService;
	
	public void sendConfirmationMail(Korisnik korisnik, String task) {
			
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setFrom("boxboux@gmail.com");
			messageHelper.setTo("boxboux@gmail.com");
			messageHelper.setSubject("Aktivacioni mejl - AukcijaApp");
			messageHelper.setText(confirmationMailMessageText(korisnik, task));
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Aktivacioni mejl uspesno poslat!");
		
	}

	public String confirmationMailMessageText(Korisnik korisnik, String task) {
		
		StringBuilder message = new StringBuilder();
		message.append("Postovani " + korisnik.getIme() + ", ");
		message.append("\n\n");
		message.append("Kako biste potvrdili registraciju kliknite na link ispod: ");
		message.append("\n\n");
		message.append("http://localhost:" + InitializationService.getServerPort() 
		+ "/registration/confirmRegistration?korisnickoIme=" + korisnik.getKorisnickoIme() 
		+ "&task=" + task);
		message.append("\n\n");
		message.append("Hvala Vam, ");
		message.append("\n");
		message.append("AukcijaApp");
		
		return message.toString();
		
	}
	
	public ArrayList<String> noCompaniesFromCategoryMail(ZahtevZaNabavku zahtevZaNabavku, Korisnik korisnik) {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setFrom("boxboux@gmail.com");
			messageHelper.setTo("boxboux@gmail.com");
			messageHelper.setSubject("Odbijen zahtjev - AukcijaApp");
			messageHelper.setText(noCompaniesFromCategoryText(zahtevZaNabavku, korisnik));
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Mejl neuspeha uspesno poslat!");
		
		HashMap<String, Object> mapa = new HashMap<>();
		mapa.put("firme", new ArrayList<>());
		
		ArrayList<String> firme = new ArrayList<>();
		return firme;
		
	}
	
	
	public String noCompaniesFromCategoryText(ZahtevZaNabavku zahtevZaNabavku, Korisnik korisnik) {
		
		StringBuilder message = new StringBuilder();
		message.append("Postovani " + korisnik.getIme() + ", ");
		message.append("\n\n");
		message.append("Vas zahtev za nabavku trenutno nije moguce izvrsiti. Ne postoji nijedna firma iz kategorije : ");
		message.append("\n\n");
		message.append(zahtevZaNabavku.getKategorija());
		message.append("\n\n");
		message.append("Hvala Vam, ");
		message.append("\n");
		message.append("AukcijaApp");
		
		return message.toString();
		
	}
	
	// manje firmi nego ponuda mail
	public ArrayList<String> lackOfFirmsMail(ZahtevZaNabavku zahtevZaNabavku, Korisnik korisnik, ArrayList<Korisnik> firmee, String task) {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setFrom("boxboux@gmail.com");
			messageHelper.setTo("boxboux@gmail.com");
			messageHelper.setSubject("Upozorenje - AukcijaApp");
			messageHelper.setText(lackOfFirmsMailText(zahtevZaNabavku, korisnik, firmee, task));
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Mejl upozorenja uspesno poslat!");
		
		ArrayList<String> firme = new ArrayList<>();
		for (Korisnik k : firmee) {
			firme.add(k.getImeFirme());
		}
		return firme;
		
	}
	
	
	public String lackOfFirmsMailText(ZahtevZaNabavku zahtevZaNabavku, Korisnik korisnik, ArrayList<Korisnik> firme, String task) {
		
		StringBuilder message = new StringBuilder();
		message.append("Postovani " + korisnik.getIme() + ", ");
		message.append("\n\n");
		message.append("Broj firmi koje ispunjavaju uslove iz zahteva je manji od minimalnog broja ponuda koji ste naveli u zahtevu. Trenutne firme koje ispunjavaju zahtev su: ");
		message.append("\n");
		for (Korisnik firma : firme) {
			message.append("\n- " + firma.getImeFirme());
		}
		message.append("\n\n");
		message.append("Ukoliko se slazete da posaljete ponude na navedene firme kliknite na link ispod: ");
		message.append("\n\n");
		message.append("http://localhost:" + InitializationService.getServerPort() 
		+ "/aukcija/lackOfFirmsDecision?decision=" + Constants.YES 
		+ "&task=" + task);
		message.append("\n\n");
		message.append("Ukoliko se ne slazete, kliknite na link ispod: ");
		message.append("\n\n");
		message.append("http://localhost:" + InitializationService.getServerPort() 
		+ "/aukcija/lackOfFirmsDecision?decision=" + Constants.NO 
		+ "&task=" + task);
		message.append("\n\n");
		message.append("Hvala Vam, ");
		message.append("\n");
		message.append("AukcijaApp");
		
		return message.toString();
		
	}
	
	public String posaljiZahtevZaNabavkuFirmi(ZahtevZaNabavku zahtevZaNabavku, String imeFirme) {
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper;
		try {
			messageHelper = new MimeMessageHelper(message, true);
			messageHelper.setFrom("boxboux@gmail.com");
			messageHelper.setTo("boxboux@gmail.com");
			messageHelper.setSubject("Upozorenje - AukcijaApp");
			messageHelper.setText(posaljiZahtevZaNabavkuFirmiText(zahtevZaNabavku, imeFirme));
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		System.out.println("Mejl zahteva uspesno poslat!");
		
		Korisnik korisnik = korisnikService.findByImeFirme(imeFirme);
		//ovo je zapravo asignee koji ce biti result varijabla service taska
		User user = identityService.createUserQuery().userId(korisnik.getKorisnickoIme()).singleResult();
		return user.getId();
		
	}
	
	
	public String posaljiZahtevZaNabavkuFirmiText(ZahtevZaNabavku zahtevZaNabavku, String imeFirme) {
		
		StringBuilder message = new StringBuilder();
		message.append("Postovani " + imeFirme + ", ");
		message.append("\n\n");
		message.append("Dobili ste zahtev od: " + zahtevZaNabavku.getKorisnik().getIme());
		message.append("\n");
		message.append("Na vasem profilu mozete popuniti ponudu ili odbiti zahtev!");
		message.append("\n\n");
		message.append("Hvala Vam, ");
		message.append("\n");
		message.append("AukcijaApp");
		
		return message.toString();
		
	}
	
	
	
	
}
