package com.newsarea.areca.plugin.as3.nunit;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.jets3t.service.security.AWSCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.myJava.file.driver.AbstractFileSystemDriver;
import com.myJava.file.driver.DefaultFileSystemDriver;
import com.newsarea.areca.plugin.as3.AS3FileSystemDriver;


public class FileSystemDriverTestCase {
	
	public static final String DIRECTORY_TEMP = "C:\\as3\\test";
	public static final String DIRECTORY_TEMP_SUB = DIRECTORY_TEMP + "\\sub";
	public static final String DIRECTORY_TEMP_FILE = DIRECTORY_TEMP + "\\testfile.tmp";
	
	AbstractFileSystemDriver _driver = null;
	
	public AbstractFileSystemDriver getDriver() {
		if(this._driver == null) {
			this._driver = new DefaultFileSystemDriver();
		}		
		return this._driver;
	}
	
	@Before
	public void before() { }
	
	@After
	public void after() { }
		
//	@Test
//	public void list() {
//		File directory = new File(DIRECTORY_TEMP + "\\" + (new Random()).nextInt() * Integer.MAX_VALUE);
//		String[] listResult = this.getDriver().list(directory);
//		assertNull(listResult);	
//		//
//		directory = new File(DIRECTORY_TEMP);
//		listResult = this.getDriver().list(directory);
//		assertNotNull(listResult);
//		assertTrue(listResult.length == 0);
//	}
	
	@Test
	public void delete() throws IOException {
		File directory = new File(DIRECTORY_TEMP + "\\" + (new Random()).nextInt() * Integer.MAX_VALUE);
		assertTrue(this.getDriver().delete(directory));
		// check folder
		String[] listResult = this.getDriver().list(new File(DIRECTORY_TEMP));
		if(listResult == null) {
			assertTrue(this.getDriver().mkdir(new File(DIRECTORY_TEMP)));
		}
		//	delete folder
		assertTrue("Ordner " + DIRECTORY_TEMP + " konnte nicht gelöscht werde", this.getDriver().delete(new File(DIRECTORY_TEMP)));
		String[] deleteResult = this.getDriver().list(new File(DIRECTORY_TEMP));
		assertNull("Ordner " + DIRECTORY_TEMP + " wurde nicht gelöscht.", deleteResult);
		//
		// create folder and sub folders
		assertTrue(this.getDriver().mkdir(new File(DIRECTORY_TEMP)));
		assertTrue(this.getDriver().mkdir(new File(DIRECTORY_TEMP_SUB)));
		// try to delete
		assertFalse(this.getDriver().delete(new File(DIRECTORY_TEMP)));
		assertTrue(this.getDriver().delete(new File(DIRECTORY_TEMP_SUB)));
		assertTrue(this.getDriver().delete(new File(DIRECTORY_TEMP)));
		//
		// create folder and file
		assertTrue(this.getDriver().mkdir(new File(DIRECTORY_TEMP)));
		assertTrue(this.getDriver().createNewFile(new File(DIRECTORY_TEMP + "\\testfile.tmp")));
		String[] listResult2 = this.getDriver().list(new File(DIRECTORY_TEMP));
		//check file
		assertEquals("testfile.tmp;", this.arrayToString(listResult2));	
		//
		assertFalse(this.getDriver().delete(new File(DIRECTORY_TEMP)));
		assertTrue(this.getDriver().delete(new File(DIRECTORY_TEMP_FILE)));
		assertTrue(this.getDriver().delete(new File(DIRECTORY_TEMP)));
		//
		assertNull("Ordner " + DIRECTORY_TEMP + " wurde nicht gelöscht", deleteResult);
	}	
	
	private String arrayToString(String[] array) {
		StringBuilder strBld = new StringBuilder();
		for(String item : array) {
			strBld.append(item);
			strBld.append(";");
		}
		return strBld.toString();
	}
	
	private String arrayToString(File[] files) {
		StringBuilder strBld = new StringBuilder();
		for(File file : files) {
			strBld.append(file.getAbsolutePath());
			strBld.append(";");
		}
		return strBld.toString();
	}

}
