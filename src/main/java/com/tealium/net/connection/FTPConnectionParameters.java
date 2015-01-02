package com.tealium.net.connection;

import java.util.HashMap;
import java.util.Map;

import com.tealium.net.ftp.FTPFileDistributor;

public class FTPConnectionParameters implements ConnectionParameters {
	private String host;
	private String userName;
	private String password;
	private String aliveAutoPing;

	public FTPConnectionParameters(String host, String userName, String password, String aliveAutoPing) {
		this.host = host;
		this.userName = userName;
		this.password = password;
		this.aliveAutoPing = aliveAutoPing;
	}

	@Override
	public Map<String, String> getConnParams() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(FTPFileDistributor.FTP_HOST, host);
		map.put(FTPFileDistributor.FTP_PASSWORD, password);
		map.put(FTPFileDistributor.FTP_USER_NAME, userName);
		map.put(FTPFileDistributor.ALIVE_AUTO_PING, aliveAutoPing);
		return map;
	}

}
