package com.newsarea.areca.plugin.as3;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.myJava.file.EventOutputStream;
import com.myJava.file.OutputStreamListener;
import com.myJava.file.driver.AbstractFileSystemDriver;
import com.myJava.file.driver.FileCacheableInformations;
import com.myJava.object.ToStringHelper;
import com.myJava.util.log.Logger;

public class AS3FileSystemDriver extends AbstractFileSystemDriver {

	private AWSCredentials _credentials = null;
	private S3Service _s3Service = null;
	private S3Bucket _bucket = null;	
	private HashMap<String, AS3File> _cachedFiles = new HashMap<String, AS3File>();
	private String _directory = null;
	private String _prefix = null;
	
	public AS3FileSystemDriver(AWSCredentials credentials, String bucket, String directory, String prefix) {
		this._credentials = credentials;		
		this._directory = directory;
		this._prefix = prefix;
		//
		try {
			this._s3Service = new RestS3Service(this._credentials);
			this._bucket = this._s3Service.getBucket(bucket);
		} catch (S3ServiceException e) {
			e.printStackTrace();
		}
	}

	private S3Service getS3Service() {
		return this._s3Service;
	}
	
	private S3Bucket getBucket() {
		return this._bucket;
	}
	
	public boolean canRead(File file) {
        throw new UnsupportedOperationException("This method is not supported by this implementation");
    }

    public boolean canWrite(File file) {
        throw new UnsupportedOperationException("This method is not supported by this implementation");
    }

    public boolean supportsLongFileNames() {
        return true;
    }

    public File getAbsoluteFile(File file) {
    	//Logger.defaultLogger().info("getAbsoluteFile - " + file.getAbsoluteFile());
        return file.getAbsoluteFile();
    }

    public String getAbsolutePath(File file) {
    	//Logger.defaultLogger().info("getAbsolutePath - " + file.getAbsolutePath());
        return normalizeIfNeeded(file.getAbsolutePath());
    }

    public File getCanonicalFile(File file) throws IOException {
    	//Logger.defaultLogger().info("getCanonicalFile - " + file.getCanonicalFile());
        return file.getCanonicalFile();
    }

    public String getCanonicalPath(File file) throws IOException {
    	//Logger.defaultLogger().info("getCanonicalPath - " + file.getCanonicalPath());
        return normalizeIfNeeded(file.getCanonicalPath());
    }

    public String getName(File file) {
    	//Logger.defaultLogger().info("getName - " + file.getName());
        return file.getName();
    }

    public String getParent(File file) {
    	//Logger.defaultLogger().info("getParent - " + file.getParent());
        return normalizeIfNeeded(file.getParent());
    }

    public File getParentFile(File file) {
    	//Logger.defaultLogger().info("getParentFile - " + file.getParentFile());
        return file.getParentFile();
    }

    public String getPath(File file) {
    	//Logger.defaultLogger().info("getPath - " + normalizeIfNeeded(file.getPath()));
    	return normalizeIfNeeded(file.getPath());
    }

    public boolean isAbsolute(File file) {
    	//Logger.defaultLogger().info("isAbsolute - " + file.isAbsolute());
        return file.isAbsolute();
    }

    public boolean isFile(File file) {
    	//Logger.defaultLogger().info("isFile - " + file.getParent());
        return (! isDirectory(file));
    }
    
    public String[] list(File file, FilenameFilter filter) {
        File[] files = this.listFiles(file, filter);
        if(files == null) { return null; }
        //
        String[] ret = new String[files.length];
        for (int i=0; i < files.length; i++) {        	
            ret[i] = files[i].getName();
        }
        //
        return ret;
    }

    public String[] list(File file) {
        File[] files = this.listFiles(file);
        if(files == null) { return null; }
        //
        String[] ret = new String[files.length];
        for (int i=0; i < files.length; i++) {        	
            ret[i] = normalizeIfNeeded(files[i].getName());
        }
        //
        return ret;
    }      

    public File[] listFiles(File file, FileFilter filter) {
        File[] unfiltered = this.listFiles(file);
        if(unfiltered == null) { return null; }
        //
        ArrayList<File> retList = new ArrayList<File>();
        for (int i = 0; i < unfiltered.length; i++) {
            if (filter.accept(unfiltered[i])) {
                retList.add(unfiltered[i]);
            }
        }
        //
        return (File[])retList.toArray(new File[0]);
    }

    public File[] listFiles(File file, FilenameFilter filter) {
        File[] unfiltered = this.listFiles(file);
        if(unfiltered == null) { return null; }
        //
        ArrayList<File> retList = new ArrayList<File>();
        for (int i = 0; i < unfiltered.length; i++) {
            if (filter.accept(unfiltered[i].getParentFile(), unfiltered[i].getName())) {
                retList.add(unfiltered[i]);
            }
        }
        //
        return (File[])retList.toArray(new File[0]);
    }

    public File[] listFiles(File file) {
    	try {
			this.mount();
		} catch (Exception ex) {
			Logger.defaultLogger().error(ex);
		}
    	//
    	ArrayList<File> result = new ArrayList<File>();
    	try { 
    		String path = this.getAS3Key(file, true);
    		if(!this._cachedFiles.containsKey(path)) {
    			return null;
    		}
    		//
        	for(AS3File as3file : this._cachedFiles.values()) {
        		String cname = as3file.getAS3Key();
        		// remove path
        		String name = cname.replace(path, "");
        		if(name.equals(cname)) { continue; }
        		if(name.equals("")) { continue; }
        		//
        		int slashIdx = name.indexOf("/");        		
        		if(slashIdx > -1 && slashIdx != name.length() - 1) { continue; }
        		//
        		result.add(as3file);
        	}
        } catch (Exception e) {
            Logger.defaultLogger().error(e);
        } finally { }
        //
        return (File[])result.toArray(new File[] { });
    }
    
    public boolean createNewFile(File file) throws IOException {
    	Logger.defaultLogger().info("createNewFile " + file.getAbsolutePath());
    	//
    	try {
			this.getS3Service().putObject(this.getBucket(), new S3Object(this.getAS3Key(file)));
			this.mount();
			return true;
		} catch (Exception ex) {
			Logger.defaultLogger().error(ex);
		}
    	return false;
    }

    public long length(File file) {
    	return this.getAS3File(file).length();
    }    

    public long lastModified(File file) {
    	return this.getAS3File(file).lastModified();
    }

    public boolean isDirectory(File file) {
    	return this.getAS3File(file).isDirectory();
    }

    public boolean exists(File file) {
    	return this.getAS3File(file).exists();
    }
    
    public boolean delete(File infile) {
    	AS3File file = this.getAS3File(infile);
    	//
    	if(file.isDirectory()) {
    		File[] files = this.listFiles(file);
    		if(files == null) { return true; }
    		// if folder is not empty
    		if(files.length > 0) { return false; }
    	}
		//
    	try {    		
    		Logger.defaultLogger().info("delete - " + this.getBucket().getName() + "/" + file.getAS3Key());
			this.getS3Service().deleteObject(this.getBucket(), file.getAS3Key());
			AS3File rFile = this._cachedFiles.remove(file.getAS3Key());
			return true;
		} catch (Exception ex) {
			Logger.defaultLogger().error(ex);
		}
    	return false;
    }

    public boolean mkdir(File file) {
    	Logger.defaultLogger().info("mkdir " + file.getAbsolutePath());
    	//
    	try {
			this.getS3Service().putObject(this.getBucket(), new S3Object(this.getAS3Key(file, true)));
			this.mount();
			return true;
		} catch (Exception ex) {
			Logger.defaultLogger().error(ex);
		}
    	return false;
    }

    public boolean renameTo(File source, File dest) {
    	try {
    		this.getS3Service().renameObject(this.getBucket().getName(), this.getAS3Key(source), new S3Object(this.getAS3Key(dest)));
    		this.mount();
    		return true;
    	} catch (Exception ex) { }
    	return false;
    }

    public InputStream getFileInputStream(File file) throws IOException {    	
    	Logger.defaultLogger().info("getFileInputStream - " + file.getAbsolutePath());
		try {
			S3Object obj = this.getS3Service().getObject(this.getBucket(), this.getAS3Key(file));
			return obj.getDataInputStream();
		} catch (S3ServiceException ex) {
			Logger.defaultLogger().error(ex);
		}
    	throw new UnsupportedOperationException("This method is not supported by this implementation");
    }

    public synchronized OutputStream getCachedFileOutputStream(File file) throws IOException {
    	return this.getFileOutputStream(file);
    }

    public InputStream getCachedFileInputStream(File file) throws IOException {
    	return this.getFileInputStream(file);
    }

    public OutputStream getFileOutputStream(File file, boolean append) throws IOException {
    	Logger.defaultLogger().info("getFileOutputStream - " + file.getAbsolutePath() + " - " + append + " " + file.length());
    	return new AS3OutputStream(this.getS3Service(), this.getBucket(), this.getAS3Key(file));
    }
    
    public OutputStream getFileOutputStream(File file, boolean append, OutputStreamListener listener) throws IOException {
    	OutputStream out = getFileOutputStream(file, append);
    	return listener == null ? out : new EventOutputStream(out, listener);
	}

    public OutputStream getFileOutputStream(File file) throws IOException {    	
        checkFilePath(file);
        return getFileOutputStream(file, false);
    }

    public boolean directFileAccessSupported() {
        return false;
    }
    
    public void mount() throws IOException {    	
    	//Logger.defaultLogger().info("mount data");
    	//
    	this._cachedFiles.clear();
    	// check prefix
//    	try {
//			this.getS3Service().getObject(this.getBucket(), this._prefix + "/");
//		} catch (S3ServiceException ex) {
//			try {
//				this.getS3Service().putObject(this.getBucket(), new S3Object(this._prefix + "/"));
//			} catch (S3ServiceException e) { }
//		}
    	//
		try {
			S3Bucket bucket = this.getBucket();
			if(bucket == null) { return; }
			//
			S3Object[] s3Objs = this.getS3Service().listObjects(bucket, this._prefix, null);
			for(S3Object obj : s3Objs) {				
				boolean isDirectory = obj.getKey().lastIndexOf("/") == (obj.getKey().length() - 1);
				AS3File file = new AS3File(
						this._directory + "/" + obj.getKey(),
						obj.getKey(),
						obj.getContentLength(), 
						isDirectory, 
						true,
						obj.getLastModifiedDate().getTime());
				//
				this._cachedFiles.put(obj.getKey(), file);
			}
		} catch (S3ServiceException ex) {
			Logger.defaultLogger().error(ex);
		}
    }

    public void unmount() throws IOException {
    	this._cachedFiles.clear();
    	//
        this.flush();
    }

    public synchronized void flush() throws IOException {
	   //throw new UnsupportedOperationException("This method is not supported by this implementation");
    }

    public String toString() {
        StringBuffer sb = ToStringHelper.init(this);
        return ToStringHelper.close(sb);
    }

    public short getAccessEfficiency() {
        return ACCESS_EFFICIENCY_POOR;
    }

    public boolean isContentSensitive() {
        return false;
    }

    public FileCacheableInformations getInformations(File file) {
    	file = this.getAS3File(file);
    	Logger.defaultLogger().info("getInformations - " + file.toString());
        return new FileCacheableInformations(
        		file.length(),
                file.lastModified(),
                file.exists(),
                false,
                false,
                file.isDirectory(),
                false
        );
    }
	    
    /* */
    
    private String getAS3Key(File file) {
    	return this.getAS3Key(file, file.isDirectory());    	
    }
        
    private String getAS3Key(File file, boolean isDirectory) {	
    	String path = file.getAbsolutePath();
    	path = path.replace("\\", "/");
    	if(path.indexOf(this._directory) == -1) { return null; }
    	path = path.replace(this._directory, "");    	
    	//
    	if(path.length() > 1) {
    		path = path.substring(1, path.length());
    	}
    	//
    	if(isDirectory) { path = path + "/"; }
    	//
    	return path;
    }
        
    private AS3File getAS3File(File file) {
    	if(file instanceof AS3File) {
    		return (AS3File)file; 
    	}
    	//
    	String as3Key = this.getAS3Key(file);
    	if(this._cachedFiles.containsKey(as3Key)) {
    		return this._cachedFiles.get(as3Key);
    	}
    	//
    	boolean isDirectory = file.isDirectory();
    	//
    	S3Object obj = this.getS3Object(as3Key);
    	if(obj == null) {
    		obj = this.getS3Object(as3Key + "/");
    		if(obj != null) {
    			as3Key += "/";
    			isDirectory = true;
    		}
    	}
    	//
    	long length = 0;
    	long ticks = 0;
    	if(obj != null) {
    		length = obj.getContentLength();
    		ticks = obj.getLastModifiedDate().getTime();
    	}	
    	//
    	return new AS3File(file.getAbsolutePath(), as3Key, length, isDirectory, obj != null || as3Key.equals("/"), ticks);
    }
        
    private S3Object getS3Object(String key) {
    	S3Bucket bucket = this.getBucket();
    	if(bucket == null) { return null; }
    	//
    	S3Object s3obj = null;
    	try {
			s3obj = this.getS3Service().getObject(bucket, key);
		} catch (S3ServiceException e) {
			//Logger.defaultLogger().error(e);
		}
		//
		return s3obj;
    }

    
}
