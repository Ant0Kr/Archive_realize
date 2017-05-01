package com.epam.archive.server.parsers;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

import com.epam.archive.models.Person;
import com.epam.archive.models.User;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class JDOM {
//1.load user
//2.load Person

	public static User loadUser(ZipInputStream zin) throws JDOMException, IOException {
		User user = new User();
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(zin);
			Element classElement = document.getRootElement();
			List<Element> userList = classElement.getChildren();
			
			user.setLogin(userList.get(0).getValue());
			user.setPassword(userList.get(1).getValue());
			user.setStringParser(userList.get(2).getValue());
			user.setStringRights(userList.get(3).getValue());
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return user;
	}

	public static Person loadPerson(ZipInputStream zin) throws JDOMException, IOException {
		Person person = new Person();
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document document = saxBuilder.build(zin);
			Element classElement = document.getRootElement();
			List<Element> personList = classElement.getChildren();

			person.setSurname(personList.get(0).getValue());
			person.setName(personList.get(1).getValue());
			person.setFathername(personList.get(2).getValue());
			person.setPhone(personList.get(3).getValue());
			person.setEMail(personList.get(4).getValue());
			person.setNameJob(personList.get(5).getValue());
			person.setExperienceJob(personList.get(6).getValue());


		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return person;
	}

}
