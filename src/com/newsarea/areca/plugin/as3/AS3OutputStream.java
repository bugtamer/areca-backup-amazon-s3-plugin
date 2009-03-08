package com.newsarea.areca.plugin.as3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.utils.Mimetypes;

import com.myJava.system.OSTool;
import com.myJava.util.Util;
import com.myJava.util.log.Logger;

public class AS3OutputStream extends OutputStream {

	private S3Service _s3Service = null;
	private S3Bucket _bucket = null;
	private String _name = null;
	private File _file = null;
	private FileOutputStream _out = null;
	
	public AS3OutputStream(S3Service s3Service, S3Bucket bucket, String name) throws FileNotFoundException {
		this._s3Service = s3Service;
		this._bucket = bucket;
		this._name = name;
		//
		this._file = new File(OSTool.getTempDirectory(), "as3tmp_" + Util.getRndLong() + ".tmp");
		this._out = new FileOutputStream(this._file);
	}
	
	public void flush() throws IOException {
		this._out.flush();
    }

    public void write(byte[] b, int off, int len) throws IOException {
    	this._out.write(b, off, len);
    }
    
    public void write(byte[] b) throws IOException {
    	this._out.write(b);
    }
    
    public void write(int b) throws IOException {
    	this._out.write(b);
    }
	
	public void close() throws IOException {
		Logger.defaultLogger().info("close OutputStream - " + this._name);
		//
		try {
			S3Object s3Object = new S3Object(this._name);			
			s3Object.setDataInputFile(this._file);
			s3Object.setContentLength(this._file.length());
			s3Object.setContentType(Mimetypes.getInstance().getMimetype(this._file));
			this._s3Service.putObject(_bucket, s3Object);
			//
			this.flush();
			this._out.close();	
			//
			this._file.delete();
		} catch (S3ServiceException ex) {
			Logger.defaultLogger().error(ex);
		}
	}

}
