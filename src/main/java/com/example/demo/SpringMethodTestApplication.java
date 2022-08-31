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
	
	public String run()throws StorageException, URISyntaxException, DocumentException, GeneralSecurityException
	{
		 final String storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=pocdemofilecontainer;AccountKey=AUrYg7IiXN6ujRJ6oY4lUVygLPYYhrgcUqv2Ee/ESWW/946H6KP7LIDF0wIG1olh1ii324gfzGZz+ASt84o3YQ==;EndpointSuffix=core.windows.net";
	    final char[] PASSWORD = "Sathvik123#".toCharArray();
		File xsltFile = null;
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
			xsltFile = File.createTempFile("template", ".xsl");
			CloudBlockBlob blob2 = container2.getBlockBlobReference("template.xsl");
	    	FileOutputStream xsloutput= new FileOutputStream(xsltFile);
	    	blob2.download(xsloutput);
	    	File xmlFile=File.createTempFile("data", ".xml");
	    	CloudBlockBlob blob3 = container2.getBlockBlobReference("data1.xml");
	    	FileOutputStream xmloutput= new FileOutputStream(xmlFile);
	    	blob3.download(xmloutput);
           StreamSource xmlSource = new StreamSource(xmlFile);
           File outputFile=File.createTempFile("output", ".pdf");
    // create an instance of fop factory
    FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
    // a user agent is needed for transformation
    FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
    // Setup output
    OutputStream out3;
    out3 = new java.io.FileOutputStream( outputFile);

    
        // Construct fop with desired output format
        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out3);

        // Setup XSLT
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

        // Resulting SAX events (the generated FO) must be piped through to
        // FOP
        Result res = new SAXResult(fop.getDefaultHandler());

        // Start XSLT transformation and FOP processing
        // That's where the XML is first transformed to XSL-FO and then
        // PDF is created
        transformer.transform(xmlSource, res);
        out3.close();
        PDDocument document = PDDocument.loadNonSeq(outputFile, null);
		@SuppressWarnings("unchecked")
		List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
       File imagepdf=File.createTempFile("imagepdf",".pdf");
        //int page = 0;
        File imageFile=File.createTempFile("image", ".png");
        OutputStream fos=new FileOutputStream(imagepdf);
    	Document document1 = new Document(PageSize.A4.rotate());
		 PdfWriter writer = PdfWriter.getInstance(document1, fos);
					      writer.open();
					      document1.open();

        for(PDPage pdPage : pdPages){
           // ++page;
            BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
            ImageIOUtil.writeImage(bim, imageFile.getAbsolutePath(), 300);
        
        
	
	
					 Image image = Image.getInstance(imageFile.getAbsolutePath());
					      image.scaleToFit(PageSize.A5.getWidth(), PageSize.A5.getHeight());
					      document1.add(image);
					      
        }
        document1.close();
	      writer.close();
       
	File finalFile=File.createTempFile("final", ".pdf");
	File keyFile=File.createTempFile("keystore",".pfx");
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
    app.sign(imagepdf.getAbsolutePath(), finalFile.getAbsolutePath() , chain, pk, DigestAlgorithms.SHA256, provider.getName(),
           PdfSigner.CryptoStandard.CMS, "Approved", "India");
		
    CloudBlockBlob blob = container.getBlockBlobReference("finalFile.pdf");
    
	//Creating blob and uploading file to it
	System.out.println("Uploading the sample file ");
	blob.uploadFromFile(finalFile.getAbsolutePath());
	
	keyoutput.close();
	in.close();
	xmloutput.close();
	xsloutput.close();
	keyFile.deleteOnExit();
	xsltFile.deleteOnExit();
	xmlFile.deleteOnExit();
	finalFile.deleteOnExit();
	outputFile.deleteOnExit();
	imageFile.deleteOnExit();
	imagepdf.deleteOnExit();

		}
	catch (FOPException | IOException | TransformerException e) {
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
            .setPageNumber(4);
    signer.setFieldName("sig");

    IExternalSignature pks = new PrivateKeySignature(pk, digestAlgorithm, provider);
    IExternalDigest digest = new BouncyCastleDigest();

    // Sign the document using the detached mode, CMS or CAdES equivalent.
   signer.signDetached(digest, pks, chain, null, null, null, 0, signatureType);
}
	
	
	
}
