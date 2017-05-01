package com.epam.archive.server.parsers;

import com.epam.archive.models.Person;
import com.epam.archive.models.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipInputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


public class StAX {
    //1.load person
    //2.load user
    public static Person loadPerson(ZipInputStream zin) {

        boolean surnamefl = false;
        boolean namefl = false;
        boolean fathernamefl = false;
        boolean phonefl = false;
        boolean emailfl = false;
        boolean nameJobfl = false;
        boolean experienceJobfl = false;
        Person person = new Person();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(zin, "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            XMLEventReader eventReader = factory.createXMLEventReader(br);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {

                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (qName.equalsIgnoreCase("models.Person")) {

                        } else if (qName.equalsIgnoreCase("surname")) {
                            surnamefl = true;
                        } else if (qName.equalsIgnoreCase("name")) {
                            namefl = true;
                        } else if (qName.equalsIgnoreCase("fathername")) {
                            fathernamefl = true;
                        } else if (qName.equalsIgnoreCase("phone")) {
                            phonefl = true;
                        } else if (qName.equalsIgnoreCase("email")) {
                            emailfl = true;
                        } else if (qName.equalsIgnoreCase("nameJob")) {
                            nameJobfl = true;
                        } else if (qName.equalsIgnoreCase("experienceJob")) {
                            experienceJobfl = true;
                        }
                        break;

                    // catches the element and writes it to variable
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if (surnamefl) {
                            person.setSurname(characters.getData());
                            surnamefl = false;
                        }
                        if (namefl) {
                            person.setName(characters.getData());
                            namefl = false;
                        }
                        if (fathernamefl) {
                            person.setFathername(characters.getData());
                            fathernamefl = false;
                        }
                        if (phonefl) {
                            person.setPhone(characters.getData());
                            phonefl = false;
                        }
                        if (emailfl) {
                            person.setEMail(characters.getData());
                            emailfl = false;
                        }
                        if (nameJobfl) {
                            person.setNameJob(characters.getData());
                            nameJobfl = false;
                        }
                        if (experienceJobfl) {
                            person.setExperienceJob(characters.getData());
                            experienceJobfl = false;
                        }
                        break;

                    // catches closing tag of an element
                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        if (endElement.getName().getLocalPart().equalsIgnoreCase("models.Person")) {
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return person;
    }

    public static User loadUser(ZipInputStream zin) {

        boolean loginfl = false;
        boolean passwordfl = false;
        boolean parserfl = false;
        boolean rightsfl = false;
        User user = new User();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(zin, "UTF-8"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            XMLEventReader eventReader = factory.createXMLEventReader(br);

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {

                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        if (qName.equalsIgnoreCase("models.User")) {

                        } else if (qName.equalsIgnoreCase("login")) {
                            loginfl = true;
                        } else if (qName.equalsIgnoreCase("password")) {
                            passwordfl = true;
                        } else if (qName.equalsIgnoreCase("parser")) {
                            parserfl = true;
                        } else if (qName.equalsIgnoreCase("rights"))
                            rightsfl = true;
                            break;


                        // catches the element and writes it to variable
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if (loginfl) {
                            user.setLogin(characters.getData());
                            loginfl = false;
                        }
                        if (passwordfl) {
                            user.setPassword(characters.getData());
                            passwordfl = false;
                        }
                        if (parserfl) {
                            user.setStringParser(characters.getData());
                            parserfl = false;
                        }
                        if (rightsfl) {
                            user.setStringRights(characters.getData());
                            rightsfl = false;
                        }
                        break;

                    // catches closing tag of an element
                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        if (endElement.getName().getLocalPart().equalsIgnoreCase("models.Person")) {
                        }
                        break;
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return user;
    }
}
