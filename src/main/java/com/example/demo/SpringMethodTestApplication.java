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

	public static void main(String[] args)  {
		//SpringMethodTestApplication ob1=new SpringMethodTestApplication();
		//String message2=ob1.run();
		//System.out.println(message2);
		SpringApplication.run(SpringMethodTestApplication.class, args);
	}

	@GetMapping("/")
	
	public String method2()
	{
		System.out.println("done");
		return "hello hi bye1";
	}
	
	@GetMapping("/testing")
	
	public String run()throws StorageException, URISyntaxException, DocumentException, GeneralSecurityException
	{
		 final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=pocdemofilecontainer;AccountKey=AUrYg7IiXN6ujRJ6oY4lUVygLPYYhrgcUqv2Ee/ESWW/946H6KP7LIDF0wIG1olh1ii324gfzGZz+ASt84o3YQ==;EndpointSuffix=core.windows.net";
	    final char[] PASSWORD = "Sathvik123#".toCharArray();
		
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
			
       
	File finalFile=File.createTempFile("final", ".pdf");
	File outputFile=File.createTempFile("output", ".pdf");
	File keyFile=File.createTempFile("keystore",".pfx");
	CloudBlockBlob blob2 = container2.getBlockBlobReference("output.pdf");
	FileOutputStream xsloutput= new FileOutputStream(outputFile);
	blob2.download(xsloutput);
	CloudBlockBlob blob4 = container2.getBlockBlobReference("pfxcertificate.pfx");
	FileOutputStream keyoutput= new FileOutputStream(keyFile);
	blob4.download(keyoutput);
	InputStream in=new FileInputStream(keyFile);

    BouncyCastleProvider provider = new BouncyCastleProvider();
    Security.addProvider(provider);
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
    ks.load(in, PASSWORD);
    String alias = ks.aliases().nextElement();
    PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
    Certificate[] chain = ks.getCertificateChain(alias);
   
    SpringMethodTestApplication app = new SpringMethodTestApplication();
    app.sign(outputFile.getAbsolutePath(), finalFile.getAbsolutePath() , chain, pk, DigestAlgorithms.SHA256, provider.getName(),
           PdfSigner.CryptoStandard.CMS, "Approved", "India");
		
    CloudBlockBlob blob = container.getBlockBlobReference("finalFile.pdf");
    
	//Creating blob and uploading file to it
	System.out.println("Uploading the sample file ");
	blob.uploadFromFile(finalFile.getAbsolutePath());
	
	keyoutput.close();
	in.close();
	
	

		}
	catch (IOException e) {
       e.printStackTrace();
	}
		return "successfull";
   }

public void sign(String src, String dest, Certificate[] chain, PrivateKey pk, String digestAlgorithm,
        String provider, PdfSigner.CryptoStandard signatureType, String reason, String location)
        throws GeneralSecurityException, IOException {

    PdfReader reader = new PdfReader(src);
    PdfSigner signer = new PdfSigner(reader, new FileOutputStream(dest),false);

    // Create the signature appearance
    Rectangle rect = new Rectangle(400, 400, 400, 400);
   PdfSignatureAppearance appearance = signer.getSignatureAppearance();
    appearance
           .setReason(reason)
            .setLocation(location)

           //Specify if the appearance before field is signed will be used
          // as a background for the signed field. The "false" value is the default value.
            .setReuseAppearance(false)
            .setPageRect(rect)
            .setPageNumber(2);
    signer.setFieldName("sig");

    IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
    IExternalDigest digest = new BouncyCastleDigest();

    // Sign the document using the detached mode, CMS or CAdES equivalent.
   signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);
}
	
	
	
}
