package com.epam.archive.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.TransformerException;

import com.epam.archive.models.Person;
import com.epam.archive.models.Request;
import com.epam.archive.models.User;
import com.epam.archive.server.parsers.DOM;
import com.epam.archive.service.SerializeMaker;
import org.apache.log4j.Logger;


public class ServerThread extends Thread {

	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private static Vector<ServerThread> allAttachedThreads = new Vector<ServerThread>();
	private static final Logger log = Logger.getLogger(ParserHandler.class);

	public ServerThread(Socket socket) throws IOException {
		this.socket = socket;
		allAttachedThreads.addElement(this);
		in = new DataInputStream(this.socket.getInputStream());
		out = new DataOutputStream(this.socket.getOutputStream());
	}

	public void run() {

		while (true) {
			String serializeObj = new String();
			String requestName = new String();
			try {
				serializeObj = in.readUTF();

			} catch (IOException e) {
				return;
			}
			Request request = SerializeMaker.deserializeFromXML(serializeObj);
			requestName = request.getRequestName();

			if(requestName.equals("SHOWALL"))
				showAll();
			else if(requestName.equals("EXIT"))
				return;
			else if(requestName.equals("DELETE"))
				delete(request);
			else if(requestName.equals("ADDPERSON"))
				addPerson(request);
			else if(requestName.equals("EDIT"))
				edit(request);
			else if(requestName.equals("SEARCH"))
				search(request);
			else if(requestName.equals("USERVALIDATE"))
				userValidate(request);
			else if(requestName.equals("GETUSERTABLE"))
				getUserTable();
			else if(requestName.equals("ADDUSER")) {
				try {
					addUser(request);
				} catch (TransformerException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					e.printStackTrace();
				}
			}
			else if(requestName.equals("CHANGERIGHTS"))
				changeRights(request);
			else if(requestName.equals("CHANGEPARSER"))
				changeParser(request);

			log.info("Server handled '"+requestName+"' request!");
		}

	}

	private void showAll() {
		synchronized (MainServer.getCatalog()) {
			try {
				out.writeUTF(SerializeMaker.serializeToXML(MainServer.getCatalog()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void delete(Request request) {

		Person object = request.getOldPerson();
		LinkedList<Person> catalog = MainServer.getCatalog();
		synchronized (catalog) {
			for (int i1 = 0; i1 < catalog.size(); i1++) {

				if (catalog.get(i1).getSurname().equals(object.getSurname())
						&& catalog.get(i1).getName().equals(object.getName())
						&& catalog.get(i1).getFathername().equals(object.getFathername())
						&& catalog.get(i1).getEMail().equals(object.getEMail())) {
					File file = new File("Data\\Archive\\" + catalog.get(i1).getSurname() + "_"
							+ catalog.get(i1).getName() + "_" + catalog.get(i1).getFathername() + ".zip");
					if (file.delete())
						catalog.remove(i1);
					else
						System.out.println("Error of delete!File not found!");
					break;
				}
			}
		}
		try {
			out.writeUTF(SerializeMaker.serializeToXML(catalog));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addPerson(Request request) {

		Person person = request.getOldPerson();
		LinkedList<Person> list = MainServer.getCatalog();
		synchronized (list) {

			list.add(person);
			DOM.savePerson(person);
			try {
				out.writeUTF(SerializeMaker.serializeToXML(list));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void edit(Request request) {

		Person newPerson = request.getNewPerson();
		Person oldPerson = request.getOldPerson();
		LinkedList<Person> llist = MainServer.getCatalog();

		synchronized (llist) {

			for (int j = 0; j < llist.size(); j++) {
				if (llist.get(j).getSurname().equals(oldPerson.getSurname())
						&& llist.get(j).getName().equals(oldPerson.getName())
						&& llist.get(j).getFathername().equals(oldPerson.getFathername())
						&& llist.get(j).getPhone().equals(oldPerson.getPhone())
						&& llist.get(j).getEMail().equals(oldPerson.getEMail())
						&& llist.get(j).getNameJob().equals(oldPerson.getNameJob())
						&& llist.get(j).getExperienceJob().equals(oldPerson.getExperienceJob())) {

					File file = new File("Data\\Archive\\" + llist.get(j).getSurname() + "_" + llist.get(j).getName()
							+ "_" + llist.get(j).getFathername() + ".zip");
					file.delete();

					if (!newPerson.getSurname().equals(""))
						llist.get(j).setSurname(newPerson.getSurname());
					if (!newPerson.getName().equals(""))
						llist.get(j).setName(newPerson.getName());
					if (!newPerson.getFathername().equals(""))
						llist.get(j).setFathername(newPerson.getFathername());
					if (!newPerson.getPhone().equals(""))
						llist.get(j).setPhone(newPerson.getPhone());
					if (!newPerson.getEMail().equals(""))
						llist.get(j).setEMail(newPerson.getEMail());
					if (!newPerson.getNameJob().equals(""))
						llist.get(j).setNameJob(newPerson.getNameJob());
					if (!newPerson.getExperienceJob().equals(""))
						llist.get(j).setExperienceJob(newPerson.getExperienceJob());

					DOM.savePerson(llist.get(j));
					try {
						out.writeUTF(SerializeMaker.serializeToXML(llist));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				}
			}

		}
	}

	private void search(Request request) {

		Person searchPerson = request.getOldPerson();
		LinkedList<Person> myList = MainServer.getCatalog();
		LinkedList<Person> finishList = new LinkedList<Person>();
		synchronized (myList) {
			for (int k = 0; k < myList.size(); k++) {
				if (!searchPerson.getSurname().equals("")) {
					if (!myList.get(k).getSurname().equals(searchPerson.getSurname()))
						continue;
				}
				if (!searchPerson.getName().equals("")) {
					if (!myList.get(k).getName().equals(searchPerson.getName()))
						continue;
				}
				if (!searchPerson.getFathername().equals("")) {
					if (!myList.get(k).getFathername().equals(searchPerson.getFathername()))
						continue;
				}
				if (!searchPerson.getPhone().equals("")) {
					if (!myList.get(k).getPhone().equals(searchPerson.getPhone()))
						continue;
				}
				if (!searchPerson.getEMail().equals("")) {
					if (!myList.get(k).getEMail().equals(searchPerson.getEMail()))
						continue;
				}
				if (!searchPerson.getNameJob().equals("")) {
					if (!myList.get(k).getNameJob().equals(searchPerson.getNameJob()))
						continue;
				}
				if (!searchPerson.getExperienceJob().equals("")) {
					if (!myList.get(k).getExperienceJob().equals(searchPerson.getExperienceJob()))
						continue;
				}
				finishList.add(myList.get(k));
			}
			try {
				out.writeUTF(SerializeMaker.serializeToXML(finishList));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void userValidate(Request request) {

		User client = request.getUser();
		LinkedList<User> catalogizer = MainServer.getUsersCatalog();
		Boolean flag = false;
		int index = 0;
		synchronized (catalogizer) {
			for (int l = 0; l < catalogizer.size(); l++) {
				if (catalogizer.get(l).getLogin().equals(client.getLogin())
						&& catalogizer.get(l).getPassword().equals(client.getPassword())) {
					flag = true;
					index = l;
					break;
				}
			}
			if (flag) {
				// Exception!!
				client.setLogin(catalogizer.get(index).getLogin());
				client.setPassword(catalogizer.get(index).getPassword());
				client.setRights(catalogizer.get(index).getRights());
				client.setParser(catalogizer.get(index).getParser());
			} else
				client.setLogin("ERROR");
		}

		try {
			out.writeUTF(SerializeMaker.serializeToXML(client));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getUserTable() {

		synchronized (MainServer.getUsersCatalog()) {
			try {
				out.writeUTF(SerializeMaker.serializeToXML(MainServer.getUsersCatalog()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void addUser(Request request) throws TransformerException, ParserConfigurationException {
		User user = request.getUser();
		LinkedList<User> userList = MainServer.getUsersCatalog();
		Boolean addFlag = false;
		synchronized (userList) {
			try {
				for (int m = 0; m < userList.size(); m++) {
					if (user.getLogin().equals(userList.get(m).getLogin())) {
						addFlag = true;
						break;
					}
				}
				if (addFlag) {
					user.setLogin("ERROR");
				} else {
					userList.add(user);
					user.setParser(User.ParserName.SAX);
					user.setRights(User.Rights.USER);
					DOM.saveUser(user);
					
				}

				out.writeUTF(SerializeMaker.serializeToXML(user));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void changeRights(Request request) {
		User changeUser = request.getUser();
		LinkedList<User> userLis = MainServer.getUsersCatalog();
		synchronized (userLis) {
			for (int z = 0; z < userLis.size(); z++) {
				if (changeUser.getLogin().equals(userLis.get(z).getLogin())
						&& changeUser.getPassword().equals(userLis.get(z).getPassword())) {
					userLis.get(z).setRights(changeUser.getRights());

				}
			}
		}
		File file = new File("Data\\Users\\" + changeUser.getLogin() + ".zip");
		file.delete();

		DOM.saveUser(changeUser);
		try {
			out.writeUTF(SerializeMaker.serializeToXML(userLis));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void changeParser(Request request) {

		User changeParserUser = request.getUser();
		LinkedList<User> userL = MainServer.getUsersCatalog();
		synchronized (userL) {
			for (int z = 0; z < userL.size(); z++) {
				if (changeParserUser.getLogin().equals(userL.get(z).getLogin())
						&& changeParserUser.getPassword().equals(userL.get(z).getPassword())) {
					userL.get(z).setParser(changeParserUser.getParser());

				}
			}
		}
		File fille = new File("Data\\Users\\" + changeParserUser.getLogin() + ".zip");
		fille.delete();

		DOM.saveUser(changeParserUser);
		try {
			out.writeUTF(SerializeMaker.serializeToXML(userL));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
