package com.tealium.net.ftp;

import java.util.Collection;

public interface FTPHelper {
	/**
	 * send purge request to CDN
	 * 
	 * @param fileNames
	 * @return
	 */
	public boolean purgeRequest(Collection<String> fileNames);
}
