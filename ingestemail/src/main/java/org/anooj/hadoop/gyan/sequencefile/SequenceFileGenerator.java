package org.anooj.hadoop.gyan.sequencefile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.DefaultCodec;

public class SequenceFileGenerator {

	public SequenceFileGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws Exception {
		try {
		while (true){
			System.out.println("Please input in the follwoing format : <input file location> <space> <Output file lacation>");
			Scanner scanner = new Scanner(System.in);
			String filepaths = scanner.nextLine();
			StringTokenizer filepathST  = new StringTokenizer(filepaths);
			System.out.println(filepaths);

			//creating file object
			File inputfile;
			String outputfileLocation;
		
				inputfile = new File(filepathST.nextToken());
				outputfileLocation = filepathST.nextToken();
			

			// writeSequenceSingleFile( inputfile, outputfileLocation);
			writeSequenceMultiFile( inputfile, outputfileLocation);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Please input in the follwoing format : <input file location> <space> <Output file lacation>");
		}

			
		}



/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String uri = "";
		File infile = null;
		if (args.length == 0 || args[0] == null) {
			infile = new File("C:\\Users\\anooj\\Downloads\\enron_3045_small_sample\\allen-p\\inbox\\1");
		} else {
			infile = new File(args[0]);
		}

		if (args.length == 0 || args[1] == null) {
			uri = "C:\\Users\\anooj\\Downloads\\enron_3045_small_sample\\output.txt";
		} else {
			uri = args[1];
		}
		// writeSequenceSingleFile(uri, infile);
		writeSequenceMultiFile(uri, infile);

	}
*/
	public static void writeSequenceSingleFile( File inputFileName, String outputFileLocation) {
		System.out.println("Step one");
		Configuration conf = new Configuration();
		// conf.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem");
		FileSystem fs = null;

		Path path = new Path(outputFileLocation);
		Text key = new Text();
		Text value = new Text();

		SequenceFile.Writer writer = null;
		try {
			fs = FileSystem.get(conf);
			System.out.println("Step two");
			writer = SequenceFile.createWriter(conf, Writer.file(path), Writer.keyClass(key.getClass()),
					Writer.valueClass(value.getClass()),
					Writer.compression(SequenceFile.CompressionType.NONE, new DefaultCodec()));

			System.out.println("Step three");
			InputStream inputStream = new FileInputStream(inputFileName);
			byte[] inputStreamByte = new byte[(int) inputFileName.length()];
			key.set(inputFileName.getAbsolutePath());
			value.set(inputStreamByte);
			System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key, value);
			System.out.println("Step four- key" + key);
			writer.append(key, value);
			System.out.println("Step four- value" + value.getLength());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(writer);
		}
	}

	public static void writeSequenceMultiFile(File inputFileName, String outputFileLocation) {
		System.out.println("Step one");
		Configuration conf = new Configuration();
		// conf.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem");
		FileSystem fs = null;

		Path path = new Path(outputFileLocation);
		Text key = new Text();
		Text value = new Text();

		SequenceFile.Writer writer = null;
		try {
			fs = FileSystem.get(conf);
			System.out.println("Step two");
			writer = SequenceFile.createWriter(conf, Writer.file(path), Writer.keyClass(key.getClass()),
					Writer.valueClass(value.getClass()),
					Writer.compression(SequenceFile.CompressionType.BLOCK, new DefaultCodec()));

			System.out.println("Step three");

			readnAppend(inputFileName, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(writer);
		}
	}

	public static void readnAppend(File inputFileName, Writer writer) throws IOException {

		Text key = new Text();
		Text value = new Text();
		InputStream inputStream = null;
		// File rootDir = new File(path);
		// File[] list = rootDir.listFiles();

		/*
		 * if (list == null) return;
		 */
		if (inputFileName.isDirectory()) {
			for (File f : inputFileName.listFiles()) {

				readnAppend(f, writer);
				System.out.println("Dir:" + f.getAbsoluteFile());
			}
		} else {
			
			inputStream = new FileInputStream(inputFileName);
			byte[] inputStreamByte = new byte[(int) inputFileName.length()];
			key.set(inputFileName.getAbsolutePath());
			value.set(inputStreamByte);
			writer.append(key, value);
			System.out.println("File:" + inputFileName.getAbsoluteFile());
		}
		if (inputStream != null)
		{
			inputStream.close();
		}

	}

}
