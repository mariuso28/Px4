package org.dx4.json.message;

import java.util.List;

public class Dx4ZodiacJson {

	public static String[] animalNames = { "Rat", "Ox", "Tiger", "Rabbit", "Dragon", "Snake", "Horse", "Goat", "Monkey", "Rooster", "Dog", "Pig" };
	
	private String animal;
	private String cAnimal;
	private int set;					// 1-3
	private int year;					// 1 - 12
	private String image;
	private List<String> numbers;
	
	public Dx4ZodiacJson()
	{
	}

	public String getAnimal() {
		return animal;
	}

	public void setAnimal(String animal) {
		this.animal = animal;
	}

	public int getSet() {
		return set;
	}

	public void setSet(int set) {
		this.set = set;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public static String[] getAnimalNames() {
		return animalNames;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<String> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}

	public String getcAnimal() {
		return cAnimal;
	}

	public void setcAnimal(String cAnimal) {
		this.cAnimal = cAnimal;
	}

	
}
