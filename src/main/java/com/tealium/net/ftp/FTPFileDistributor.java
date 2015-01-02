package com.tealium.net.ftp;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPConnector;
import it.sauronsoftware.ftp4j.FTPListParser;
import it.sauronsoftware.ftp4j.FTPTextualExtensionRecognizer;
import it.sauronsoftware.ftp4j.connectors.DirectConnector;
import it.sauronsoftware.ftp4j.extrecognizers.DefaultTextualExtensionRecognizer;
import it.sauronsoftware.ftp4j.extrecognizers.ParametricTextualExtensionRecognizer;

import java.io.File;

import javax.net.ssl.SSLSocketFactory;

import com.tealium.net.FileDistributor;
import com.tealium.net.exception.FileDistributorException;

public interface FTPFileDistributor extends  FileDistributor {
	enum CDNSource {
		Akamai("akamai"), LimeLight("limelight"), EdgeCache("edgecache");

		private String cdnName;

		public String getName() {
			return cdnName;
		}

		private CDNSource(String cdnName) {
			this.cdnName = cdnName;
		}
	}

	public static final String ALIVE_AUTO_PING = "alive.auto.ping";
	public static final String FTP_USER_NAME = "ftp_user_name";
	public static final String FTP_PASSWORD = "ftp_password";
	public static final String FTP_HOST = "ftphost";

	public static final String FTP_HOST_AKAMAI = "akamai.ftphost";
	public static final String FTP_HOST_EDGECACHE = "edgecache.ftphost";
	public static final String FTP_HOST_LIMELIGHT = "limelight.ftphost";
	public static final String FTP_HOST_CDNETWORKS = "limelight.ftphost";

	public static final String ALIVE_AUTO_PING_AKAMAI = "akamai.alive.auto.ping";
	public static final String FTP_USER_NAME_AKAMAI = "akamai.ftp_user_name";
	public static final String FTP_PASSWORD_AKAMAI = "akamai.ftp_password";
	
	public static final String ALIVE_AUTO_PING_CDNETWORKS = "cdnetworks.alive.auto.ping";
	public static final String FTP_USER_NAME_CDNETWORKS = "cdnetworks.ftp_user_name";
	public static final String FTP_PASSWORD_CDNETWORKS = "cdnetworks.ftp_password";

	public static final String ALIVE_AUTO_PING_EDGECACHE = "edgecache.alive.auto.ping";
	public static final String FTP_USER_NAME_EDGECACHE = "edgecache.ftp_user_name";
	public static final String FTP_PASSWORD_EDGECACHE = "edgecache.ftp_password";

	public static final String ALIVE_AUTO_PING_LIMELIGHT = "limelight.alive.auto.ping";
	public static final String FTP_USER_NAME_LIMELIGHT = "limelight.ftp_user_name";
	public static final String FTP_PASSWORD_LIMELIGHT = "limelight.ftp_password";

	/**
	 * The constant for the FTP security level.
	 * 
	 * 
	 */
	public static final int SECURITY_FTP = 0;
	/**
	 * The constant for the FTPS (FTP over implicit TLS/SSL) security level.
	 * 
	 * 
	 */
	public static final int SECURITY_FTPS = 1;
	/**
	 * The constant for the FTPES (FTP over explicit TLS/SSL) security level.
	 * 
	 * 
	 */
	public static final int SECURITY_FTPES = 2;

	/**
	 * The constant for the MLSD policy that causes the client to use the MLSD
	 * command instead of LIST, but only if the MLSD command is explicitly
	 * supported by the server (the support is tested with the FEAT command).
	 * 
	 * 
	 */
	public static final int MLSD_IF_SUPPORTED = 0;
	/**
	 * The constant for the MLSD policy that causes the client to use always the
	 * MLSD command instead of LIST, also if the MLSD command is not explicitly
	 * supported by the server (the support is tested with the FEAT command).
	 * 
	 * 
	 */
	public static final int MLSD_ALWAYS = 1;
	/**
	 * The constant for the MLSD policy that causes the client to use always the
	 * LIST command, also if the MLSD command is explicitly supported by the
	 * server (the support is tested with the FEAT command).
	 * 
	 * 
	 */
	public static final int MLSD_NEVER = 2;

	/**
	 * This method returns the connector used to connect the remote host.
	 * 
	 * @return The connector used to connect the remote host.
	 */
	public FTPConnector getConnector();

	/**
	 * This method sets the connector used to connect the remote host.
	 * 
	 * Default one is a
	 * it.sauronsoftware.ftp4j.connectors.direct.DirectConnector instance.
	 * 
	 * @param connector
	 *            The connector used to connect the remote host.
	 * @see DirectConnector
	 */
	public void setConnector(FTPConnector connector);

	/**
	 * Sets the SSL socket factory used to negotiate SSL connections.
	 * 
	 * @param sslSocketFactory
	 *            The SSL socket factory used to negotiate SSL connections.
	 * 
	 * 
	 */
	public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory);

	/**
	 * Returns the SSL socket factory used to negotiate SSL connections.
	 * 
	 * @return The SSL socket factory used to negotiate SSL connections.
	 * 
	 * 
	 */
	public SSLSocketFactory getSSLSocketFactory();

	/**
	 * Sets the security level for the connection. This method should be called
	 * before starting a connection with a server. The security level must be
	 * expressed using one of the SECURITY_FTP, SECURITY_FTPS and SECURITY_FTPES
	 * costants.
	 * 
	 * SECURITY_FTP, which is the default value, applies the basic FTP security
	 * level.
	 * 
	 * SECURITY_FTPS applies the FTPS security level, which is FTP over implicit
	 * TLS/SSL.
	 * 
	 * SECURITY_FTPES applies the FTPES security level, which is FTP over
	 * explicit TLS/SSL.
	 * 
	 * @param security
	 *            The security level.
	 * @throws IllegalStateException
	 *             If the client is already connected to a server.
	 * @throws IllegalArgumentException
	 *             If the supplied security level is not valid.
	 * 
	 */
	public void setSecurity(int security) throws FileDistributorException;

	/**
	 * Returns the security level used by the client in the connection.
	 * 
	 * @return The security level, which could be one of the SECURITY_FTP,
	 *         SECURITY_FTPS and SECURITY_FTPES costants.
	 * 
	 * 
	 */
	public int getSecurity();

	/**
	 * This method enables/disables the use of the passive mode.
	 * 
	 * @param passive
	 *            If true the passive mode is enabled.
	 */
	public void setPassive(boolean passive);

	/**
	 * This methods sets how to treat the contents during a file transfer.
	 * 
	 * The type supplied should be one of TYPE_AUTO, TYPE_TEXTUAL or TYPE_BINARY
	 * constants. Default value is TYPE_AUTO.
	 * 
	 * {@link FTPClient#TYPE_TEXTUAL} means that the data sent or received is
	 * treated as textual information. This implies charset conversion during
	 * the transfer.
	 * 
	 * {@link FTPClient#TYPE_BINARY} means that the data sent or received is
	 * treated as a binary stream. The data is taken "as is", without any
	 * charset conversion.
	 * 
	 * {@link FTPClient#TYPE_AUTO} lets the client pick between textual and
	 * binary types, depending on the extension of the file exchanged, using a
	 * FTPTextualExtensionRecognizer instance, which could be set through the
	 * setTextualExtensionRecognizer method. The default recognizer is an
	 * instance of {@link DefaultTextualExtensionRecognizer}.
	 * 
	 * @param type
	 *            The type.
	 * @throws IllegalArgumentException
	 *             If the supplied type is not valid.
	 * @see FTPClient#setTextualExtensionRecognizer(FTPTextualExtensionRecognizer)
	 * @see DefaultTextualExtensionRecognizer
	 */
	public void setType(int type) throws IllegalArgumentException;

	/**
	 * This method returns the value suggesting how the client encode and decode
	 * the contents during a data transfer.
	 * 
	 * @return The type as a numeric value. The value could be compared to the
	 *         constants {@link FTPClient#TYPE_AUTO},
	 *         {@link FTPClient#TYPE_BINARY} and {@link FTPClient#TYPE_TEXTUAL}.
	 */
	public int getType();

	/**
	 * This method lets the user control how the client chooses whether to use
	 * or not the MLSD command (RFC 3659) instead of the base LIST command.
	 * 
	 * The type supplied should be one of MLSD_IF_SUPPORTED, MLSD_ALWAYS or
	 * MLSD_NEVER constants. Default value is MLSD_IF_SUPPORTED.
	 * 
	 * {@link FTPClient#MLSD_IF_SUPPORTED} means that the client should use the
	 * MLSD command only if it is explicitly supported by the server.
	 * 
	 * {@link FTPClient#MLSD_ALWAYS} means that the client should use always the
	 * MLSD command, also if the MLSD command is not explicitly supported by the
	 * server
	 * 
	 * {@link FTPClient#MLSD_NEVER} means that the client should use always only
	 * the LIST command, also if the MLSD command is explicitly supported by the
	 * server.
	 * 
	 * The support for the MLSD command is tested by the client after the
	 * connection to the remote server, with the FEAT command.
	 * 
	 * @param mlsdPolicy
	 *            The MLSD policy.
	 * @throws IllegalArgumentException
	 *             If the supplied MLSD policy value is not valid.
	 * 
	 */
	public void setMLSDPolicy(int mlsdPolicy) throws IllegalArgumentException;

	/**
	 * This method returns the value suggesting how the client chooses whether
	 * to use or not the MLSD command (RFC 3659) instead of the base LIST
	 * command.
	 * 
	 * @return The MLSD policy as a numeric value. The value could be compared
	 *         to the constants {@link FTPClient#MLSD_IF_SUPPORTED},
	 *         {@link FTPClient#MLSD_ALWAYS} and {@link FTPClient#MLSD_NEVER}.
	 * 
	 */
	public int getMLSDPolicy();

	/**
	 * Returns the name of the charset used to establish textual communications.
	 * If not null the client will use always the given charset. If null the
	 * client tries to auto-detect the server charset. If this attempt fails the
	 * client will use the machine current charset.
	 * 
	 * @return The name of the charset used to establish textual communications.
	 * @since 1.1
	 */
	public String getCharset();

	/**
	 * Sets the name of the charset used to establish textual communications. If
	 * not null the client will use always the given charset. If null the client
	 * tries to auto-detect the server charset. If this attempt fails the client
	 * will use the machine current charset.
	 * 
	 * @param charset
	 *            The name of the charset used to establish textual
	 *            communications.
	 * @since 1.1
	 */
	public void setCharset(String charset);

	/**
	 * Checks whether the connected remote FTP server supports compressed data
	 * transfers (uploads, downloads, list operations etc.). If so, the
	 * compression of any subsequent data transfer (upload, download, list etc.)
	 * can be compressed, saving bandwidth. To enable compression call
	 * {@link FTPClient#setCompressionEnabled(boolean)} .
	 * 
	 * The returned value is not significant if the client is not connected and
	 * authenticated.
	 * 
	 * @return <em>true</em> if compression of data transfers is supported on
	 *         the server-side, <em>false</em> otherwise.
	 * @see FTPClient#isCompressionEnabled()
	 * 
	 */
	public boolean isCompressionSupported();

	/**
	 * Enables or disables the use of compression during any subsequent data
	 * transfer. Compression is enabled when both the supplied value and the
	 * {@link FTPClient#isCompressionSupported()}) returned value are
	 * <em>true</em>.
	 * 
	 * The default value is <em>false</em>.
	 * 
	 * @param compressionEnabled
	 *            <em>true</em> to enable the use of compression during any
	 *            subsequent file transfer, <em>false</em> to disable the
	 *            feature.
	 * @see FTPClient#isCompressionSupported()
	 * 
	 */
	public void setCompressionEnabled(boolean compressionEnabled);

	/**
	 * Checks whether the use of compression is enabled on the client-side.
	 * 
	 * Please note that compressed transfers are actually enabled only if both
	 * this method and {@link FTPClient#isCompressionSupported()} return
	 * <em>true</em>.
	 * 
	 * @return <em>true</em> if compression is enabled, <em>false</em>
	 *         otherwise.
	 * @see FTPClient#isCompressionSupported()
	 * 
	 */
	public boolean isCompressionEnabled();

	/**
	 * This method returns the textual extension recognizer used by the client.
	 * 
	 * Default one is {@link DefaultTextualExtensionRecognizer}.
	 * 
	 * @return The textual extension recognizer used by the client.
	 * @see DefaultTextualExtensionRecognizer
	 */
	public FTPTextualExtensionRecognizer getTextualExtensionRecognizer();

	/**
	 * This method sets the textual extension recognizer used by the client.
	 * 
	 * The default one is {@link DefaultTextualExtensionRecognizer}.
	 * 
	 * You can plug your own by implementing the
	 * {@link FTPTextualExtensionRecognizer} interface. For your convenience the
	 * ftp4j gives you another FTPTextualExtensionRecognizer implementation,
	 * which is {@link ParametricTextualExtensionRecognizer}.
	 * 
	 * @param textualExtensionRecognizer
	 *            The textual extension recognizer used by the client.
	 * @see DefaultTextualExtensionRecognizer
	 * @see ParametricTextualExtensionRecognizer
	 */
	public void setTextualExtensionRecognizer(FTPTextualExtensionRecognizer textualExtensionRecognizer);

	/**
	 * This method tests if this client works in passive FTP mode.
	 * 
	 * @return true if this client is configured to work in passive FTP mode.
	 */
	public boolean isPassive();

	/**
	 * Enable and disable the auto-noop feature.
	 * 
	 * If the supplied value is greater than 0, the auto-noop feature is
	 * enabled, otherwise it is disabled. If positive, the field is used as a
	 * timeout value (expressed in milliseconds). If autoNoopDelay milliseconds
	 * has passed without any communication between the client and the server, a
	 * NOOP command is automaticaly sent to the server by the client.
	 * 
	 * The default value for the auto noop delay is 0 (disabled).
	 * 
	 * @param autoNoopTimeout
	 *            The duration of the auto-noop timeout, in milliseconds. If 0
	 *            or less, the auto-noop feature is disabled.
	 * 
	 * 
	 */
	public void setAutoNoopTimeout(long autoNoopTimeout);

	/**
	 * Returns the duration of the auto-noop timeout, in milliseconds. If 0 or
	 * less, the auto-noop feature is disabled.
	 * 
	 * @return The duration of the auto-noop timeout, in milliseconds. If 0 or
	 *         less, the auto-noop feature is disabled.
	 * 
	 * 
	 */
	public long getAutoNoopTimeout();

	/**
	 * This method adds a {@link FTPListParser} to the object.
	 * 
	 * @param listParser
	 *            The list parser.
	 */
	public void addListParser(FTPListParser listParser);

	/**
	 * This method removes a {@link FTPListParser} previously added to the
	 * object.
	 * 
	 * @param listParser
	 *            The list parser to be removed.
	 */
	public void removeListParser(FTPListParser listParser);

	/**
	 * This method returns a list with all the {@link FTPListParser} used by the
	 * client.
	 * 
	 * @return A list with all the FTPListParsers used by the client.
	 */
	public FTPListParser[] getListParsers();

	/**
	 * This method performs a "noop" operation with the server.
	 * 
	 * @throws FileDistributorException
	 */
	public void noop() throws FileDistributorException;

	/**
	 * This method sends a custom command to the server. Don't use this method
	 * to send standard commands already supported by the client: this should
	 * cause unexpected results.
	 * 
	 * @param command
	 *            The command line.
	 * @return The reply supplied by the server, parsed and served in an object
	 *         way mode.
	 * @throws FileDistributorException
	 */
	public TealiumFTPReply sendCustomCommand(String command) throws FileDistributorException;

	/**
	 * This method sends a SITE specific command to the server.
	 * 
	 * @param command
	 *            The site command.
	 * @return The reply supplied by the server, parsed and served in an object
	 *         way mode.
	 * @throws FileDistributorException
	 */
	public TealiumFTPReply sendSiteCommand(String command) throws FileDistributorException;

	/**
	 * This method calls the HELP command on the remote server, returning a list
	 * of lines with the help contents.
	 * 
	 * @return The help contents, splitted by line.
	 * @throws FileDistributorException
	 */
	public String[] help() throws FileDistributorException;

	public void download(String remoteFileName, File localFile) throws FileDistributorException;
	
	
	/**
	 * If the client is connected, it reports the remote host name or address.
	 * 
	 * @return The remote host name or address.
	 */
	public  String getHost();

	/**
	 * If the client is connected, it reports the remote port number.
	 * 
	 * @return The remote port number.
	 */
	public  int getPort();

	/**
	 * If the client is authenticated, it reports the authentication password.
	 * 
	 * @return The authentication password.
	 */
	public  String getPassword();

	/**
	 * If the client is authenticated, it reports the authentication username.
	 * 
	 * @return The authentication username.
	 */
	public  String getUsername();
	

	/**
	 * This method connects the client to the remote FTP host, using the default
	 * port value 21 (990 if security level is set to FTPS, see
	 * {@link FTPClient#setSecurity(int)}).
	 * 
	 * @param host
	 *            The hostname of the destination.
	 * @return The destination welcome message, one line per array element.
	 * @throws FileDistributorException
	 */
	public  String[] connect(String host) throws FileDistributorException;
	


	/**
	 * This method connects the client to the remote FTP host.
	 * 
	 * @param host
	 *            The host name or address of the destination.
	 * @param port
	 *            The port listened by the destination.
	 * @return The destination welcome message, one line per array element.
	 * @throws FileDistributorException
	 */
	public  String[] connect(String host, int port) throws FileDistributorException;


	/**
	 * This method disconnects from the destination, optionally performing the
	 * QUIT procedure.
	 * 
	 * @param sendQuitCommand
	 *            If true the QUIT procedure with the destination will be performed,
	 *            otherwise the connection is abruptly closed by the client
	 *            without sending any advice to the destination.
	 * @throws FileDistributorException
	 */
	public  void disconnect(boolean sendQuitCommand) throws FileDistributorException;
	


	/**
	 * This method performs a logout operation for the current user, leaving the
	 * connection open, thus it can be used to start a new user session. 
	 * 
	 * @throws FileDistributorException
	 */
	public  void logout() throws FileDistributorException;
	


	/**
	 * Call this method to switch the user current account. Be careful with
	 * this: some FTP destinations don't implement this feature, even though it is a
	 * standard FTP one.
	 * 
	 * @param account
	 *            The account.
	 * @throws FileDistributorException
	 */
	public  void changeAccount(String account) throws FileDistributorException;
	

	/**
	 * This method returns the destination status
	 * 
	 * @return The destination status, splitted by line.
	 * @throws FileDistributorException
	 */
	public  String[] destinationStatus() throws FileDistributorException;
}