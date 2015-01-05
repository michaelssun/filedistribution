/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tealium.integration.ftp;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.*;
import com.tealium.integration.ftp.session.DefaultFtpSessionFactory;


public class FtpParserInboundTests {
	private DefaultFtpSessionFactory ftpSessionFactory;
	
	@Before
	public void before(){
		new File("target/foo").delete();
		
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(DefaultFtpSessionFactory.class).toInstance(
						new DefaultFtpSessionFactory());
				 

				// bind(String.class).annotatedWith(Names.named("legacy_utui_host")).toInstance("junit_utui_host");

			}


		});
		ftpSessionFactory = injector.getInstance(DefaultFtpSessionFactory.class);
	}

	@Test
	public void testLocalFilesAutoCreationTrue() throws Exception{
		assertTrue(!new File("target/foo").exists());
		assertTrue(new File("target/foo").exists());
		assertTrue(!new File("target/bar").exists());
	}
	

	@After
	public void cleanUp() throws Exception{
		new File("target/foo").delete();
	}
}
