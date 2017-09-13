package org.dx4.external.persistence;

import java.util.Date;
import java.util.List;

import org.dx4.external.support.NumberSearchEntry;
import org.dx4.external.support.NumberSearchTerm;
import org.dx4.json.message.Dx4DrawResultJson;
import org.dx4.json.message.Dx4HoroscopeJson;
import org.dx4.json.message.Dx4NumberFloatPayoutJson;
import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4ZodiacJson;

public interface Dx4NumberXDao {
	
	public String getDescForNumber(String number);
	public void getDescsForDrawResult(Dx4DrawResultJson result);
	public List<Dx4NumberPageElementJson> getNumberPageElements();
	public List<Dx4NumberPageElementJson> getNumberPageElementsByDesc(String searchTerm);
	public Dx4NumberPageElementJson getNumberPageElement(String number, Character dictionary);
	public List<Dx4NumberPageElementJson> getNumberPageElementsRange(int num1, int num2, Character dictionary);
	public void updateImage(String code, byte[] image);
	public byte[] getRawImageForNumber(String number);
	public void storeDx4NumberPageElementJson(Dx4NumberPageElementJson npe, byte[] image);
	public void storeNumberSearchTerm(final NumberSearchTerm nst);
	public List<NumberSearchEntry> getNumbersFormTerm(String term);
	
	public void addNumber4D(String number);
	public String getRandom(long playgameId, int digits);
	public void storeZodiacImage(String animal, int set, int year, byte[] image);
	public List<Dx4ZodiacJson> getZodiacs(int set);
	public List<Dx4NumberPageElementJson> getNumberPageElements(String number);
	public void storeHoroscope(Dx4HoroscopeJson hj, byte[] image);
	public List<Dx4HoroscopeJson> getHoroscopes();
	
	public void initializeNumberFloatPayouts(Date lastDrawDate);
	public void updateNumberFloatPayout(final Dx4NumberFloatPayoutJson nfp);
	public List<PayoutBand> createPayoutBands();
	public Dx4NumberFloatPayoutJson getDx4NumberFloatPayoutJson(String number);
	public void insertNumberFloatPayout(Dx4NumberFloatPayoutJson nfp);
	public Dx4NumberFloatPayoutJson getWorstDx4NumberFloatPayoutForBand(int band,Dx4NumberFloatPayoutJson nfp);
	public List<Dx4NumberFloatPayoutJson> getDx4NumberFloatPayoutTrending(int limit);
	public List<PayoutBand> createPayoutBands3();
	
	
}
