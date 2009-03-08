package com.newsarea.areca.plugin.as3.gui;

import com.application.areca.impl.policy.FileSystemPolicy;
import com.application.areca.launcher.gui.TargetEditionWindow;
import com.application.areca.plugins.StorageSelectionHelper;
import com.newsarea.areca.plugin.as3.AS3FileSystemPolicy;

public class AS3StorageSelectionHelper implements StorageSelectionHelper {

    //private TargetEditionWindow window;
    
    public FileSystemPolicy handleConfiguration() {
    	return new AS3FileSystemPolicy();
    }

    public void handleSelection() {
    }

    public void setWindow(TargetEditionWindow window) {
        //this.window = window;
    }
    
}
