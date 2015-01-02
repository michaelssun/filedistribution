package com.tealium.net.ftp.benchmark;

import java.util.Date;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class TestDataTransferListener implements FTPDataTransferListener {

	public void started() {
		// Transfer started
		System.out.println("TRANSFER-STATUS: File transfer started... " + new Date());
	}

	public void transferred(int length) {
		// Yet other length bytes has been transferred since the last time this
		// method was called
	}

	public void completed() {
		// Transfer completed
		System.out.println("TRANSFER-STATUS: File transfer completed..." + new Date());
	}

	public void aborted() {
		// Transfer aborted
		System.err.println("TRANSFER-STATUS: File transfer aborted...");
	}

	public void failed() {
		// Transfer failed
		System.err.println("TRANSFER-STATUS: File transfer failed...");
	}
}