/*
 * Copyright 2002-2010 the original author or authors.
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

package com.tealium.integration.ftp.session;

import it.sauronsoftware.ftp4j.FTPClient;

import java.io.IOException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;

/**
 * SessionFactory for FTPS.
 * 
 * @author Josh Long
 * @author Iwein Fuld
 * @author Mark Fisher
 * @since 2.0
 */
public class DefaultFtpsSessionFactory extends AbstractFtpSessionFactory<FTPClient> {

	private Boolean useClientMode;

	private Boolean sessionCreation;

	private String authValue;

	private TrustManager trustManager;

	private String[] cipherSuites;

	private String[] protocols;

	private KeyManager keyManager;

	private Boolean needClientAuth;

	private Boolean wantsClientAuth;

	private boolean implicit = false;

	private String prot = "P";

	private String protocol;


	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setUseClientMode(Boolean useClientMode) {
		this.useClientMode = useClientMode;
	}

	public void setSessionCreation(Boolean sessionCreation) {
		this.sessionCreation = sessionCreation;
	}

	public void setAuthValue(String authValue) {
		this.authValue = authValue;
	}

	public void setTrustManager(TrustManager trustManager) {
		this.trustManager = trustManager;
	}

	public void setCipherSuites(String[] cipherSuites) {
		this.cipherSuites = cipherSuites;
	}

	public void setProtocols(String[] protocols) {
		this.protocols = protocols;
	}

	public void setKeyManager(KeyManager keyManager) {
		this.keyManager = keyManager;
	}

	public void setNeedClientAuth(Boolean needClientAuth) {
		this.needClientAuth = needClientAuth;
	}

	public void setWantsClientAuth(Boolean wantsClientAuth) {
		this.wantsClientAuth = wantsClientAuth;
	}

	public void setProt(String prot) {
		this.prot = prot;
	}

	public void setImplicit(boolean implicit) {
		this.implicit = implicit;
	}

	@Override
	protected FTPClient createClientInstance() {
		FTPClient client=new FTPClient();
		try {
			
				client.setSecurity(FTPClient.SECURITY_FTPS);
				return client;
			
		}
		catch (Exception e) {
		    
			/* 
			 This catch block is technically not necessary but it allows users 
			 to use the older Commons Net 2.0 if necessary, which requires you 
			 to catch a NoSuchAlgorithmException. 
			 */
			
			if (e instanceof RuntimeException) {
		        throw (RuntimeException) e;
		    }
		    
			throw new RuntimeException("Failed to create FTPS client.", e);
		}
	}

	@Override
	protected void postProcessClientAfterConnect(FTPClient ftpsClient) throws IOException { 
	}

	@Override
	protected void postProcessClientBeforeConnect(FTPClient ftpsClient) throws IOException {
		
	}

}
