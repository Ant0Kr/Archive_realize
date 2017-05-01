package com.epam.archive.client.controllers.threads;

import com.epam.archive.client.controllers.ClientMainController;
import com.epam.archive.models.Person;
import com.epam.archive.models.Request;
import com.epam.archive.service.SerializeMaker;

import java.io.IOException;
import java.util.LinkedList;


public class ArchiveRepainterThread extends Thread {

	public ArchiveRepainterThread() {
	}

	public void run() {
		
		while (true) {

			Request request = new Request("SHOWALL", null, null, null);
			String response = new String();
			synchronized (ClientMainController.getClient()) {
				try {
					if (!ClientMainController.getSearchFlag()) {
						ClientMainController.getClient().getOutputStream()
								.writeUTF(SerializeMaker.serializeToXML(request));
						response = ClientMainController.getClient().getInputStream().readUTF();
						LinkedList<Person> list = SerializeMaker.deserializeFromXML(response);
						synchronized (ClientMainController.getArchiveList()) {
							ClientMainController.setArchiveList(list);
						}
					}
					synchronized (ClientMainController.getArchiveList()) {
						synchronized (ClientMainController.getArchiveTable()) {
							ClientMainController.changeArchiveTable(ClientMainController.getArchiveList());
						}

					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return;
			}

		}
	}
}
