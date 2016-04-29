package org.anooj.hadoop.client.ingestemail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import org.anooj.hadoop.client.ingestemail.parser.TikaParser;
import org.anooj.hadoop.client.ingestemail.pojo.Email;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.exception.TikaException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
public class SolrClient {
	private SolrServer client;
	 private Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
	@Test
	public void indexDocument() throws Exception {
		while (true){
			Scanner scanner = new Scanner(System.in);
			String filepath = scanner.nextLine();
			//String filepath = "C:\\Users\\anooj\\Downloads\\enron_3045_small_sample\\allen-p\\inbox\\5";
			System.out.println(filepath);
			// This is the (input) Data Transfer Object between your client and SOLR.
			
			// 1. Read and  Populates with (at least required) fields
			//creating file object and Email object
			File files = new File(filepath);//
			//added earlier enhancement 0.1 -- start
	/*		if (files.isDirectory())
			{
				File[] filesList = files.getCanonicalFile().listFiles();
				for(File name : filesList) {
				System.out.println(name.getAbsolutePath());
		
			File file = new File (name.getAbsolutePath());
			SolrInputDocument document = new SolrInputDocument();
			addDocumenttoSolr(document, file);
				}
			}else
			{
				SolrInputDocument document = new SolrInputDocument();
				addDocumenttoSolr(document, files);
			}*/
			//added earlier enhancement 0.1 -- end
			//added later enhancement 0.2 -- start
			 ArrayList<Email> emails= new ArrayList<Email>();
			
			 emails = TikaParser.readFileRecursively(files);
				for (int i = 0; i < emails.size(); i++) {
					System.out.println(emails.get(i).toString());
					SolrInputDocument document = new SolrInputDocument();
					addDocumenttoSolr(document, emails.get(i));
				}
				client.add(docs);
	
				client.commit();
			//added later enhancement 0.2 -- end
				queryData();
		}

	}

	public void addDocumenttoSolr(final SolrInputDocument document,  Email email)
			throws FileNotFoundException, IOException, SAXException, TikaException, SolrServerException {
		//added earlier enhancement 0.1 -- start
		/*		Email email= new Email();
		TikaParser.extractnPopulateEmail(file,email);*/
		//added earlier enhancement 0.1 -- end
		System.out.println(email.toString());
/*		document.setField(LocalConstants.documentFrom, email.getFrom());
		document.setField(LocalConstants.documentTo, email.getTo());
		document.setField(LocalConstants.documentBody, email.getBody());
*/
		document.setField("from", email.getFrom());
		document.setField("to", email.getTo());
		document.setField("body", email.getBody());
	//document.setField("title", "972-2-5A619-12A-X");
		// 2. Adds the document
		docs.add(document);
		//client.add(document);
		
		

		// 3. Makes index changes visible
		//client.commit();
	}

	public void queryData() throws SolrServerException {
		// 4. Builds a new query object with a "select all" query. 
		final SolrQuery query = new SolrQuery("*:*");
		query.setRows(2000);
		// 5. Executes the query
		final QueryResponse response = client.query(query);

		/*		assertEquals(1, response.getResults().getNumFound());*/

		// 6. Gets the (output) Data Transfer Object.
		
		
	
		if (response.getResults().iterator().hasNext())
		{
			final SolrDocument output = response.getResults().iterator().next();
			final String from = (String) output.getFieldValue("from");
			final String to = (String) output.getFieldValue("to");
			final String body = (String) output.getFieldValue("body");
			// 7.1 In case we are running as a Java application print out the query results.
			System.out.println("It works! I found the following book: ");
			System.out.println("--------------------------------------");
			System.out.println("ID: " + from);
			System.out.println("Title: " + to);
			System.out.println("Author: " + body);
		}
		
		SolrDocumentList list = response.getResults();
		System.out.println("list size is: " + list.size());



		/*		// 7. Otherwise asserts the query results using standard JUnit procedures.
		assertEquals("1", id);
		assertEquals("Apache SOLR Essentials", title);
		assertEquals("Andrea Gazzarini", author);
		assertEquals("972-2-5A619-12A-X", isbn);*/
	}

	/**
	 * Entry point for running the example as a Java application.
	 * 
	 * @param args the command line arguments. Not used here.
	 * @throws Exception never, otherwise something went wrong.
	 */
	public static void main(String[] args) throws Exception {
		final SolrClient test = new SolrClient();

		test.setUp();
		test.indexDocument();
		test.queryData();
		test.tearDown();
	}

	/**
	 * Setup things for this test case.
	 */
	@Before
	public void setUp() {
		//client = new HttpSolrServer("http://127.0.0.1:8983/solr/biblo");
		client = new HttpSolrServer("http://192.168.1.34:8983/solr/collection3_shard1_replica1");
	}

	/**
	 * Shutdown SOLR client.
	 */
	@After 
	public void tearDown() {
		client.shutdown();
	}

}
