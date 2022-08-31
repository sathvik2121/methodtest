package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;


import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import java.io.FileOutputStream;
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;

import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;

import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


import java.security.GeneralSecurityException;
import java.security.KeyStore;

import java.security.PrivateKey;
import java.security.Security;

import java.security.cert.Certificate;
@RestController
@SpringBootApplication
public class SpringMethodTestApplication {

	public static void main(String[] args) {
		//SpringMethodTestApplication ob1=new SpringMethodTestApplication();
		//String message2=ob1.method1();
		//System.out.println(message2);
		SpringApplication.run(SpringMethodTestApplication.class, args);
	}

	@GetMapping("/")
	
	public String method2()
	{
		System.out.println("done");
		return "hello hi bye";
	}
	
	@GetMapping("/testing")
	
	 public String  run()
	//public static String demo()
	{
		 final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=pocdemofilecontainer;AccountKey=AUrYg7IiXN6ujRJ6oY4lUVygLPYYhrgcUqv2Ee/ESWW/946H6KP7LIDF0wIG1olh1ii324gfzGZz+ASt84o3YQ==;EndpointSuffix=core.windows.net";
		 String s = "";
		File sourceFile = null;
		System.out.println("Azure Blob storage quick start sample");

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;

		try {    
		
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference("quickstartcontainer");
			CloudBlobContainer container2=blobClient.getContainerReference("fileaccess");
		
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		 
			 
		
			sourceFile = File.createTempFile("temp", ".txt");
			System.out.println("Creating a sample file at: " + sourceFile.toString());
			
			  
			  
			    	CloudBlockBlob blob2 = container2.getBlockBlobReference("hi.txt");
			    	
			    	FileOutputStream fos2= new FileOutputStream(sourceFile);
			    	blob2.download(fos2);
			    	 BufferedReader br= new BufferedReader(new FileReader(sourceFile));
			         String z;
			         while ((z = br.readLine()) != null)
			        	 s=s+z;
                    br.close();
                    fos2.close();

		}
		 catch (Exception e) {
		      e.printStackTrace();
		    }
			    	
			    	
			 //System.out.println(s);
		return s;
			         
			         
			  
	
}
	
	
	
}
