package com.newsarea.areca.plugin.as3;

import java.io.File;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.security.AWSCredentials;

import com.application.areca.ApplicationException;
import com.application.areca.ArchiveMedium;
import com.application.areca.impl.policy.AbstractFileSystemPolicy;
import com.application.areca.impl.policy.FileSystemPolicy;
import com.myJava.file.FileSystemManager;
import com.myJava.file.driver.FileSystemDriver;
import com.myJava.object.Duplicable;
import com.myJava.system.OSTool;

public class AS3FileSystemPolicy extends AbstractFileSystemPolicy implements FileSystemPolicy {
	
	public static String LOCAL_DIR_PREFIX = "C:/as3";
	
	static {
    	String prefix;
        if (OSTool.isSystemWindows()) {
        	prefix = "C:/as3";
        } else {
        	prefix = "/as3";
        }
        
        String tg = prefix;
        int n=0;
        while (FileSystemManager.exists(new File(tg))) {
        	tg = prefix + n++;
        }
        
        LOCAL_DIR_PREFIX = tg;
    }
	
	private String _key = null;
	private String _secret = null;
	private String _bucket = null;
	private String _prefix = null;
	//	
	private ArchiveMedium _medium = null;
	
	public AS3FileSystemPolicy() {
		super();
		super.setId("areca-plugin-as3");
	}
	
	private AWSCredentials getCredentials() {
		return new AWSCredentials(this._key, this._secret);
	}
	
	public String getArchivePath() {
		return LOCAL_DIR_PREFIX + "/" + this._prefix + "/" + this.getUid() + "/";
	}

	public String getDisplayableParameters() {		
		if(this._key == null || this._secret == null || this._bucket == null) {
			return "";		
		}
		//
		StringBuffer sb = new StringBuffer();
		sb.append(this._key);
		sb.append("@");
        sb.append(this._bucket + ":" + this._prefix);
        return sb.toString();
	}

	public FileSystemDriver initFileSystemDriver() throws ApplicationException {
		return new AS3FileSystemDriver(this.getCredentials(), this._bucket, LOCAL_DIR_PREFIX, this._prefix + "/" + this.getUid());
	}
	
	public void setKey(String key) {
		this._key = key;
	}
	
	public String getKey() {
		return this._key;
	}
	
	public void setSecret(String secret) {
		this._secret = secret;
	}
	
	public String getSecret() {
		return this._secret;
	}
	
	public void setPrefix(String prefix) {
		this._prefix = prefix;
	}
	
	public String getPrefix() {
		return this._prefix;
	}
	
	public void setBucket(String bucket) {
		this._bucket = bucket;
	}
	
	public String getBucket() {
		return this._bucket;
	}
	
	public String getUid() {
        return this._medium.getTarget().getUid();
    }

	public void setMedium(ArchiveMedium medium) {
		this._medium = medium;
	}
	
	public ArchiveMedium getMedium() {
        return this._medium;
    }

	public void synchronizeConfiguration() { }

	public void validate(boolean extendedTests) throws ApplicationException {
		try {			
			S3Service s3Service = new RestS3Service(this.getCredentials());
			S3Bucket bucket = s3Service.getBucket(this._bucket);
			if(bucket == null) {
				throw new ApplicationException("invalid bucket name");
			}
		} catch (S3ServiceException s3ex) {
			throw new ApplicationException(s3ex.getMessage());
		} catch (Exception ex) {
			throw new ApplicationException(ex.getMessage());
		}		
	}
	
	public void copyAttributes(AS3FileSystemPolicy policy) {
		super.copyAttributes(policy);
		//
		policy.setKey(this.getKey());
		policy.setSecret(this.getSecret());		
		policy.setBucket(this.getBucket());
		policy.setPrefix(this.getPrefix());
		//
		policy.setMedium(this.getMedium());
    }

	public Duplicable duplicate() {
		AS3FileSystemPolicy policy = new AS3FileSystemPolicy();
		copyAttributes(policy);
		return policy;
	}

}
