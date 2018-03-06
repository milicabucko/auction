package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gs.collections.impl.list.mutable.ArrayListAdapter;

@Entity
public class Firm implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String ime;
	
	private Integer udaljenost;
	
	@ManyToOne
	private Category kategorija;
	
	@JsonIgnore
	@OneToMany
	private List<User> korisnici = new ArrayList();
	
	public Firm() {
		// TODO Auto-generated constructor stub
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

	public Integer getUdaljenost() {
		return udaljenost;
	}

	public void setUdaljenost(Integer udaljenost) {
		this.udaljenost = udaljenost;
	}

	public Category getKategorija() {
		return kategorija;
	}

	public void setKategorija(Category kategorija) {
		this.kategorija = kategorija;
	}

	public List<User> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(List<User> korisnici) {
		this.korisnici = korisnici;
	}
	
	
}
