package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
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
import java.io.FileReader;

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
	
	public static String run()
	{
		final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=pocdemofilecontainer;AccountKey=AUrYg7IiXN6ujRJ6oY4lUVygLPYYhrgcUqv2Ee/ESWW/946H6KP7LIDF0wIG1olh1ii324gfzGZz+ASt84o3YQ==;EndpointSuffix=core.windows.net";
		File sourceFile = null, downloadedFile = null;
		System.out.println("Azure Blob storage quick start sample");

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;

		try {    
			// Parse the connection string and create a blob client to interact with Blob storage
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference("quickstartcontainer");
			CloudBlobContainer container2=blobClient.getContainerReference("fileaccess");
			// Create the container if it does not exist with public access.
			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		 
			 
			//Creating a sample file
			sourceFile = File.createTempFile("final2", ".pdf");
			System.out.println("Creating a sample file at: " + sourceFile.toString());
			Document document = new Document(PageSize.A4.rotate());
			   // String input ="src//main//java//output//output.pdf-1.png"; // .gif and .jpg are ok too!
			   // String output = "src//main//java//output//final.pdf";
			    try {
			    	CloudBlockBlob blob2 = container2.getBlockBlobReference("output.pdf-1.png");
			    	File sourceFile2=File.createTempFile("final", ".png");
			    	FileOutputStream fos2= new FileOutputStream(sourceFile2);
			    	blob2.download(fos2);
			    	
			    	//URL url=new URL("https://pocdemofileaccess.blob.core.windows.net/fileaccess/final.pdf");
			      FileOutputStream fos = new FileOutputStream(sourceFile);
			      PdfWriter writer = PdfWriter.getInstance(document, fos);
			      writer.open();
			      document.open();
			      Image image = Image.getInstance(sourceFile2.getAbsolutePath());
			      image.scaleToFit(PageSize.A5.getWidth(), PageSize.A5.getHeight());
			      document.add(image);
			      document.close();
			      writer.close();
			    }
			    catch (Exception e) {
			      e.printStackTrace();
			    }

			//Writer output1 = new BufferedWriter(new FileWriter(sourceFile));
			//output1.write("Hello Azure!");
			//output1.close();

			//Getting a blob reference
			CloudBlockBlob blob = container.getBlockBlobReference(sourceFile.getName());

			//Creating blob and uploading file to it
			System.out.println("Uploading the sample file ");
			blob.uploadFromFile(sourceFile.getAbsolutePath());

			//Listing contents of container
			for (ListBlobItem blobItem : container.listBlobs()) {
			System.out.println("URI of blob is: " + blobItem.getUri());
		}

		// Download blob. In most cases, you would have to retrieve the reference
		// to cloudBlockBlob here. However, we created that reference earlier, and 
		// haven't changed the blob we're interested in, so we can reuse it. 
		// Here we are creating a new file to download to. Alternatively you can also pass in the path as a string into downloadToFile method: blob.downloadToFile("/path/to/new/file").
		downloadedFile = new File(sourceFile.getParentFile(), "downloadedFile.txt");
		blob.downloadToFile(downloadedFile.getAbsolutePath());
		} 
		catch (StorageException ex)
		{
			System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}
		catch (Exception ex) 
		{
			System.out.println(ex.getMessage());
		}
		finally 
		{
			System.out.println("The program has completed successfully.");
			}
		return "successfull";
	}
	
	
	
}
