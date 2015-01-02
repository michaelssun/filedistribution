package com.tealium.net.poolableresource;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tealium.net.FileDistributor;
import com.tealium.net.ftp.FTPFileDistributor;

public final class PoolableFileDistributorClientFactory extends AbstractPoolableFactory<FileDistributor> {

	@Inject
	private Provider<FTPFileDistributor> provider;

	public PoolableFileDistributorClientFactory() {
	}

	@Override
	public FileDistributor create() throws Exception {

		return provider.get();
	}

	@Override
	public PooledObject<FileDistributor> wrap(FileDistributor obj) {
		return new DefaultPooledObject<FileDistributor>(obj);
	}

	@Override
	public void destroyObject(PooledObject<FileDistributor> p) throws Exception {
		if (p.getObject().getClass().isAssignableFrom(FTPFileDistributor.class)) {
			((FTPFileDistributor) p.getObject()).disconnect(Boolean.FALSE);
		}
		super.destroyObject(p);
	}

}
