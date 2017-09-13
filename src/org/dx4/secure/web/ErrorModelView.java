package org.dx4.secure.web;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

public class ErrorModelView 
{
	public static ModelAndView createErrorStackTrace(String message,Exception e)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);
		String content = baos.toString();
		List<String> msgs = new ArrayList<String>();
		msgs.add(message);
		msgs.add(content);
		return new ModelAndView("dx4Error" , "msgs", msgs);
	}
}
