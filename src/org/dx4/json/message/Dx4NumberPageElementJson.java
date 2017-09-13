package org.dx4.json.message;

import java.util.ArrayList;
import java.util.List;

public class Dx4NumberPageElementJson 
{
	public static char DICTIONARYALL = 'A';
	public static char DICTIONARYSTANDARD3 = 'S';
	public static char DICTIONARYTRADITIONAL3 = 'T';
	public static char DICTIONARYMODERN4 = 'M';
	
	private List<Dx4NumberStoreJson> numbers;
	private Integer number;
	private String token;
	private String description;
	private String descriptionCh;
	private String image;
	private char dictionary;

	public Dx4NumberPageElementJson()
	{
		numbers = new ArrayList<Dx4NumberStoreJson>();
	}
	
	public Dx4NumberPageElementJson(String token, String description) {
		this();
		this.token = token;
		this.description = description;
	}

	public List<Dx4NumberStoreJson> getNumbers() {
		return numbers;
	}
	public void setNumbers(List<Dx4NumberStoreJson> numbers) {
		this.numbers = numbers;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getNumber() {
		return number;
	}

	public void setDescriptionCh(String descriptionCh) {
		this.descriptionCh = descriptionCh;
	}

	public String getDescriptionCh() {
		return descriptionCh;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public char getDictionary() {
		return dictionary;
	}

	public void setDictionary(char dictionary) {
		this.dictionary = dictionary;
	}

	@Override
	public String toString() {
		return "Dx4NumberPageElementJson [number=" + number + ", description=" + description + "]";
	}
	 
	
}
