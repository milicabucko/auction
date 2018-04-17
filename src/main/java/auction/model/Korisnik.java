package auction.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Korisnik implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String ime;
	
	private String prezime;
	
	private String email;
	
	private String lozinka;
	
	private String korisnickoIme;
	
	private String grad;
	
	private String adresa;
	
	private String postanskiBroj;
	
	private String tipKorisnika;
	
	private Boolean potvrdjenMail;
	
	private String imeFirme;
	
	private String kategorija;
	
	private Double udaljenost; 
	
	public Korisnik() {
		// TODO Auto-generated constructor stub
	}

	public Korisnik(String ime, String prezime, String email, String lozinka, String korisnickoIme,
			String grad, String adresa, String postanskiBroj, String tipKorisnika, Boolean potvrdjenMail,
			String imeFirme, String kategorija, Double udaljenost) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.email = email;
		this.lozinka = lozinka;
		this.korisnickoIme = korisnickoIme;
		this.grad = grad;
		this.adresa = adresa;
		this.postanskiBroj = postanskiBroj;
		this.tipKorisnika = tipKorisnika;
		this.potvrdjenMail = potvrdjenMail;
		this.imeFirme = imeFirme;
		this.kategorija = kategorija;
		this.udaljenost = udaljenost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getPostanskiBroj() {
		return postanskiBroj;
	}

	public void setPostanskiBroj(String postanskiBroj) {
		this.postanskiBroj = postanskiBroj;
	}

	public String getTipKorisnika() {
		return tipKorisnika;
	}

	public void setTipKorisnika(String tipKorisnika) {
		this.tipKorisnika = tipKorisnika;
	}

	public Boolean getPotvrdjenMail() {
		return potvrdjenMail;
	}

	public void setPotvrdjenMail(Boolean potvrdjenMail) {
		this.potvrdjenMail = potvrdjenMail;
	}

	public String getImeFirme() {
		return imeFirme;
	}

	public void setImeFirme(String imeFirme) {
		this.imeFirme = imeFirme;
	}

	public String getKategorija() {
		return kategorija;
	}

	public void setKategorija(String kategorija) {
		this.kategorija = kategorija;
	}

	public Double getUdaljenost() {
		return udaljenost;
	}

	public void setUdaljenost(Double udaljenost) {
		this.udaljenost = udaljenost;
	}

}
