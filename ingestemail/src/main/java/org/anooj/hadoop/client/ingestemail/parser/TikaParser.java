package org.anooj.hadoop.client.ingestemail.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.anooj.hadoop.client.ingestemail.config.LocalConstants;
import org.anooj.hadoop.client.ingestemail.pojo.Email;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TikaParser {

	public static void main(String[] args) throws Exception {

		while (true){
			Scanner scanner = new Scanner(System.in);
			String filepath = scanner.nextLine();
			System.out.println(filepath);

			//creating file object
			File files = new File(filepath);//

			/*			//using tika facade class 
			Tika tika = new Tika();

			//detecting the file type using detect method
			String filetype = tika.detect(file);
			System.out.println(filetype);*/

				readFileRecursively(files);
			for (int i = 0; i < emails.size(); i++) {
				System.out.println(emails.get(i).toString());
			}
			
		}

	}
	private static ArrayList<Email> emails= new ArrayList<Email>();
	public static ArrayList<Email> readFileRecursively(File files)
			throws FileNotFoundException, IOException, SAXException, TikaException {

		
		if (files.isDirectory())
		{
			
			File[] filesList = files.getCanonicalFile().listFiles();
			for(File name : filesList) {
				System.out.println(name.getAbsolutePath());

				File file = new File (name.getAbsolutePath());
		
				readFileRecursively(file);

			}
		}else
		{
			Email email= new Email();
			extractnPopulateEmail(files,email);
			emails.add(email);
		}


		return emails;
	}

	public static void extractnPopulateEmail(File file, Email email)
			throws FileNotFoundException, IOException, SAXException, TikaException {
		Parser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);
		ParseContext context = new ParseContext();
		parser.parse(inputstream, handler, metadata, context);
		//System.out.println(handler.toString());

		//getting the list of all meta data elements 
		String[] metadataNames = metadata.names();

		for(String name : metadataNames) {
			//System.out.println(name + ": " + metadata.get(name));
			email.setFrom(metadata.get(LocalConstants.from));
			email.setTo(metadata.get(LocalConstants.to));

		}
		email.setBody(handler.toString());


	}
}