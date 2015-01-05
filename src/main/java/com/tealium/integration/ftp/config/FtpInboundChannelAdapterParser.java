/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tealium.integration.ftp.config;

import org.springframework.integration.file.config.AbstractRemoteFileInboundChannelAdapterParser;

import com.tealium.integration.ftp.filters.FtpRegexPatternFileListFilter;
import com.tealium.integration.ftp.filters.FtpSimplePatternFileListFilter;
import com.tealium.integration.ftp.inbound.FtpInboundFileSynchronizer;
import com.tealium.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;

/**
 * Parser for the FTP 'inbound-channel-adapter' element.
 *
 * @author Mark Fisher
 * @author Gary Russell
 * @since 2.0
 */
public class FtpInboundChannelAdapterParser extends AbstractRemoteFileInboundChannelAdapterParser {

	@Override
	protected String getMessageSourceClassname() {
		return FtpInboundFileSynchronizingMessageSource.class.getName();
	}

	@Override
	protected String getInboundFileSynchronizerClassname() {
		return FtpInboundFileSynchronizer.class.getName();
	}

	@Override
	protected String getSimplePatternFileListFilterClassname() {
		return FtpSimplePatternFileListFilter.class.getName();
	}

	@Override
	protected String getRegexPatternFileListFilterClassname() {
		return FtpRegexPatternFileListFilter.class.getName();
	}

}
