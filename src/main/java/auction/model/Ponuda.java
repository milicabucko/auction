package auction.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Ponuda {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private ZahtevZaNabavku zahtevZaNabavku;
	
	@ManyToOne
	private Korisnik firma;
	
	private String status;
	
	private Double cena;
	
	private String rokZaIzvrsavanje;
	
	private Double poeni;
	
	public Ponuda() {
		// TODO Auto-generated constructor stub
	}

	public Ponuda(ZahtevZaNabavku zahtevZaNabavku, Korisnik firma, String status, Double cena, String rokZaIzvrsavanje,
			Double poeni) {
		super();
		this.zahtevZaNabavku = zahtevZaNabavku;
		this.firma = firma;
		this.status = status;
		this.cena = cena;
		this.rokZaIzvrsavanje = rokZaIzvrsavanje;
		this.poeni = poeni;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ZahtevZaNabavku getZahtevZaNabavku() {
		return zahtevZaNabavku;
	}

	public void setZahtevZaNabavku(ZahtevZaNabavku zahtevZaNabavku) {
		this.zahtevZaNabavku = zahtevZaNabavku;
	}

	public Korisnik getFirma() {
		return firma;
	}

	public void setFirma(Korisnik firma) {
		this.firma = firma;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getCena() {
		return cena;
	}

	public void setCena(Double cena) {
		this.cena = cena;
	}

	public String getRokZaIzvrsavanje() {
		return rokZaIzvrsavanje;
	}

	public void setRokZaIzvrsavanje(String rokZaIzvrsavanje) {
		this.rokZaIzvrsavanje = rokZaIzvrsavanje;
	}

	public Double getPoeni() {
		return poeni;
	}

	public void setPoeni(Double poeni) {
		this.poeni = poeni;
	}
	
}
