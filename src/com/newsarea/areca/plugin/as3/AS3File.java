package com.newsarea.areca.plugin.as3;

import java.io.File;

import com.myJava.object.ToStringHelper;
/**
 * This class extends the default "File" structure.
 * <BR>It represents a fictive local or a remote file.
 * <BR>In both cases, it does not exist on the local file system.
 * <BR>It does not redefine fileSystem actions such as "delete", "mkdir", ... (these actions are the resposibility of the FileSystemDriver)
 * but keep technical informations in cache such as file size, isDirectory, ... (to avoid making multiple ftp accesses for the same file)
 * <BR>
 * @author Olivier PETRUCCI
 * <BR>
 * <BR>Areca Build ID : 8785459451506899793
 */

public class AS3File extends File {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private long _length;
	private boolean _directory;
	private boolean _exists;
	private long ftpTs;
	private String _as3Key;	
	
	/**
	 * Technical attribute : Driver used for ftp accesses 
	 */
    public AS3File(
            String localPath, 
            String as3Key,
            long length, 
            boolean directory,
            boolean exists,
            long ftpTs) {
        
        super(localPath);
    
        this._as3Key = as3Key;
        this._length = length;
        this._directory = directory;
        this._exists = exists;
        this.ftpTs = ftpTs;
    }
    
    public String getLocalPath() {
        return this.getAbsolutePath();
    }
    
    public String getAS3Key() {
        return this._as3Key;
    }
    
	public long length() {
		return this._length;
	}
	
	public boolean isDirectory() {		
		return this._directory;
	}

	public boolean isFile() {
		return ! isDirectory();
	}
	
    public boolean exists() {		
		return this._exists;
    }    
    
    public long lastModified() {	
		return ftpTs;
    }
    
	public void init(long length, boolean directory, boolean exists, long time) {	
		this._length = length;
		this._directory = directory;
		this._exists = exists;
		this.ftpTs = time;
	}
	
	public String toString() {
	    StringBuffer sb = ToStringHelper.init(this);
	    ToStringHelper.append("LOCAL PATH", this.getAbsolutePath(), sb);
	    ToStringHelper.append("REMOTE PATH", this._as3Key, sb);
	    ToStringHelper.append("LENGTH", this._length, sb);
	    ToStringHelper.append("TS", ftpTs, sb);
	    ToStringHelper.append("IS_DIRECTORY", this._directory, sb);
	    ToStringHelper.append("EXISTS", this._exists, sb); 
	    return ToStringHelper.close(sb);
	}
}