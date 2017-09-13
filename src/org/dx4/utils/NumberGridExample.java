package org.dx4.utils;

import java.util.ArrayList;
import java.util.List;

import org.dx4.json.message.Dx4NumberPageElementJson;
import org.dx4.json.message.Dx4NumberStoreJson;
import org.dx4.services.Dx4Services;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class NumberGridExample {

	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
		"Dx4-service.xml");

		Dx4Services dx4Services = (Dx4Services) context.getBean("dx4Services");

		List<Dx4NumberPageElementJson> pageElements = dx4Services.getDx4Home().getNumberPageElements();
		
		NumberGrid ng = NumberGrid.create(4,pageElements);
		List<Dx4NumberPageElementJson> elems = ng.getPageElements(0);
		System.out.println(elems);
		
		System.out.println();
		ArrayList<ArrayList<Dx4NumberStoreJson>> grid = ng.getNumbers();
		for (int i=0; i<grid.size(); i++)
		{
			ArrayList<Dx4NumberStoreJson> row = grid.get(i);
			for (Dx4NumberStoreJson element : row)
			{
				System.out.print(element.getNumber() + " ");
			}
			System.out.println();
		}
		
		Dx4NumberStoreJson test1 = ng.getNumberStore("22");
		Dx4NumberStoreJson test2 = ng.getNumberStore("15");
		
		System.out.println("22 " + test1 + " 15 " + test2);
		
	}
}
