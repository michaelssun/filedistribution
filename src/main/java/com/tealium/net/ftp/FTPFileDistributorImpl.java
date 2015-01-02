package com.tealium.net.ftp;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPCommunicationListener;
import it.sauronsoftware.ftp4j.FTPConnector;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;
import it.sauronsoftware.ftp4j.FTPListParser;
import it.sauronsoftware.ftp4j.FTPTextualExtensionRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.tealium.net.CommunicationListener;
import com.tealium.net.DataTransferListener;
import com.tealium.net.FileMetaData;
import com.tealium.net.annotation.ThreadScope;
import com.tealium.net.exception.FileDistributorException;
import com.tealium.net.exception.FileDistributorException.Error;
import com.tealium.net.poolableresource.FtpUtil;

@ThreadScope
public class FTPFileDistributorImpl implements FTPFileDistributor {

	private static Logger logger = LoggerFactory.getLogger(FTPFileDistributorImpl.class);
	private FTPClient ftpClient;
	private FTPHelper tealiumFTPHelper;

	private static final Map<String, Boolean> MODE_Z_SUPPORT = new HashMap<String, Boolean>();

	public FTPFileDistributorImpl(Map<String, String> connParams) {
		ftpClient = new FTPClient();
		this.connect(connParams.get(FTP_HOST));
		this.login(connParams.get(FTP_USER_NAME), connParams.get(FTP_PASSWORD));

		ftpClient.setCompressionEnabled(isCompressSupported(connParams.get(FTP_HOST)));
		ftpClient.setPassive(Boolean.TRUE);
		if (connParams.get(ALIVE_AUTO_PING)!=null) {
			ftpClient.setAutoNoopTimeout(Long.parseLong(connParams.get(ALIVE_AUTO_PING)));
		}
		ftpClient.setType(FTPClient.TYPE_AUTO);
	}

	private synchronized boolean isCompressSupported(String host) {
		if (MODE_Z_SUPPORT.get(host) != null) {
			return MODE_Z_SUPPORT.get(host);
		}

		boolean modeZSupport = this.isCompressionSupported();
		MODE_Z_SUPPORT.put(host, modeZSupport);
		return modeZSupport;
	}

	@Override
	public FTPConnector getConnector() {
		return ftpClient.getConnector();
	}

	@Override
	public void setConnector(FTPConnector connector) {
		ftpClient.setConnector(connector);
	}

	@Override
	public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		ftpClient.setSSLSocketFactory(sslSocketFactory);

	}

	@Override
	public SSLSocketFactory getSSLSocketFactory() {
		return ftpClient.getSSLSocketFactory();
	}

	@Override
	public void setSecurity(int security) throws FileDistributorException {
		ftpClient.setSecurity(security);

	}

	@Override
	public int getSecurity() {
		return ftpClient.getSecurity();
	}

	@Override
	public void setPassive(boolean passive) {
		ftpClient.setPassive(passive);

	}

	@Override
	public void setType(int type) throws IllegalArgumentException {
		ftpClient.setType(type);

	}

	@Override
	public int getType() {
		return ftpClient.getType();
	}

	@Override
	public void setMLSDPolicy(int mlsdPolicy) {
		ftpClient.setMLSDPolicy(mlsdPolicy);

	}

	@Override
	public int getMLSDPolicy() {
		return ftpClient.getMLSDPolicy();
	}

	@Override
	public String getCharset() {
		return ftpClient.getCharset();
	}

	@Override
	public void setCharset(String charset) {
		ftpClient.setCharset(charset);

	}

	@Override
	public boolean isResumeSupported() {
		return ftpClient.isResumeSupported();
	}

	@Override
	public boolean isCompressionSupported() {
		return ftpClient.isCompressionSupported();
	}

	@Override
	public void setCompressionEnabled(boolean compressionEnabled) {
		ftpClient.setCompressionEnabled(compressionEnabled);

	}

	@Override
	public boolean isCompressionEnabled() {
		return ftpClient.isCompressionEnabled();
	}

	@Override
	public FTPTextualExtensionRecognizer getTextualExtensionRecognizer() {
		return ftpClient.getTextualExtensionRecognizer();
	}

	@Override
	public void setTextualExtensionRecognizer(FTPTextualExtensionRecognizer textualExtensionRecognizer) {
		ftpClient.setTextualExtensionRecognizer(textualExtensionRecognizer);

	}

	@Override
	public boolean isAuthenticated() {
		return ftpClient.isAuthenticated();
	}

	@Override
	public boolean isConnected() {
		return ftpClient.isConnected();
	}

	@Override
	public boolean isPassive() {
		return ftpClient.isPassive();
	}

	@Override
	public String getHost() {
		return ftpClient.getHost();
	}

	@Override
	public int getPort() {
		return ftpClient.getPort();
	}

	@Override
	public String getPassword() {
		return ftpClient.getPassword();
	}

	@Override
	public String getUsername() {
		return ftpClient.getUsername();
	}

	@Override
	public void setAutoNoopTimeout(long autoNoopTimeout) {
		ftpClient.setAutoNoopTimeout(autoNoopTimeout);

	}

	@Override
	public long getAutoNoopTimeout() {
		return ftpClient.getAutoNoopTimeout();
	}

	@Override
	public void addCommunicationListener(CommunicationListener listener) {
		final CommunicationListener listener1 = listener;
		ftpClient.addCommunicationListener(new FTPCommunicationListener() {

			@Override
			public void sent(String statement) {
				listener1.sent(statement);

			}

			@Override
			public void received(String statement) {
				listener1.received(statement);
			}
		});

	}

	@Override
	public void removeCommunicationListener(CommunicationListener listener) {
		if (!listener.getClass().isAssignableFrom(TealiumFTPCommunicationListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}

		ftpClient.removeCommunicationListener((TealiumFTPCommunicationListener) listener);

	}

	@Override
	public CommunicationListener[] getCommunicationListeners() {
		TealiumFTPCommunicationListener[] listeners = (TealiumFTPCommunicationListener[]) ftpClient.getCommunicationListeners();
		return listeners;
	}

	@Override
	public void addListParser(FTPListParser listParser) {
		ftpClient.addListParser(listParser);

	}

	@Override
	public void removeListParser(FTPListParser listParser) {
		ftpClient.removeListParser(listParser);

	}

	@Override
	public FTPListParser[] getListParsers() {
		return ftpClient.getListParsers();
	}

	@Override
	public String[] connect(String host) throws FileDistributorException {
		try {
			return ftpClient.connect(host);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to connect host - " + host, e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public String[] connect(String host, int port) throws FileDistributorException {
		try {
			return ftpClient.connect(host, port);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to connect host - " + host + "|port-" + port, e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public void disconnect(boolean sendQuitCommand) throws FileDistributorException {
		try {
			ftpClient.disconnect(sendQuitCommand);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to disconnect sendQuitCommand=" + sendQuitCommand, e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void abruptlyCloseCommunication() {
		ftpClient.abruptlyCloseCommunication();

	}

	private void login(String username, String password) throws FileDistributorException {
		try {
			ftpClient.login(username, password);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to login(" + username + "," + password + ")", e);
			throw new FileDistributorException(getError(e));
		}

	}

	private void login(String username, String password, String account) throws FileDistributorException {
		try {
			ftpClient.login(username, password, account);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to login(" + username + "," + password + "," + account + ")", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public void logout() throws FileDistributorException {
		try {
			ftpClient.logout();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to logout", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void noop() throws FileDistributorException {
		try {
			ftpClient.noop();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to noop", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public TealiumFTPReply sendCustomCommand(String command) throws FileDistributorException {
		try {
			return FtpUtil.getTealiumFTPReply(ftpClient.sendCustomCommand(command));
		} catch (IllegalStateException | IOException | FTPIllegalReplyException e) {
			logger.error("Error to sendCustomCommand[" + command + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public TealiumFTPReply sendSiteCommand(String command) throws FileDistributorException {
		try {
			return FtpUtil.getTealiumFTPReply(ftpClient.sendSiteCommand(command));
		} catch (IllegalStateException | IOException | FTPIllegalReplyException e) {
			logger.error("Error to sendSiteCommand[" + command + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public void changeAccount(String account) throws FileDistributorException {
		try {
			ftpClient.changeAccount(account);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to changeAccount[" + account + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public String currentDirectory() throws FileDistributorException {
		try {
			return ftpClient.currentDirectory();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to currentDirectory[" + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public void changeDirectory(String path) throws FileDistributorException {
		try {
			ftpClient.changeDirectory(path);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to changeDirectory[" + path + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void changeDirectoryUp() throws FileDistributorException {
		try {
			ftpClient.changeDirectoryUp();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to changeDirectoryUp[" + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public Date modifiedDate(String path) throws FileDistributorException {
		try {
			return ftpClient.modifiedDate(path);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to modifiedDate[" + path + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public long fileSize(String path) throws FileDistributorException {
		try {
			return ftpClient.fileSize(path);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to fileSize[" + path + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public void rename(String oldPath, String newPath) throws FileDistributorException {
		try {
			ftpClient.rename(oldPath, newPath);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to rename[" + oldPath + "," + newPath + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void deleteFile(String path) throws FileDistributorException {
		try {
			ftpClient.deleteFile(path);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to deleteFile[" + path + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void deleteDirectory(String path) throws FileDistributorException {
		try {
			ftpClient.deleteDirectory(path);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to deleteDirectory[" + path + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void createDirectory(String directoryName) throws FileDistributorException {
		try {
			ftpClient.createDirectory(directoryName);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to createDirectory[" + directoryName + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public String[] help() throws FileDistributorException {
		try {
			return ftpClient.help();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to help[" + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public Collection<FileMetaData> list(String fileSpec) throws FileDistributorException {
		try {
			FTPFile[] list = Strings.isNullOrEmpty(fileSpec) ? ftpClient.list() : ftpClient.list(fileSpec);
			if (list == null) {
				return Collections.EMPTY_LIST;
			}
			return FtpUtil.getFileMetaDataSet(Arrays.asList(list));
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException
				| FTPListParseException e) {
			logger.error("Error to list[" + fileSpec + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public Collection<FileMetaData> list() throws FileDistributorException {
		return this.list(null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> listNames() throws FileDistributorException {
		String[] listNames;
		try {
			listNames = ftpClient.listNames();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException
				| FTPListParseException e) {
			logger.error("Error to listNames[" + "]", e);
			throw new FileDistributorException(getError(e));
		}
		return listNames == null ? Collections.EMPTY_LIST : Arrays.asList(listNames);
	}

	@Override
	public void upload(File file) throws FileDistributorException {
		try {
			ftpClient.upload(file);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to upload[" + file.getName() + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void upload(File file, DataTransferListener listener) throws FileDistributorException {
		if (!listener.getClass().isAssignableFrom(TealiumDataTransferListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}

		try {
			ftpClient.upload(file, (TealiumDataTransferListener) listener);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to upload[" + file.getName() + "|listener..." + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void upload(File file, long restartAt) throws FileDistributorException {
		try {
			ftpClient.upload(file, restartAt);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to upload[" + file.getName() + "|startat-" + restartAt + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void upload(File file, long restartAt, DataTransferListener listener) throws FileDistributorException {
		if (!listener.getClass().isAssignableFrom(TealiumDataTransferListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}
		try {
			ftpClient.upload(file, restartAt, (TealiumDataTransferListener) listener);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to upload[" + file.getName() + "|startat-" + restartAt + "|datalistener" + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public void upload(String fileName, InputStream inputStream, long restartAt, long streamOffset, DataTransferListener listener)
			throws FileDistributorException {
		if (!listener.getClass().isAssignableFrom(TealiumDataTransferListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}
		try {
			ftpClient.upload(fileName, inputStream, restartAt, streamOffset, (TealiumDataTransferListener) listener);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to upload[" + fileName + "|startat-" + restartAt + "|streamOffset-" + streamOffset + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void append(File file) throws FileDistributorException {
		try {
			ftpClient.append(file);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to append[" + file.getName() + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void append(File file, DataTransferListener listener) throws FileDistributorException {
		if (!listener.getClass().isAssignableFrom(TealiumDataTransferListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}
		try {
			ftpClient.append(file, (TealiumDataTransferListener) listener);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to append[" + file.getName() + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void append(String fileName, InputStream inputStream, long streamOffset, DataTransferListener listener) throws FileDistributorException {
		if (!listener.getClass().isAssignableFrom(TealiumDataTransferListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}
		try {
			ftpClient.append(fileName, inputStream, streamOffset, (TealiumDataTransferListener) listener);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to append[" + fileName + "|streamOffset-" + streamOffset + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}
	
	@Override
	public void download(String remoteFileName, File localFile) throws FileDistributorException {
		 this.download(remoteFileName, localFile, "");
	}

	@Override
	public boolean download(String remoteFileName, File localFile, String remotePath) throws FileDistributorException {
		try {
			if (!Strings.isNullOrEmpty(remotePath)) {
				ftpClient.changeDirectory(remotePath);
			}
			ftpClient.download(remoteFileName, localFile);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to download[" + remoteFileName + "," + localFile + "]", e);
			throw new FileDistributorException(getError(e));
		}
		return Boolean.TRUE;
	}

	@Override
	public void download(String remoteFileName, File localFile, DataTransferListener listener) throws FileDistributorException {
		if (!listener.getClass().isAssignableFrom(TealiumDataTransferListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}
		try {
			ftpClient.download(remoteFileName, localFile, (TealiumDataTransferListener) listener);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to download[" + remoteFileName + "," + localFile + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void download(String remoteFileName, File localFile, long restartAt) throws FileDistributorException {
		try {
			ftpClient.download(remoteFileName, localFile, restartAt);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to download[" + remoteFileName + "," + localFile + "," + restartAt + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void download(String remoteFileName, File localFile, long restartAt, DataTransferListener listener) throws FileDistributorException {
		if (!listener.getClass().isAssignableFrom(TealiumDataTransferListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}
		try {
			ftpClient.download(remoteFileName, localFile, restartAt, (TealiumDataTransferListener) listener);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to download[" + remoteFileName + "," + localFile + "," + restartAt + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void download(String fileName, OutputStream outputStream, long restartAt, DataTransferListener listener) throws FileDistributorException {
		if (!listener.getClass().isAssignableFrom(TealiumDataTransferListener.class)) {
			throw new IllegalArgumentException("Not supported for Listener - " + listener.getClass());
		}
		try {
			ftpClient.download(fileName, outputStream, restartAt, (TealiumDataTransferListener) listener);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException | FTPDataTransferException | FTPAbortedException e) {
			logger.error("Error to download[" + fileName + "," + restartAt + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public void abortCurrentDataTransfer(boolean sendAbortCommand) throws FileDistributorException {
		try {
			ftpClient.abortCurrentDataTransfer(sendAbortCommand);
		} catch (IOException | FTPIllegalReplyException e) {
			logger.error("Error to abortCurrentDataTransfer[" + sendAbortCommand + "]", e);
			throw new FileDistributorException(getError(e));
		}

	}

	@Override
	public boolean purgeDestFiles(Collection<String> fileNames) {
		return tealiumFTPHelper.purgeRequest(fileNames);
	}

	@Override
	public String[] destinationStatus() throws FileDistributorException {

		try {
			return ftpClient.serverStatus();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			logger.error("Error to destinationStatus[" + "]", e);
			throw new FileDistributorException(getError(e));
		}
	}

	@Override
	public boolean uploadAndRemoveLocal(File file) throws FileDistributorException {
		this.upload(file);

		if (file.exists()) {
			return file.delete();
		}

		return Boolean.TRUE;

	}

	@Override
	public void upload(File localFile, String remoteFileName, String remotePath) throws FileDistributorException {
		if (!Strings.isNullOrEmpty(remotePath) ){
			this.changeDirectory(remotePath);
		}
		upload(localFile);
		if (!Strings.isNullOrEmpty(remoteFileName)&&remoteFileName.equals(localFile.getName())) {
			rename(localFile.getName(), remoteFileName);
		}
	}
	
	private static Error getError(Exception e) {
		if (e.getClass().isAssignableFrom(IllegalStateException.class)) {
			return Error.IllegalState;
		}

		if (e.getClass().isAssignableFrom(IOException.class)) {
			return Error.IOException;
		}

		if (e.getClass().isAssignableFrom(FTPIllegalReplyException.class)) {
			return Error.IllegalReply;
		}

		if (e.getClass().isAssignableFrom(FTPException.class)) {
			return Error.FTPGeneric;
		}

		if (e.getClass().isAssignableFrom(FTPDataTransferException.class)) {
			return Error.DataTransfer;
		}

		if (e.getClass().isAssignableFrom(FTPAbortedException.class)) {
			return Error.Aborted;
		}

		if (e.getClass().isAssignableFrom(FTPListParseException.class)) {
			return Error.ListParse;
		}
		if (e.getClass().isAssignableFrom(FileNotFoundException.class)) {
			return Error.FileNotFound;
		}

		return Error.Unknown;

	}

}
