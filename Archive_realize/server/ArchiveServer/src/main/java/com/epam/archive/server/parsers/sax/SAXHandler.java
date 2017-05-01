package com.epam.archive.server.parsers.sax;

import com.epam.archive.models.User;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

	private boolean loginfl = false;
	private boolean passwordfl = false;
	private boolean parserfl = false;
	private boolean rightsfl = false;
	private User user = new User();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("models.User")) {

		} else if (qName.equalsIgnoreCase("login")) {
			loginfl = true;
		} else if (qName.equalsIgnoreCase("password")) {
			passwordfl = true;
		} else if (qName.equalsIgnoreCase("parser")) {
			parserfl = true;
		} else if (qName.equalsIgnoreCase("rights")) {
			rightsfl = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("models.User")) {
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (loginfl) {
			user.setLogin(new String(ch, start, length));
			loginfl = false;
		} else if (passwordfl) {
			user.setPassword(new String(ch, start, length));
			passwordfl = false;
		} else if (parserfl) {
			user.setStringParser(new String(ch, start, length));
			parserfl = false;
		} else if (rightsfl) {
			user.setStringRights(new String(ch, start, length));
			rightsfl = false;
		}
	}
	
	public User getUser(){
		return user;
	}

	
}
