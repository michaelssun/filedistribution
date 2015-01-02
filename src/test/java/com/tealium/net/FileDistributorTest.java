package com.tealium.net;

import static org.junit.Assert.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.io.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tealium.net.FileDistributor;
import com.tealium.net.annotation.BindingThreadScope;
import com.tealium.net.annotation.FTPFileDistributorAkamai;
import com.tealium.net.annotation.FTPFileDistributorCDNetworks;
import com.tealium.net.annotation.S3FileDistributorPublishFiles;
import com.tealium.net.annotation.ThreadScope;
import com.tealium.net.aws.s3.S3FileDistributor;
import com.tealium.net.ftp.FTPFileDistributor;
import com.tealium.net.provider.FileDistributorProvider;
import com.tealium.scope.ThreadScopeImpl;

public class FileDistributorTest {

	 private static final String S3_END_POINT = "https://s3.amazonaws.com/dev-michaelsun";
	 private static final String S3_END_POINT1 = "https://dev-michaelsun.s3.amazonaws.com";
	private static final String S3_KEY_ID = "AKIAJEXKCSWWKDSP2BTA"; 
	private static final String S3_KEY = "9ZyyD1Hu58iBycbbjRv+DwOjRwG8rw+kpoyqUcCo";
	private FileDistributor s3FileDistributor;
	private FileDistributor cdnetworksFTPFileDistributor;
	private FileDistributor limelightFTPFileDistributor;
	private FileDistributor edgecacheFTPFileDistributor;
	private FileDistributor akamaiFTPFileDistributor;

	@Before
	public void setUp() throws Exception {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() { 
				ThreadScopeImpl scopeDef = new ThreadScopeImpl();
				bindScope(ThreadScope.class, scopeDef);
				bind(ThreadScopeImpl.class).annotatedWith(BindingThreadScope.class).toInstance(scopeDef);
				
				
				try {
					bind(String.class).annotatedWith(Names.named(S3FileDistributor.ACCESS_KEY_ID)).toInstance(S3_KEY_ID);
					//bind(String.class).annotatedWith(Names.named(S3FileDistributor.ACCESS_KEY_ID)).toInstance(new String(S3_KEY.getBytes("ISO-8859-1"), "UTF-8"));
					bind(String.class).annotatedWith(Names.named(S3FileDistributor.ACCESS_KEY)).toInstance(new String(S3_KEY.getBytes("ISO-8859-1"), "UTF-8"));
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bind(String.class).annotatedWith(Names.named(S3FileDistributor.END_POINT)).toInstance(S3_END_POINT1);

				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.ALIVE_AUTO_PING_AKAMAI)).toInstance("30000");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_HOST_AKAMAI)).toInstance("upload.tealium.sf.cdngp.net");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_USER_NAME_AKAMAI)).toInstance("dev_tealium");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_PASSWORD_AKAMAI)).toInstance("sz4NbTDnw4fs8Fx9PP");

				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.ALIVE_AUTO_PING_EDGECACHE)).toInstance("30000");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_HOST_EDGECACHE)).toInstance("upload.tealium.sf.cdngp.net");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_USER_NAME_EDGECACHE)).toInstance("dev_tealium");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_PASSWORD_EDGECACHE)).toInstance("sz4NbTDnw4fs8Fx9PP");

				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.ALIVE_AUTO_PING_LIMELIGHT)).toInstance("30000");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_HOST_LIMELIGHT)).toInstance("upload.tealium.sf.cdngp.net");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_USER_NAME_LIMELIGHT)).toInstance("dev_tealium");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_PASSWORD_LIMELIGHT)).toInstance("sz4NbTDnw4fs8Fx9PP");

				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.ALIVE_AUTO_PING_CDNETWORKS)).toInstance("30000");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_HOST_CDNETWORKS)).toInstance("upload.tealium.sf.cdngp.net");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_USER_NAME_CDNETWORKS)).toInstance("dev_tealium");
				bind(String.class).annotatedWith(Names.named(FTPFileDistributor.FTP_PASSWORD_CDNETWORKS)).toInstance("sz4NbTDnw4fs8Fx9PP");

				bind(FileDistributor.class).annotatedWith(S3FileDistributorPublishFiles.class)
						.toProvider(new FileDistributorProvider(FileDistributorProvider.ConnectionType.AWS_S3));
				bind(FileDistributor.class).annotatedWith(FTPFileDistributorCDNetworks.class)
				.toProvider(new FileDistributorProvider(FileDistributorProvider.ConnectionType.FTP_CDN_NETWORKS));
//				bind(FileDistributor.class).annotatedWith(FTPFileDistributorAkamai.class)
//				.toProvider(new FileDistributorProvider(FileDistributorProvider.ConnectionType.FTP_CDN_AKAMAI)).in(ThreadScope.class);

			}
		});
		s3FileDistributor = injector.getInstance(Key.get(FileDistributor.class, S3FileDistributorPublishFiles.class));
		//akamaiFTPFileDistributor = injector.getInstance(Key.get(FileDistributor.class, FTPFileDistributorAkamai.class));
		cdnetworksFTPFileDistributor = injector.getInstance(Key.get(FileDistributor.class, FTPFileDistributorCDNetworks.class));
	}

	@Test
	@Ignore
	public void testUploadS3() {
		assertNotNull(s3FileDistributor);
		assertTrue("resutl==" + s3FileDistributor.currentDirectory(), "aa".contains(s3FileDistributor.currentDirectory()));
	}

	@Test
	@Ignore
	public void testCurrentDirCDNNetwork() {
		assertNotNull(cdnetworksFTPFileDistributor);
		assertTrue("resutl==" + akamaiFTPFileDistributor.currentDirectory(), "/".contains(akamaiFTPFileDistributor.currentDirectory()));
	}

	@Test
	@Ignore
	public void testUploadCDNNetwork() {
		assertNotNull(cdnetworksFTPFileDistributor);

		Collection<File> listFiles = getFiles();

		for (File file : listFiles) {
			akamaiFTPFileDistributor.upload(file);
		}

		assertTrue("resutl==" + akamaiFTPFileDistributor.listNames(), akamaiFTPFileDistributor.listNames().toString().contains("utag.js"));
	}

	private static Collection<File> getFiles() {
		Collection<File> listFiles = FileUtils.listFiles(new File(FileDistributorTest.class.getClassLoader().getResource("").getFile()),
				new String[] { "js" }, false);
		return listFiles == null ? Collections.EMPTY_LIST : listFiles;

	}

}
