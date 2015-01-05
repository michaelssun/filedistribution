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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class FtpMessageHistoryTests {
	private SourcePollingChannelAdapter adapter;
	
	@Before
	public void before(){
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(SourcePollingChannelAdapter.class).toInstance(
						new SourcePollingChannelAdapter());
			}


		});
		adapter = injector.getInstance(SourcePollingChannelAdapter.class); 
		adapter.setComponentName("adapterFtp");
		assertNotNull(adapter);
	}
	
	
	@Test
	public void testMessageHistory() throws Exception{
		
		assertEquals("adapterFtp", adapter.getComponentName());
	}
}
