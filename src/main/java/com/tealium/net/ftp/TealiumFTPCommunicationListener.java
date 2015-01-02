package com.tealium.net.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.sauronsoftware.ftp4j.FTPCommunicationListener;

import com.tealium.net.CommunicationListener;

public class TealiumFTPCommunicationListener implements CommunicationListener, FTPCommunicationListener {
 private static Logger logger = LoggerFactory.getLogger(TealiumFTPCommunicationListener.class);
	@Override
	public void sent(String statement) {
		if (logger.isDebugEnabled()) {
			logger.debug(statement);
		}

	}

	@Override
	public void received(String statement) {
		if (logger.isDebugEnabled()) {
			logger.debug(statement);
		}
	}

}
