package com.newsarea.areca.plugin.as3;

import com.application.areca.plugins.AbstractStoragePlugin;
import com.application.areca.plugins.FileSystemPolicyXMLHandler;
import com.application.areca.plugins.StoragePlugin;
import com.application.areca.plugins.StorageSelectionHelper;
import com.application.areca.version.VersionInfos;
import com.myJava.util.version.VersionData;
import com.newsarea.areca.plugin.as3.gui.AS3StorageSelectionHelper;

public class AS3StoragePlugin extends AbstractStoragePlugin implements StoragePlugin {

	public static final String PLG_DISPLAY = "AS3";
    public static final String PLG_NAME = "Amazon S3";
	
	public AS3StoragePlugin() {
		super();
	}
	
	public String getFullName() {
		return PLG_NAME;
	}

    public String getDisplayName() {
        return PLG_DISPLAY;
    }
	
	public FileSystemPolicyXMLHandler buildFileSystemPolicyXMLHandler() {
		return new AS3FileSystemPolicyXMLHandler();
	}
	
	public boolean storageSelectionHelperProvided() {
        return false;
    }

	public StorageSelectionHelper getStorageSelectionHelper() {
		return new AS3StorageSelectionHelper();
	}

	public VersionData getVersionData() {
		return VersionInfos.getLastVersion();
	}

}
