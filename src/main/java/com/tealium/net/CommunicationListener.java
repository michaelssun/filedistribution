package com.tealium.net;

public interface CommunicationListener {	
	/**
	 * Called every time a telnet statement has been sent over the network to
	 * the remote server.
	 * 
	 * @param statement
	 *            The statement that has been sent.
	 */
	public void sent(String statement);

	/**
	 * Called every time a telnet statement is received by the client.
	 * 
	 * @param statement
	 *            The received statement.
	 */
	public void received(String statement);
}
