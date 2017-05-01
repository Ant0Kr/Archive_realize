package com.epam.archive.client.controllers.threads;

import com.epam.archive.client.controllers.ClientMainController;
import com.epam.archive.models.Request;
import com.epam.archive.models.User;
import com.epam.archive.service.SerializeMaker;

import java.io.IOException;
import java.util.LinkedList;


public class UserRepainterThread extends Thread {

	public UserRepainterThread() {
	}

	public void run() {
		while (true) {

			Request request = new Request("GETUSERTABLE", null, null, null);
			String response = new String();
			synchronized (ClientMainController.getClient()) {
				try {

					ClientMainController.getClient().getOutputStream().writeUTF(SerializeMaker.serializeToXML(request));
					response = ClientMainController.getClient().getInputStream().readUTF();
					LinkedList<User> list = SerializeMaker.deserializeFromXML(response);
					synchronized (ClientMainController.getUserList()) {
						ClientMainController.setUserList(list);
					}

					synchronized (ClientMainController.getUserList()) {
						synchronized (ClientMainController.getUserTable()) {
							ClientMainController.changeUserTable(ClientMainController.getUserList());
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