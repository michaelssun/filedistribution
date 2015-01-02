package com.tealium.net.ftp.benchmark;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

/**
 * Unit test for simple App.
 */
public class Ftp4jBenchMarkTest {

	public static final String DEV_ACCOUNT_PASSWORD = "sz4NbTDnw4fs8Fx9PP";
	public static final String DEV_ACCOUNT_USER = "dev_tealium";
	public static final String CDN_FTP_SERVER_URL = "upload.tealium.sf.cdngp.net";

	final long foldername = new Date().getTime();
	FTPClient client;

	@Before
	public void before() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		client = new FTPClient();
		client.connect(CDN_FTP_SERVER_URL);
		client.login(DEV_ACCOUNT_USER, DEV_ACCOUNT_PASSWORD);
		client.setAutoNoopTimeout(30000);
		
//		assertTrue(	client.isCompressionSupported());
//		client.setCompressionEnabled(true);
	}

	public void testUpload() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException,
			FTPDataTransferException, FTPAbortedException, FTPListParseException {
		assertNotNull(client);
		assertTrue(client.isConnected());
		client.upload(new File("/Users/michaelsun/Documents/dev/design/PROD-profile-hyatt/profiles/copy-digital-media.profile.201409081616"),
				new TestDataTransferListener());

		FTPFile[] names = client.list();
		for (FTPFile ftpFile : names) {
			System.out.println(ftpFile);
		}

		// client.rename("copy-digital-media.profile.201409081616","copy-digital-media.profile.201409081616.bak");
	}

	@Test
	public void testUploadFiles() throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException,
			FTPDataTransferException, FTPAbortedException, FTPListParseException {
		assertNotNull(client);
		assertTrue(client.isConnected());


		String path = "distro" + foldername;
		client.createDirectory(path);
		client.changeDirectory(path);

		File file = new File("/Users/michaelsun/Documents/dev/design/uploadsamplefiles/distro");

		File[] listFiles = file.listFiles();
		System.out.println("total number of files: " + listFiles.length);
		System.out.println("start - " + new Date());
		for (File file2 : listFiles) {
			client.upload(file2);
		}
		System.out.println("end - " + new Date());

	}

	@After
	public void after() throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		if (client.isConnected()) {
			client.disconnect(false);
		}
	}

}
