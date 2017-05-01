package com.epam.archive.service;

import com.epam.archive.client.controllers.ClientMainController;
import com.epam.archive.models.Person;
import com.itextpdf.text.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Antoha12018 on 30.04.2017.
 */
public class GenerateTXT {

    public static void GenerateTXT(String way) throws IOException {

        File file = new File(way);
        if (file.exists())
            file.delete();
        file.createNewFile();

        LinkedList<Person> item;
        synchronized (ClientMainController.getArchiveList()) {
            item = (LinkedList<Person>) ClientMainController.getArchiveList();
        }
        FileWriter fstream = new FileWriter(way);// конструктор с одним параметром - для перезаписи
        BufferedWriter out = new BufferedWriter(fstream); //  создаём буферезированный поток
        out.write("***Archive***\r\n"+"*************\r\n");
        String writeStr = new String();
        int size = item.size();
        for (int i = 0; i < size; i++) {

            writeStr = "";
            Person person = item.get(i);
            writeStr = writeStr+"\r\nSurname:"+person.getSurname()+"\r\n";
            writeStr = writeStr+"Name:"+person.getName()+"\r\n";
            writeStr = writeStr+"Fathername:"+person.getFathername()+"\r\n";
            writeStr = writeStr+"Phone:"+person.getPhone()+"\r\n";
            writeStr = writeStr+"EMail:"+person.getEMail()+"\r\n";
            writeStr = writeStr+"Name of job:"+person.getName()+"\r\n";
            writeStr = writeStr+"Job experience:"+person.getExperienceJob()+"\r\n";
            out.write(writeStr);

        }
        out.close();
        fstream.close();

    }
}
