package com.newsarea.areca.plugin.as3.gui;

import com.application.areca.AbstractTarget;
import com.application.areca.impl.AbstractIncrementalFileSystemMedium;
import com.application.areca.impl.policy.FileSystemPolicy;
import com.application.areca.launcher.gui.TargetEditionWindow;
import com.application.areca.plugins.StorageSelectionHelper;
import com.newsarea.areca.plugin.as3.AS3FileSystemPolicy;


public class AS3StorageSelectionHelper implements StorageSelectionHelper {

    private TargetEditionWindow window;
    

    public FileSystemPolicy handleConfiguration() {
    	AS3FileSystemPolicy ft = null;
    	//
    	AbstractTarget arTarget = this.window.getTarget();
    	if(arTarget != null) {
    		AbstractIncrementalFileSystemMedium aFSMedium = (AbstractIncrementalFileSystemMedium)arTarget.getMedium();
    		ft = (AS3FileSystemPolicy)aFSMedium.getFileSystemPolicy();
    	}
    	//
    	AS3EditionWindow frm = new AS3EditionWindow(ft);
		frm.setModal(this.window);
		frm.setBlockOnOpen(true);
		frm.open();
		//
		ft = frm.getCurrentPolicy();
		if (ft != null) {
			ft.setId("areca-plugin-as3");
        }
        //
        return ft;
    }


    public void handleSelection() {
    }


    public void setWindow(TargetEditionWindow window) {
        this.window = window;
    }
    
}
