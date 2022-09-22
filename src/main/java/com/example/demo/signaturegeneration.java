package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;

import org.apache.commons.io.FilenameUtils;
import java.io.FileOutputStream;


import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;


import java.util.ArrayList;
import java.util.List;




import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;

import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;
import com.itextpdf.text.DocumentException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


import java.security.GeneralSecurityException;
import java.security.KeyStore;

import java.security.PrivateKey;
import java.security.Security;

import java.security.cert.Certificate;
@RestController
@SpringBootApplication
public class SignatureGeneration {

	public static void main(String[] args) throws StorageException, URISyntaxException, DocumentException, GeneralSecurityException   {
		SignatureGeneration ob= new SignatureGeneration();
		String s=ob.run();
		System.out.println(s);
		SpringApplication.run(SignatureGeneration.class, args);
	}

	@GetMapping("/")
	
	public String method2()
	{
		
		return "welcome to signature generation";
	}
	
	@GetMapping("/testing")
	
	public String run()throws StorageException, URISyntaxException, DocumentException, GeneralSecurityException
	{
		final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=pocfiledemoaccess;AccountKey=GY0kWJV+ik5mNByEavFYamMDMu5LecAHNK0VmiD6VPQka7s/4OrrATcBol4jOIxq33ID3yChL0W8+AStDb8VCA==;EndpointSuffix=core.windows.net";
	    final char[] PASSWORD = "Sathvik123#".toCharArray();
		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;

		try {    
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			container = blobClient.getContainerReference("finalpdffolder");
			CloudBlobContainer container2=blobClient.getContainerReference("fileaccess");
			CloudBlobContainer imagecontainer=blobClient.getContainerReference("buffercontainerimages");
			CloudBlobContainer buffercontainer=blobClient.getContainerReference("buffercontainer");
			CloudBlobContainer xmlcontainer=blobClient.getContainerReference("xmlcontainer");
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(), new OperationContext());		 
			List<String> list=new ArrayList<String>();
			for (ListBlobItem blobItem : imagecontainer.listBlobs()) {
				URL url=blobItem.getUri().toURL();
				String s=FilenameUtils.getName(url.getPath());
    		    String result = URLDecoder.decode(s,"utf-8");
    			list.add(result);
    				}
    			for(int i = 0; i < list.size(); i++)
    			{
                   File finalFile=File.createTempFile("final", ".pdf");
	               File outputFile=File.createTempFile("output", ".pdf");
	               File keyFile=File.createTempFile("keystore",".pfx");
	               CloudBlockBlob blob2 = imagecontainer.getBlockBlobReference(list.get(i));
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
	               SignatureGeneration app = new SignatureGeneration();
	               app.sign(outputFile.getAbsolutePath(), finalFile.getAbsolutePath() , chain, pk, DigestAlgorithms.SHA256, provider.getName(),
	               PdfSigner.CryptoStandard.CMS, "Approved", "India");
	               String fileNameWithOutExt = FilenameUtils.removeExtension(list.get(i));
	               CloudBlockBlob blob = container.getBlockBlobReference(fileNameWithOutExt+".pdf");
	               blob.uploadFromFile(finalFile.getAbsolutePath());
	               CloudBlockBlob xmlblob = xmlcontainer.getBlockBlobReference(fileNameWithOutExt+".xml");
	               CloudBlockBlob bufferblob = buffercontainer.getBlockBlobReference(list.get(i));
	               keyoutput.close();
	               xsloutput.close();
	               in.close();
	               finalFile.deleteOnExit();
	               outputFile.deleteOnExit();
	               keyFile.deleteOnExit();
	               blob2.deleteIfExists();
	               xmlblob.deleteIfExists();
	               bufferblob.deleteIfExists();
    			}
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
    Rectangle rect = new Rectangle(70, 105, 400, 400);
    PdfSignatureAppearance appearance = signer.getSignatureAppearance();
    appearance.setReason(reason).setLocation(location).setReuseAppearance(false).setPageRect(rect).setPageNumber(5);
    signer.setFieldName("sig");
    IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
    IExternalDigest digest = new BouncyCastleDigest();
    signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);
	}
	}