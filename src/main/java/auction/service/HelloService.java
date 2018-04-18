package auction.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
	
	public ArrayList<String> makeArray() {
		ArrayList<String> elementi = new ArrayList<>();
		elementi.add("1");
		elementi.add("2");
		elementi.add("3");
		return elementi;
	}
	
	public void helloWorld(String kome) {
		System.out.println("Hello World " + kome);
	}

}
