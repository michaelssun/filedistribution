package com.tealium.net.provider;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.tealium.net.FileDistributor;
import com.tealium.net.aws.s3.S3FileDistributor;
import com.tealium.net.aws.s3.S3FileDistributorImpl;
import com.tealium.net.ftp.FTPFileDistributor;
import com.tealium.net.ftp.FTPFileDistributorImpl;

/**
 * connection parameter provider
 *
 */

public class FileDistributorProvider implements Provider<FileDistributor> {
	public enum ConnectionType {
		AWS_S3, FTP_CDN_AKAMAI, FTP_CDN_EDGECACHE, FTP_CDN_LIMELIGHT, FTP_CDN_NETWORKS
	};

	private ConnectionType connectionType;

	@Inject
	@Named(S3FileDistributor.ACCESS_KEY)
	private String s3AccessKey;

	@Inject
	@Named(S3FileDistributor.ACCESS_KEY_ID)
	private String s3AccessKeyId;

	@Inject
	@Named(S3FileDistributor.END_POINT)
	private String s3AccessEndPoint;

	@Inject
	@Named(FTPFileDistributor.ALIVE_AUTO_PING_AKAMAI)
	private String ftpAliveAutoPingAkamai;
	@Inject
	@Named(FTPFileDistributor.FTP_HOST_AKAMAI)
	private String ftpHostAkamai;
	@Inject
	@Named(FTPFileDistributor.FTP_USER_NAME_AKAMAI)
	private String ftpUserNameAkamai;
	@Inject
	@Named(FTPFileDistributor.FTP_PASSWORD_AKAMAI)
	private String ftpPasswordAkamai;

	@Inject
	@Named(FTPFileDistributor.ALIVE_AUTO_PING_EDGECACHE)
	private String ftpAliveAutoPingEdgeCache;
	@Inject
	@Named(FTPFileDistributor.FTP_HOST_EDGECACHE)
	private String ftpHostEdgeCache;
	@Inject
	@Named(FTPFileDistributor.FTP_USER_NAME_EDGECACHE)
	private String ftpUserNameEdgeCache;
	@Inject
	@Named(FTPFileDistributor.FTP_PASSWORD_EDGECACHE)
	private String ftpPasswordEdgeCache;

	@Inject
	@Named(FTPFileDistributor.ALIVE_AUTO_PING_LIMELIGHT)
	private String ftpAliveAutoPingLimeLight;
	@Inject
	@Named(FTPFileDistributor.FTP_HOST_LIMELIGHT)
	private String ftpHostLimeLight;
	@Inject
	@Named(FTPFileDistributor.FTP_USER_NAME_LIMELIGHT)
	private String ftpUserNameLimeLight;
	@Inject
	@Named(FTPFileDistributor.FTP_PASSWORD_LIMELIGHT)
	private String ftpPasswordLimeLight; 
	

	@Inject
	@Named(FTPFileDistributor.ALIVE_AUTO_PING_CDNETWORKS)
	private String ftpAliveAutoPingCDNetworks;
	@Inject
	@Named(FTPFileDistributor.FTP_HOST_CDNETWORKS)
	private String ftpHostCDNetworks;
	@Inject
	@Named(FTPFileDistributor.FTP_USER_NAME_CDNETWORKS)
	private String ftpUserNameCDNetworks;
	@Inject
	@Named(FTPFileDistributor.FTP_PASSWORD_CDNETWORKS)
	private String ftpPasswordCDNetworks;
	// todo: ftp conn param for edgecache and limelight

	private final static Map<ConnectionType, Map<String, String>> paramMap = new HashMap<FileDistributorProvider.ConnectionType, Map<String, String>>();

	public FileDistributorProvider(ConnectionType connectionType) {
		this.connectionType = connectionType;
	}

	@Override
	public FileDistributor get() {
		if (paramMap.get(connectionType) == null) {
			setParamMap();
		}
		if (ConnectionType.AWS_S3.equals(connectionType)) {
			return new S3FileDistributorImpl(paramMap.get(ConnectionType.AWS_S3));
		}
		return new FTPFileDistributorImpl( paramMap.get(connectionType));
	}

	private void setParamMap() {
		paramMap.put(ConnectionType.AWS_S3, getS3ConnParams(s3AccessKey, s3AccessKeyId, s3AccessEndPoint));

		paramMap.put(ConnectionType.FTP_CDN_AKAMAI, getFTPConnParams(ftpHostAkamai, ftpUserNameAkamai, ftpPasswordAkamai, ftpAliveAutoPingAkamai));
		paramMap.put(ConnectionType.FTP_CDN_LIMELIGHT,
				getFTPConnParams(ftpHostLimeLight, ftpUserNameLimeLight, ftpPasswordLimeLight, ftpAliveAutoPingLimeLight));
		paramMap.put(ConnectionType.FTP_CDN_EDGECACHE,
				getFTPConnParams(ftpHostEdgeCache, ftpUserNameEdgeCache, ftpPasswordEdgeCache, ftpAliveAutoPingEdgeCache));
		paramMap.put(ConnectionType.FTP_CDN_NETWORKS,
				getFTPConnParams(ftpHostCDNetworks, ftpUserNameCDNetworks, ftpPasswordCDNetworks, ftpAliveAutoPingCDNetworks));

	}

	private Map<String, String> getS3ConnParams(String accessKey, String accessKeyId, String endpoint) {
		Map<String, String> connParams = new HashMap<String, String>();
		connParams.put(S3FileDistributor.ACCESS_KEY, accessKey);
		connParams.put(S3FileDistributor.ACCESS_KEY_ID, accessKeyId);
		connParams.put(S3FileDistributor.END_POINT, endpoint);

		return connParams;
	}

	private Map<String, String> getFTPConnParams(String host, String userName, String password, String aliveAutoPing) {
		Map<String, String> connParams = new HashMap<String, String>();
		connParams.put(FTPFileDistributor.FTP_HOST, host);
		connParams.put(FTPFileDistributor.FTP_USER_NAME, userName);
		connParams.put(FTPFileDistributor.FTP_PASSWORD, password);
		connParams.put(FTPFileDistributor.ALIVE_AUTO_PING, aliveAutoPing);

		return connParams;
	}
}
