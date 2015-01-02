package com.tealium.net.ftp;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import com.tealium.net.DataTransferListener;

public class TealiumDataTransferListener implements DataTransferListener, FTPDataTransferListener {
	private Collection<String> filesToTransfer;
	private static Logger logger = LoggerFactory.getLogger(TealiumDataTransferListener.class);

	public TealiumDataTransferListener(Collection<String> filesToTransfer) {
		this.filesToTransfer = filesToTransfer;
	}

	@Override
	public void started() {
		if (logger.isDebugEnabled()) {
			logger.debug(filesToTransfer + " - Starting data transfer...");
		}
	}

	@Override
	public void transferred(int length) {
		if (logger.isDebugEnabled()) {
			logger.debug(filesToTransfer + " -  bytes transferred");
		}

	}

	@Override
	public void completed() {
		if (logger.isDebugEnabled()) {
			logger.debug(filesToTransfer + " - Data transfer completes");
		}
	}

	@Override
	public void aborted() {
		if (logger.isDebugEnabled()) {
			logger.debug(filesToTransfer + " - Data transfer was aborted");
		}
	}

	@Override
	public void failed() {
		if (logger.isDebugEnabled()) {
			logger.debug(filesToTransfer + " - Data transfer failed");
		}
	}

}
