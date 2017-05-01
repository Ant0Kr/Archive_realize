package com.epam.archive.server;

import java.net.Socket;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import com.epam.archive.models.Person;
import com.epam.archive.models.User;
import com.epam.archive.service.Validation;
import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

public class MainServer {

	private static LinkedList<User> userList;
	private static LinkedList<Person> archiveCatalog;
	final static int PORT = 5555;
	private static final Logger log = Logger.getLogger(MainServer.class);

	public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException, JDOMException {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (Exception e) {
			System.out.println("Server already connected");
			log.error("Server already connected.");
			return;
		}
		System.out.println("Hello I'm server!");
		initUsersCatalog();
		initArchiveCatalog();

		System.out.println("Server connected!");
		log.info("Server connected!");

		while (true) {
			socket = serverSocket.accept();
			
			ServerThread thread = new ServerThread(socket);
			thread.start();
		}
	}

	public static void initArchiveCatalog() throws ParserConfigurationException, SAXException, IOException {

		archiveCatalog = new LinkedList<Person>();
		File[] fList;
		File file = new File("Data\\Archive");
		fList = file.listFiles();
		for (int i = 0; i < fList.length; i++) {
			if (fList[i].isFile()) {
				String way = fList[i].getName();
				String newWay = new String();
				int j = 0;
				while (way.charAt(j) != '.') {
					newWay = newWay + way.charAt(j);
					j++;
				}
				if (Validation.checkValid("Data\\Person.xsd", fList[i].toString(), "Data\\")) {
					System.out.println(newWay + ".xml is validate!");
					log.info(newWay + ".xml is validate!");
					archiveCatalog.add(ParserHandler.loadPerson(newWay));
				} else {
					System.out.println(newWay + ".xml not validate!");
					log.info(newWay + ".xml not validate!");
				}

			}

		}
	}

	public static void initUsersCatalog() throws SAXException, IOException, ParserConfigurationException, JDOMException {

		userList = new LinkedList<User>();
		File[] fList;
		File file = new File("Data\\Users");
		fList = file.listFiles();
		for (int i = 0; i < fList.length; i++) {
			if (fList[i].isFile()) {
				String way = fList[i].getName();
				String newWay = new String();
				int j = 0;
				while (way.charAt(j) != '.') {
					newWay = newWay + way.charAt(j);
					j++;
				}
				
				if (Validation.checkValid("Data\\User.xsd", fList[i].toString(), "Data\\")) {
					System.out.println(newWay + ".xml is validate!");
					log.info(newWay + ".xml is validate!");
					userList.add(ParserHandler.loadUser(newWay));
				} else {
					System.out.println(newWay + ".xml not validate!");
					log.info(newWay + ".xml not validate!");
				}
				
			}

		}
	}
	
	public static LinkedList<Person> getCatalog() {
		return archiveCatalog;
	}

	public static LinkedList<User> getUsersCatalog() {
		return userList;
	}
	
}