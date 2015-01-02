package com.tealium.net;


import it.sauronsoftware.ftp4j.FTPClient;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;

import com.tealium.net.annotation.ThreadScope;
import com.tealium.net.exception.FileDistributorException;


public interface FileDistributor {
	/**
	 * The constant for the AUTO file transfer type. It lets the client pick
	 * between textual and binary types, depending on the extension of the file
	 * exchanged through a textual extension recognizer.
	 */
	public static final int TYPE_AUTO = 0;
	/**
	 * The constant for the TEXTUAL file transfer type. It means that the data
	 * sent or received is treated as textual information. This implies charset
	 * conversion during the transfer.
	 */
	public static final int TYPE_TEXTUAL = 1;
	/**
	 * The constant for the BINARY file transfer type. It means that the data
	 * sent or received is treated as a binary stream. The data is taken "as
	 * is", without any charset conversion.
	 */
	public static final int TYPE_BINARY = 2;

	/**
	 * purge remote files
	 * 
	 * @param fileNames
	 * @return
	 */
	public boolean purgeDestFiles(Collection<String> fileNames);
	
	
	/**
	 * This method tests if this client is authenticated.
	 * 
	 * @return true if this client is authenticated, false otherwise.
	 */
	public  boolean isAuthenticated();
	


	/**
	 * If there's any ongoing data transfer operation, this method aborts it.
	 * 
	 * @param sendAbortCommand
	 *            If true the client will negotiate the abort procedure with the
	 *            destination. Otherwise the
	 *            open data transfer connection will be closed without any
	 *            advise sent to the destination.
	 * @throws FileDistributorException
	 */
	public  void abortCurrentDataTransfer(boolean sendAbortCommand) throws FileDistributorException;

	

	/**
	 * This method tests if this client is connected to a remote FTP destination.
	 * 
	 * @return true if this client is connected to a remote FTP destination, false
	 *         otherwise.
	 */
	public  boolean isConnected();
	
	/**
	 * This method adds a CommunicationListener to the object.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public  void addCommunicationListener(CommunicationListener listener);

	/**
	 * This method removes a CommunicationListener previously added to the
	 * object.
	 * 
	 * @param listener
	 *            The listener to be removed.
	 */
	public  void removeCommunicationListener(CommunicationListener listener);

	/**
	 * This method returns a list with all the {@link CommunicationListener}
	 * used by the client.
	 * 
	 * @return A list with all the FTPCommunicationListener used by the client.
	 */
	public  CommunicationListener[] getCommunicationListeners();



	/**
	 * This method causes the communication channel to be abruptly closed. Use
	 * it carefully, since this one is not thread-safe. It is given as an
	 * "emergency brake" to close the control connection when it is blocked. A
	 * thread-safe solution for the same purpose is a call to disconnect(false).
	 * 
	 * @see FTPClient#disconnect(boolean)
	 */
	public  void abruptlyCloseCommunication();
	

	/**
	 * Checks whether the destination explicitly supports resuming of
	 * broken data transfers.
	 * 
	 * @return true if the destination supports resuming, false otherwise.
	 */
	public  boolean isResumeSupported();



	/**
	 * This method asks and returns the current working directory.
	 * 
	 * @return path The path to the current working directory.
	 * @throws FileDistributorException
	 */
	public  String currentDirectory() throws FileDistributorException;

	/**
	 * This method changes the current working directory.
	 * 
	 * @param path
	 *            The path to the new working directory.
	 * @throws FileDistributorException
	 */
	public  void changeDirectory(String path) throws FileDistributorException;

	/**
	 * This method changes the current working directory to the parent one.
	 * 
	 * @throws FileDistributorException
	 */
	public  void changeDirectoryUp() throws FileDistributorException;

	/**
	 * This method asks and returns the last modification date of a file or
	 * directory.
	 * 
	 * @param path
	 *            The path to the file or the directory.
	 * @return The file/directory last modification date.
	 * @throws FileDistributorException
	 */
	public  Date modifiedDate(String path) throws FileDistributorException;

	/**
	 * This method asks and returns a file size in bytes.
	 * 
	 * @param path
	 *            The path to the file.
	 * @return The file size in bytes.
	 * @throws FileDistributorException
	 */
	public  long fileSize(String path) throws FileDistributorException;

	/**
	 * This method renames a remote file or directory. It can also be used to
	 * move a file or a directory.
	 * 
	 * In example:
	 * 
	 * <pre>
	 * client.rename(&quot;oldname&quot;, &quot;newname&quot;); // This one renames
	 * </pre>
	 * 
	 * <pre>
	 * client.rename(&quot;the/old/path/oldname&quot;, &quot;/a/new/path/newname&quot;); // This one moves
	 * </pre>
	 * 
	 * @param oldPath
	 *            The current path of the file (or directory).
	 * @param newPath
	 *            The new path for the file (or directory).
	 * @throws FileDistributorException
	 */
	public  void rename(String oldPath, String newPath) throws FileDistributorException;

	/**
	 * This method deletes a remote file.
	 * 
	 * @param path
	 *            The path to the file.
	 * @throws FileDistributorException
	 */
	public  void deleteFile(String path) throws FileDistributorException;

	/**
	 * This method deletes a remote directory.
	 * 
	 * @param path
	 *            The path to the directory.
	 * @throws FileDistributorException
	 */
	public  void deleteDirectory(String path) throws FileDistributorException;

	/**
	 * This method creates a new remote directory in the current working one.
	 * 
	 * @param directoryName
	 *            The name of the new directory.
	 * @throws FileDistributorException
	 */
	public  void createDirectory(String directoryName) throws FileDistributorException;


	/**
	 * This method lists the entries of the current working directory 
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed.
	 * 
	 * @param fileSpec
	 *            A file filter string. Depending on the destination implementation,
	 *            wildcard characters could be accepted.
	 * @return The list of the files (and directories) in the current working
	 *         directory.
	 * @throws FileDistributorException
	 */
	public  Collection<FileMetaData> list(String fileSpec) throws FileDistributorException ;

	/**
	 * This method lists the entries of the current working directory 
	 * 
	 * @return The list of the files (and directories) in the current working
	 *         directory.
	 * @throws FileDistributorException
	 */
	public  Collection<FileMetaData> list() throws FileDistributorException;

	/**
	 * This method lists the entries of the current working directory 
	 * 
	 * The response consists in an array of string, each one reporting the name
	 * of a file or a directory placed in the current working directory. For a
	 * more detailed directory listing procedure look at the list() method.
	 * 
	 * @return The list of the files (and directories) in the current working
	 *         directory.
	 * @throws FileDistributorException
	 */
	public  Collection<String> listNames() throws FileDistributorException;

	/**
	 * This method uploads a file to the remote destination.
	 * 
	 * 
	 * @param file
	 *            The file to upload.
	 * @throws FileDistributorException
	 */
	public  void upload(File file) throws FileDistributorException;

	/**
	 * This method uploads a file to the destination.
	 * 
	 * Calling this method should block the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). 
	 * 
	 * @param file
	 *            The file to upload.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws FileDistributorException
	 */
	public  void upload(File file, DataTransferListener listener) throws FileDistributorException;

	/**
	 * This method uploads a file to the destination by resuming from a stop point.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). 
	 * 
	 * @param file
	 *            The file to upload.
	 * @param restartAt
	 *            The restart point (number of bytes already uploaded). Use
	 *            {@link isResumeSupported()} to check if the destination
	 *            supports resuming of broken data transfers.
	 * @throws FileDistributorException
	 */
	public  void upload(File file, long restartAt) throws FileDistributorException;

	/**
	 * This method uploads a file to the destination.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). 
	 * 
	 * @param file
	 *            The file to upload.
	 * @param restartAt
	 *            The restart point (number of bytes already uploaded). Use
	 *            {@link isResumeSupported()} to check if the destination
	 *            supports resuming of broken data transfers.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws FileDistributorException 
	 */
	public  void upload(File file, long restartAt, DataTransferListener listener) throws FileDistributorException;

	/**
	 * This method move a content to the destination without keeping local copy.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). 
	 * 
	 * @param fileName
	 *            The name of the remote file.
	 * @param inputStream
	 *            The source of data.
	 * @param restartAt
	 *            The restart point (number of bytes already uploaded). Use
	 *            {@link isResumeSupported()} to check if the destination
	 *            supports resuming of broken data transfers.
	 * @param streamOffset
	 *            The offset to skip in the stream.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws FileDistributorException
	 */
	public  void upload(String fileName, InputStream inputStream, long restartAt, long streamOffset, DataTransferListener listener)
			throws FileDistributorException;

	/**
	 * This method uploads a file to the remote destination and deletes the local file
	 * 
	 * 
	 * @param file
	 *            The file to upload.
	 * @return 
	 * @throws FileDistributorException
	 */
	public  boolean uploadAndRemoveLocal(File file) throws FileDistributorException;
	

	/**
	 * This method uploads a file to the destination.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). The method will break with an AbortedException.
	 * 
	 * @param file
	 *            The local file whose contents will be appended to the remote
	 *            file.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws FileDistributorException
	 *             If any exception occurs.  
	 */
	public  void append(File file, DataTransferListener listener) throws FileDistributorException;

	/**
	 * This method appends data to an existing file on the destination.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). 
	 * 
	 * @param fileName
	 *            The name of the remote file.
	 * @param inputStream
	 *            The source of data.
	 * @param streamOffset
	 *            The offset to skip in the stream.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws FileDistributorException
	 *             If any exception occurs.  
	 */
	public  void append(String fileName, InputStream inputStream, long streamOffset, DataTransferListener listener)
			throws FileDistributorException;
	
	

	/**
	 * This method appends the contents of a local file to an existing file on
	 * the destination.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer().
	 * 
	 * @param file
	 *            The local file whose contents will be appended to the remote
	 *            file.
	 * @throws FileDistributorException
	 *             If any exception occurs.  
	 */
	public  void append(File file) throws FileDistributorException;


	/**
	 * This method downloads a remote file  to a local file.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). 
	 * 
	 * @param remoteFileName
	 *            The name of the file to download.
	 * @param localFile
	 *            The local file.
	 * @param remotePath remote directory or bucket name
	 * @return TODO
	 * @throws FileDistributorException
	 *             If any exception occurs.  
	 */
	public  boolean download(String remoteFileName, File localFile, String remotePath) throws FileDistributorException;

	/**
	 * This method downloads a remote file  to a local file.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer(). 
	 * 
	 * @param remoteFileName
	 *            The name of the file to download.
	 * @param localFile
	 *            The local file.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws FileDistributorException
	 *             If any exception occurs.  
	 */
	public  void download(String remoteFileName, File localFile,  DataTransferListener listener) throws FileDistributorException;

	/**
	 * This method resumes a download operation from the remote to a
	 * local file.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer().
	 * 
	 * @param remoteFileName
	 *            The name of the file to download.
	 * @param localFile
	 *            The local file.
	 * @param restartAt
	 *            The restart point (number of bytes already downloaded). Use
	 *            {@link isResumeSupported()} to check if the destination
	 *            supports resuming of broken data transfers.
	 * @throws FileDistributorException
	 *             If any exception occurs.  
	 */
	public  void download(String remoteFileName, File localFile, long restartAt) throws FileDistributorException;

	/**
	 * This method resumes a download operation from the destination to a
	 * local file.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer().
	 * 
	 * @param remoteFileName
	 *            The name of the file to download.
	 * @param localFile
	 *            The local file.
	 * @param restartAt
	 *            The restart point (number of bytes already downloaded). Use
	 *            {@link #isResumeSupported()} to check if the destination
	 *            supports resuming of broken data transfers.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws FileDistributorException
	 *             If any exception occurs.  
	 */
	public  void download(String remoteFileName, File localFile, long restartAt,  DataTransferListener listener)
			throws FileDistributorException;

	/**
	 * This method resumes a download operation from the remote.
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. The operation could be interrupted by another thread calling
	 * abortCurrentDataTransfer().
	 * 
	 * @param fileName
	 *            The name of the remote file.
	 * @param outputStream
	 *            The destination stream of data read during the download.
	 * @param restartAt
	 *            The restart point (number of bytes already downloaded). Use
	 *            {@link #isResumeSupported()} to check if the destination
	 *            supports resuming of broken data transfers.
	 * @param listener
	 *            The listener for the operation. Could be null.
	 * @throws FileDistributorException
	 *             If any exception occurs.  
	 */
	public  void download(String fileName, OutputStream outputStream, long restartAt,  DataTransferListener listener)
			throws FileDistributorException;


	/**
	 * 	This method uploads a file to the destination with special name and in specified directory
	 * 
	 * Calling this method blocks the current thread until the operation is
	 * completed. 
	 * 
	 * @param localFile
	 * @param remoteFileName
	 * @param remotePath
	 * @throws FileDistributorException
	 */
	public void upload(File localFile, String remoteFileName, String remotePath) throws FileDistributorException;
	
	
	
}
