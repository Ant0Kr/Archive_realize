package com.epam.archive.server;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import com.epam.archive.models.Person;
import com.epam.archive.models.User;
import com.epam.archive.server.parsers.DOM;
import com.epam.archive.server.parsers.JDOM;
import com.epam.archive.server.parsers.StAX;
import com.epam.archive.server.parsers.sax.SAX;
import org.apache.log4j.Logger;
import org.jdom2.JDOMException;
import org.xml.sax.SAXException;



public class ParserHandler {

    private static boolean flag = false;
    private static boolean flag1 = false;
    private static final Logger log = Logger.getLogger(ParserHandler.class);

    public static Person loadPerson(String fileName) throws ParserConfigurationException, SAXException {

        Person person = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new String("Data\\Archive\\" + fileName + ".zip"));

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        ZipInputStream zin = new ZipInputStream(fis);
        ZipEntry entry;

        try {
            zin.closeEntry();
            while ((entry = zin.getNextEntry()) != null) {
                if (entry.getName().equals((new String(fileName + ".xml")))) {
                    File file = new File("Data\\parser.txt");
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line = br.readLine();
                    br.close();
                    fr.close();
                    if (line != null) {
                        if (line.equals("DOM")) {
                            if (!flag) {
                                log.info("DOM load Persons!");
                                System.out.println("DOM load Persons!");
                                flag = true;
                            }
                            person = DOM.loadPerson(zin);
                        } else if (line.equals("JDOM")) {
                            if (!flag) {
                                log.info("JDOM load Persons!");
                                System.out.println("JDOM load Persons!");
                                flag = true;
                            }
                            person = JDOM.loadPerson(zin);
                        } else if (line.equals("SAX")) {
                            if (!flag) {
                                log.info("SAX load Persons!");
                                System.out.println("SAX load Persons!");
                                flag = true;
                            }
                            person = SAX.loadPerson(zin);
                        } else if (line.equals("StAX")) {
                            if (!flag) {
                                log.info("StAX load Persons!");
                                System.out.println("StAX load Persons!");
                                flag = true;
                            }
                            person = StAX.loadPerson(zin);
                        }
                    }

                    break;
                }
            }
            zin.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        return person;
    }

    public static User loadUser(String fileName) throws ParserConfigurationException, SAXException, JDOMException {

        User user = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new String("Data\\Users\\" + fileName + ".zip"));

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        ZipInputStream zin = new ZipInputStream(fis);
        ZipEntry entry;
        try {
            zin.closeEntry();

            while ((entry = zin.getNextEntry()) != null) {
                if (entry.getName().equals((new String(fileName + ".xml")))) {

                    File file = new File("Data\\parser.txt");
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String line = br.readLine();
                    br.close();
                    fr.close();
                    if (line != null) {
                        if (line.equals("DOM")) {
                            if (!flag1) {
                                log.info("DOM load Users!");
                                System.out.println("DOM load Users!");
                                flag1 = true;
                            }
                            user = DOM.loadUser(zin);
                        } else if (line.equals("JDOM")) {
                            if (!flag1) {
                                log.info("JDOM load Users!");
                                System.out.println("JDOM load Users!");
                                flag1 = true;
                            }
                            user = JDOM.loadUser(zin);
                        } else if (line.equals("SAX")) {
                            if (!flag1) {
                                log.info("SAX load Users!");
                                System.out.println("SAX  Users load!");
                                flag1 = true;
                            }
                            user = SAX.loadUser(zin);
                        } else if (line.equals("StAX")) {
                            if (!flag1) {
                                log.info("StAX load Users!");
                                System.out.println("StAX load Users!");
                                flag1 = true;
                            }
                            user = StAX.loadUser(zin);
                        }
                    }
                    break;
                }
            }
            zin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }


}
