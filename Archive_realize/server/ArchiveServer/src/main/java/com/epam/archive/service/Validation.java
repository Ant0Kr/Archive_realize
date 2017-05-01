package com.epam.archive.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;



import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class Validation {

	@SuppressWarnings("resource")
	public static Boolean checkValid(String chemeWay, String fileWay,String partWay) throws SAXException, IOException {

		byte[] buffer = new byte[1024];
		ZipInputStream zin = new ZipInputStream(new FileInputStream(fileWay));
		ZipEntry ze = zin.getNextEntry();
		String newFileName = new String();
		
		while (ze != null) {
			newFileName = ze.getName();
			File nextFile = new File(partWay + newFileName);
			FileOutputStream out = new FileOutputStream(nextFile);
			int length;
			while ((length = zin.read(buffer)) >= 0) {
				out.write(buffer, 0, length);
			}
			ze = zin.getNextEntry();
			out.close();
		}

		zin.closeEntry();
		zin.close();

			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(chemeWay));
			Validator validator = schema.newValidator();
			File file = new File(partWay + newFileName);
			StreamSource source = new StreamSource(file);
		try {
			validator.validate(source);
		} catch (IOException  e) {
			System.out.println("Exception: " + e.getMessage());


			file.delete();
			return false;
		}
		file.delete();
		return true;
	}
}
