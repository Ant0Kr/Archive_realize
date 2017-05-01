package com.epam.archive.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class SerializeMaker {

	public static <T> String serializeToXML(T object) {

		String serializedString = new String();
		try {
			XStream xStream = new XStream(new DomDriver());
			serializedString = xStream.toXML(object);

		} catch (XStreamException e) {
			e.printStackTrace();
			return null;
		}
		return serializedString;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserializeFromXML(String stringForDes) {
		T object;
		try {
			XStream xStream = new XStream(new DomDriver());
			object = (T) xStream.fromXML(stringForDes);
		} catch (XStreamException exp) {
			exp.printStackTrace();
			return null;
		}
		return object;
	}

	public static <T> boolean saveXML(T object, String fileName,String way) {
		XStream xStream = new XStream(new DomDriver());
		try {
			FileOutputStream fStream = new FileOutputStream(new String(way+fileName+".zip"));
			ZipOutputStream zipOutpStream = new ZipOutputStream(fStream);

			ZipEntry zipEntry = new ZipEntry(new String(fileName + ".xml"));
			try {
				zipOutpStream.putNextEntry(zipEntry);
				xStream.toXML(object, zipOutpStream);
				zipOutpStream.closeEntry();
				zipOutpStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	@SuppressWarnings("unchecked")
	public static <T> T load(String fileName,String way) {
		T object = null;
		try {
			
			XStream xs = new XStream(new StaxDriver());
			FileInputStream fis = new FileInputStream(new String(way + fileName + ".zip"));
			ZipInputStream zin = new ZipInputStream(fis);
			ZipEntry entry;
			try {
				zin.closeEntry();
				while ((entry = zin.getNextEntry()) != null) {
					if (entry.getName().equals((new String(fileName + ".xml")))) {
						object = (T) xs.fromXML(zin, object);
						
						break;
					}
				}
				zin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return object;
	}
}
