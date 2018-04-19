package auction.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class PointsService {

	
	public long dayDifferenceBetweenDates(String datumPonude, String datumZahteva) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date firstDate = sdf.parse(datumPonude);
        Date secondDate = sdf.parse(datumZahteva);
        
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        
        return diff;
	}
	
	public Double points (String datumPonude, String datumZahteva, Double cenaPonude, Double cenaZahteva) throws ParseException {
		
		Double points = 0.0;
		long diff = dayDifferenceBetweenDates(datumPonude, datumZahteva);
		
		if (datumPonude.compareTo(datumZahteva) > 0) {       //posle zadatog roka (lose!)
            points += Math.abs(diff) * 10;
        }
        else if (datumPonude.compareTo(datumZahteva) < 0) {      //prije zadatog roka (dobro!)
            points += Math.abs(diff) * 100;
        }
        else {                              //u zadatom roku
            points += 0;
        }
		
		double resultCena = cenaPonude - cenaZahteva;
        if (resultCena < 0) {
        	points += Math.abs(resultCena) * 10;
        }
        else if (resultCena > 0) {
        	points += Math.abs(resultCena);
        }
        else {
        	points += 0;
        }
		
		return points;
	}
	
}