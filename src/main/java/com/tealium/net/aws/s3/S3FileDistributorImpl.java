package com.tealium.net.aws.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.ThreadSafe;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.tealium.net.CommunicationListener;
import com.tealium.net.DataTransferListener;
import com.tealium.net.FileMetaData;
import com.tealium.net.annotation.ThreadScope;
import com.tealium.net.exception.FileDistributorException;
import com.tealium.net.exception.FileDistributorException.Error;

@ThreadSafe
@ThreadScope
public class S3FileDistributorImpl implements S3FileDistributor {
	private static Logger logger = LoggerFactory
			.getLogger(S3FileDistributorImpl.class);

	private final Map<String, String> connParams;

	private AmazonS3 s3Client;

	public S3FileDistributorImpl(Map<String, String> connParams) {
		this.connParams = connParams;

		AWSCredentials credentials = new BasicAWSCredentials(
				connParams.get(ACCESS_KEY_ID), connParams.get(ACCESS_KEY));

		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTPS);

		s3Client = new AmazonS3Client(credentials, clientConfig);
		s3Client.setEndpoint(connParams.get(END_POINT));
	}

	@Override
	public boolean purgeDestFiles(Collection<String> fileNames) {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

		return false;
	}

	@Override
	public boolean isAuthenticated() {

		return false;
	}

	@Override
	public void abortCurrentDataTransfer(boolean sendAbortCommand)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public boolean isConnected() {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return false;
	}

	@Override
	public void addCommunicationListener(CommunicationListener listener) {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void removeCommunicationListener(CommunicationListener listener) {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public CommunicationListener[] getCommunicationListeners() {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return null;
	}

	@Override
	public void abruptlyCloseCommunication() {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public boolean isResumeSupported() {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return false;
	}

	@Override
	public String currentDirectory() throws FileDistributorException {
		List<Bucket> listBuckets = s3Client.listBuckets();
		return listBuckets != null ? "" + listBuckets.indexOf(0) : "";
	}

	@Override
	public void changeDirectory(String path) throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void changeDirectoryUp() throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
	}

	@Override
	public Date modifiedDate(String path) throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return null;
	}

	@Override
	public long fileSize(String path) throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return 0;
	}

	@Override
	public void rename(String oldPath, String newPath)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void deleteFile(String path) throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void deleteDirectory(String path) throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void createDirectory(String directoryName)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public Collection<FileMetaData> list(String fileSpec)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return null;
	}

	@Override
	public Collection<FileMetaData> list() throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return null;
	}

	@Override
	public Collection<String> listNames() throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return null;
	}

	@Override
	public void upload(File localFile, String remoteFileName, String bucketName)
			throws FileDistributorException {

		ByteArrayInputStream input;
		try {
			input = new ByteArrayInputStream(
					FileUtils.readFileToByteArray(localFile));
			s3Client.putObject(bucketName, remoteFileName, input,
					new ObjectMetadata());
		} catch (IOException e) {
			logger.error("Error to upload - " + localFile + "|remotefile-"
					+ remoteFileName + "|" + bucketName, e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public void upload(File file, DataTransferListener listener)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void upload(File file, long restartAt)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void upload(File file, long restartAt, DataTransferListener listener)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void upload(String fileName, InputStream inputStream,
			long restartAt, long streamOffset, DataTransferListener listener)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public boolean uploadAndRemoveLocal(File file)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}
		return false;
	}

	@Override
	public void append(File file, DataTransferListener listener)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void append(String fileName, InputStream inputStream,
			long streamOffset, DataTransferListener listener)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void append(File file) throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public boolean download(String remoteFileName, File localFile,
			String remotePath) throws FileDistributorException {
		ObjectMetadata objectMetaData = s3Client.getObject(
				new GetObjectRequest(remotePath, remoteFileName), localFile);
		if (logger.isDebugEnabled()) {
			logger.debug("downloaded file: " + objectMetaData);
		}

		return Boolean.TRUE;
	}

	@Override
	public void download(String remoteFileName, File localFile,
			DataTransferListener listener) throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void download(String remoteFileName, File localFile, long restartAt)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void download(String remoteFileName, File localFile, long restartAt,
			DataTransferListener listener) throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void download(String fileName, OutputStream outputStream,
			long restartAt, DataTransferListener listener)
			throws FileDistributorException {
		if (true) {
			throw new IllegalArgumentException("Not implemented");
		}

	}

	@Override
	public void upload(File file) throws FileDistributorException {
		String currentBucket = this.currentDirectory();
		this.upload(file, file.getName(), currentBucket);
	}

	private static Error getError(Exception e) {
		if (e.getClass().isAssignableFrom(IllegalStateException.class)) {
			return Error.IllegalState;
		}

		if (e.getClass().isAssignableFrom(IOException.class)) {
			return Error.IOException;
		}

		return Error.Unknown;

	}

}
