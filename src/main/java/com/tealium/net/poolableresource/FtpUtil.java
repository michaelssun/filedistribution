package com.tealium.net.poolableresource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
 



import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPReply;

import com.google.common.collect.Collections2;
import com.tealium.net.FileMetaData;
import com.tealium.net.ftp.TealiumFTPReply;

public class FtpUtil {
	private FtpUtil() {
	}

	public static TealiumFTPReply getTealiumFTPReply(FTPReply ftpReply) {
		TealiumFTPReply tReply = new TealiumFTPReply(ftpReply.getCode(), ftpReply.getMessages());
		tReply.setSuccessCode(ftpReply.isSuccessCode());
		return tReply;
	}
	
	public static FileMetaData getFileMetaData(FTPFile ftpFile){
		FileMetaData fileMetaData=new FileMetaData();
		fileMetaData.setLink(ftpFile.getLink());
		fileMetaData.setModifiedDate( ftpFile.getModifiedDate());
		fileMetaData.setName( ftpFile.getName());
		fileMetaData.setSize( ftpFile.getSize());
		fileMetaData.setType( ftpFile.getType());
		
		return fileMetaData;
	}	
	public static Collection< FileMetaData> getFileMetaDataSet(Collection<FTPFile> ftpFiles){ 
		if (ftpFiles==null) {
			return Collections.EMPTY_LIST;
		}
		Collection< FileMetaData> set=new HashSet<FileMetaData>();
		for (Iterator<FTPFile> iterator = ftpFiles.iterator(); iterator.hasNext();) {
			FileMetaData fileMetaData2 =getFileMetaData(  iterator.next());
			set.add(fileMetaData2);
		}
		
		return set;
	} 
}
