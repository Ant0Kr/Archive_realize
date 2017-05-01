package com.epam.archive.models;

public class User {
	
	public static enum ParserName {
		JDOM, DOM, SAX, STAX

	};

	public static enum Rights {
		ADMINISTRATOR, SENIORUSER, USER,
	};
	
	private String login;
	private String password;
	private ParserName parser;
	private Rights rights;

	public User(String login, String password, Rights rights, ParserName parser) {
		this.login = login;
		this.password = password;
		this.rights = rights;
		this.parser = parser;
	}

	public User(User user) {
		this.login = user.getLogin();
		this.password = user.getPassword();
		this.rights = user.getRights();
		this.parser = user.getParser();
	}

	public User() {
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public Rights getRights() {
		return rights;
	}

	public ParserName getParser() {
		return parser;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRights(Rights rights) {
		this.rights = rights;
	}

	public void setParser(ParserName parser) {
		this.parser = parser;
	}

	public void setStringParser(String parserName) {

		if(parserName.equals("DOM"))
			parser = ParserName.DOM;
		else if(parserName.equals("JDOM"))
			parser = ParserName.JDOM;
		else if(parserName.equals("STAX"))
			parser = ParserName.STAX;
		else if(parserName.equals("SAX"))
			parser = ParserName.SAX;
	}
	
	public void setStringRights(String rights) {

		if(rights.equals("ADMINISTRATOR"))
			this.rights = Rights.ADMINISTRATOR;
		else if(rights.equals("USER"))
			this.rights = Rights.USER;
		else if(rights.equals("SENIORUSER"))
			this.rights = Rights.SENIORUSER;

	}
}
