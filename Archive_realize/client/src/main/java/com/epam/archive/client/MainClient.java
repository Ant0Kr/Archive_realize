package com.epam.archive.client;

import com.epam.archive.client.controllers.ClientMainController;
import com.epam.archive.models.User;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class MainClient{
	
	private String address;
	private int PORT;
	private DataInputStream in;
	private DataOutputStream out;
	private User infoUser;
	private Socket socket;
	private static final Logger log = Logger.getLogger(MainClient.class);
	
	public MainClient(String login, String pass, User.Rights rights, User.ParserName parser){
		address = "127.0.0.1";
		PORT = 5555;
		infoUser = new User(login,pass,rights,parser);
	}
	
	public Boolean ClientConnection() throws IOException{
		InetAddress IPadress = InetAddress.getByName(address);
		try{
			socket = new Socket(IPadress,PORT);
		}catch(Exception e){
			System.out.println("Server disconnected!");
			log.info("Server disconnected.");
			return false;
		}
		in = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream(socket.getOutputStream());
		log.info("System connected to server.");
		return true;
	}
	
	public DataInputStream getInputStream(){
		return in;
	}
	public DataOutputStream getOutputStream(){
		return out;
	}
	
	public User getUser(){
		return infoUser;
	}
	
	public void setUser(User user){
		this.infoUser = user;
	}
		
}