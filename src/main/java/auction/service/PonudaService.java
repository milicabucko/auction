package auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auction.model.Ponuda;
import auction.repository.PonudaRepository;

@Service
public class PonudaService {

	@Autowired
	private PonudaRepository ponudaRepository;
	
	public Ponuda save(Ponuda ponuda) {
		return ponudaRepository.save(ponuda);
	}
	
}
