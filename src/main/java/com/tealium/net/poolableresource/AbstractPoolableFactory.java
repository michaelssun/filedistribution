package com.tealium.net.poolableresource;

import org.apache.commons.pool2.BasePooledObjectFactory;

public abstract class AbstractPoolableFactory<T> extends BasePooledObjectFactory<T> {
	public abstract T create() throws Exception;
}
