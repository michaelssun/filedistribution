package com.tealium.net.poolableresource;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.google.inject.Inject;
import com.tealium.net.FileDistributor;

public class FileDistributorPool extends GenericObjectPool<FileDistributor> {
	@Inject
	private int maxWaitMills;
	@Inject
	private int maxIdle;
	@Inject
	private int maxTotal;
	

	@Inject
	public FileDistributorPool( final PooledObjectFactory<FileDistributor> factory) {

		super(factory);
		
		this.setConfig(getConfig());
	}

	private GenericObjectPoolConfig getConfig() {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxIdle(maxIdle);
		config.setMaxTotal(maxTotal);
		config.setMaxWaitMillis(maxWaitMills);
		config.setTestOnBorrow(Boolean.TRUE);
		config.setTestOnReturn(Boolean.TRUE);
		return config;
	}
}
