package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import java.io.IOException;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.FilenameUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import java.io.FileOutputStream;


import com.itextpdf.text.DocumentException;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;

@RestController
@SpringBootApplication
public class generationtest {

	public static void main(String[] args) throws InvalidKeyException, DocumentException, URISyntaxException, StorageException, IOException  {
		generationtest ob= new generationtest();
		String s=ob.run ();
		System.out.println(s);
		SpringApplication.run(generationtest.class, args);
	}
@GetMapping("/")
	
	public String method2()
	{
		
		return "welcome to signature generation";
	}
	
	@GetMapping("/testing")
	
	public String run() throws DocumentException, URISyntaxException, StorageException, InvalidKeyException, IOException
	{
		File file=null;
			final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storagetestpoccsl;AccountKey=SAe7L4MStD0F/FNFgXgsTDzda4aqptsq4lOO1Bl4QpXiTbbiMi78H01CKpVTFPXUsRjNjLJbsAl++AStgB4PLA==;EndpointSuffix=core.windows.net";
		     file = File.createTempFile("hello",".txt");
			CloudStorageAccount storageAccount;
			CloudBlobClient blobClient = null;
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			CloudBlobContainer container=blobClient.getContainerReference("test"); 
			CloudBlockBlob blob = container.getBlockBlobReference("hello.txt");
			PrintWriter pw=new PrintWriter(file);
			pw.write("dscsdvckhsbcjh sbdjcsbn.,kdc");
			pw.close();
			blob.uploadFromFile(file.getAbsolutePath());
			
				return "successfull";
		   }
}
