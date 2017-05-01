package com.epam.archive.server.parsers.sax;

import com.epam.archive.models.Person;
import com.epam.archive.models.User;

import java.util.zip.ZipInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class SAX {
//1.load user
//2.load person
	public static User loadUser(ZipInputStream zin) {
		SAXHandler handler = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			handler = new SAXHandler();
			saxParser.parse(zin, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handler.getUser();

	}

	public static Person loadPerson(ZipInputStream zin) {
		SAXHandlerP handler = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			handler = new SAXHandlerP();
			saxParser.parse(zin,handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return handler.getPerson();

	}
}
