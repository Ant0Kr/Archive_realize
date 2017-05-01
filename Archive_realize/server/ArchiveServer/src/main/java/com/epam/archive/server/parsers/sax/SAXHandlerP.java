package com.epam.archive.server.parsers.sax;

import com.epam.archive.models.Person;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Antoha12018 on 29.04.2017.
 */
public class SAXHandlerP extends DefaultHandler {

    private boolean surnamefl = false;
    private boolean namefl = false;
    private boolean fathernamefl = false;
    private boolean phonefl = false;
    private boolean emailfl = false;
    private boolean nameJobfl = false;
    private boolean experienceJobfl = false;
    private Person person = new Person();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
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
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("models.Person")) {
        }
    }
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (surnamefl) {
            person.setSurname(new String(ch, start, length));
            surnamefl = false;
        } else if (namefl) {
            person.setName(new String(ch, start, length));
            namefl = false;
        } else if (fathernamefl) {
            person.setFathername(new String(ch, start, length));
            fathernamefl = false;
        } else if (phonefl) {
            person.setPhone(new String(ch, start, length));
            phonefl = false;
        } else if (emailfl) {
            person.setEMail(new String(ch, start, length));
            emailfl = false;
        } else if (nameJobfl) {
            person.setNameJob(new String(ch, start, length));
            nameJobfl = false;
        } else if (experienceJobfl) {
            person.setExperienceJob(new String(ch, start, length));
            experienceJobfl = false;
        }
    }


    public Person getPerson(){
        return person;
    }


}
