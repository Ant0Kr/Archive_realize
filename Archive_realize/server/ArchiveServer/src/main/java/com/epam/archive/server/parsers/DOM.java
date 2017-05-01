package com.epam.archive.server.parsers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;

import com.epam.archive.models.Person;
import com.epam.archive.models.User;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DOM {
//1.load user
//2.load person
//3.save person
//4.save user
	public static Person loadPerson(ZipInputStream zin) throws ParserConfigurationException, SAXException, IOException {

		Person person = new Person();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(zin);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("models.Person");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				person.setSurname(eElement.getElementsByTagName("surname").item(0).getTextContent());
				person.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
				person.setFathername(eElement.getElementsByTagName("fathername").item(0).getTextContent());
				person.setPhone(eElement.getElementsByTagName("phone").item(0).getTextContent());
				person.setEMail(eElement.getElementsByTagName("email").item(0).getTextContent());
				person.setNameJob(eElement.getElementsByTagName("nameJob").item(0).getTextContent());
				person.setExperienceJob(eElement.getElementsByTagName("experienceJob").item(0).getTextContent());
			}

		}

		return person;

	}

	public static User loadUser(ZipInputStream zin) throws ParserConfigurationException, SAXException, IOException {
		User user = new User();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(zin);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName("models.User");
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				user.setLogin(eElement.getElementsByTagName("login").item(0).getTextContent());
				user.setPassword(eElement.getElementsByTagName("password").item(0).getTextContent());
				user.setStringParser(eElement.getElementsByTagName("parser").item(0).getTextContent());
				user.setStringRights(eElement.getElementsByTagName("rights").item(0).getTextContent());
			}

		}
		return user;
	}

	public static void savePerson(Person person) {
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("models.Person");
			doc.appendChild(rootElement);

			Element surname = doc.createElement("surname");
			surname.appendChild(doc.createTextNode(person.getSurname()));
			rootElement.appendChild(surname);
			
			Element name = doc.createElement("name");
			name.appendChild(doc.createTextNode(person.getName()));
			rootElement.appendChild(name);
			
			Element fathername = doc.createElement("fathername");
			fathername.appendChild(doc.createTextNode(person.getFathername()));
			rootElement.appendChild(fathername);
			
			Element phone = doc.createElement("phone");
			phone.appendChild(doc.createTextNode(person.getPhone()));
			rootElement.appendChild(phone);
			
			Element email = doc.createElement("email");
			email.appendChild(doc.createTextNode(person.getEMail()));
			rootElement.appendChild(email);
			
			Element nameJob = doc.createElement("nameJob");
			nameJob.appendChild(doc.createTextNode(person.getNameJob()));
			rootElement.appendChild(nameJob);
			
			Element experienceJob = doc.createElement("experienceJob");
			experienceJob.appendChild(doc.createTextNode(person.getExperienceJob()));
			rootElement.appendChild(experienceJob);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Data\\Archive\\"+person.getSurname()+"_"+
			person.getName()+"_"+person.getFathername()+".xml"));
			transformer.transform(source, result);
			// Output to console for testing
			FileOutputStream fStream = new FileOutputStream(new String("Data\\Archive\\"+person.getSurname()+"_"+
					person.getName()+"_"+person.getFathername()+".zip"));
			ZipOutputStream zipOutpStream = new ZipOutputStream(fStream);
			
			byte [] buf = Files.readAllBytes(Paths.get("Data\\Archive\\"+person.getSurname()+"_"+
					person.getName()+"_"+person.getFathername()+".xml"));
			
			ZipEntry zipEntry = new ZipEntry(new String(person.getSurname()+"_"+
					person.getName()+"_"+person.getFathername()+".xml"));
			
			
			try {
				zipOutpStream.putNextEntry(zipEntry);
				zipOutpStream.write(buf);
				zipOutpStream.flush();
				//zipOutpStream.closeEntry();
				zipOutpStream.close();
				fStream.close();
				File file = new File("Data\\Archive\\"+person.getSurname()+"_"+
						person.getName()+"_"+person.getFathername()+".xml");
				file.delete();
				
			} catch (IOException e) {
				e.printStackTrace();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveUser(User user) {
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElement = doc.createElement("models.User");
			doc.appendChild(rootElement);

			Element login = doc.createElement("login");
			login.appendChild(doc.createTextNode(user.getLogin()));
			rootElement.appendChild(login);
			
			Element password = doc.createElement("password");
			password.appendChild(doc.createTextNode(user.getPassword()));
			rootElement.appendChild(password);
			
			Element parser = doc.createElement("parser");
			parser.appendChild(doc.createTextNode(user.getParser().toString()));
			rootElement.appendChild(parser);
			
			Element rights = doc.createElement("rights");
			rights.appendChild(doc.createTextNode(user.getRights().toString()));
			rootElement.appendChild(rights);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Data\\Users\\"+user.getLogin()+".xml"));
			transformer.transform(source, result);
			// Output to console for testing
			FileOutputStream fStream = new FileOutputStream(new String("Data\\Users\\"+user.getLogin()+".zip"));
			ZipOutputStream zipOutpStream = new ZipOutputStream(fStream);
			
			byte [] buf = Files.readAllBytes(Paths.get("Data\\Users\\"+user.getLogin()+".xml"));
			
			ZipEntry zipEntry = new ZipEntry(new String(user.getLogin()+".xml"));
			
			
			try {
				zipOutpStream.putNextEntry(zipEntry);
				zipOutpStream.write(buf);
				zipOutpStream.flush();
				//zipOutpStream.closeEntry();
				zipOutpStream.close();
				fStream.close();
				File file = new File("Data\\Users\\"+user.getLogin()+".xml");
				file.delete();
				
			} catch (IOException e) {
				e.printStackTrace();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}