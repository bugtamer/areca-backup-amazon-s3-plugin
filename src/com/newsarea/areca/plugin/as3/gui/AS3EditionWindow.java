package com.newsarea.areca.plugin.as3.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.application.areca.ApplicationException;
import com.application.areca.launcher.gui.common.ListPane;
import com.application.areca.launcher.gui.common.SecuredRunner;
import com.myJava.util.Util;
import com.myJava.util.log.Logger;
import com.newsarea.areca.plugin.as3.AS3FileSystemPolicy;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class AS3EditionWindow extends com.application.areca.launcher.gui.common.AbstractWindow {
	
	private AS3FileSystemPolicy _policy = null;
	private Thread _currentRunningTest = null;
	//
	protected Text txtAS3Key;
    protected Text txtAS3Secret;
    protected Text txtBucket;
    protected Text txtPrefix;
    //
    protected Button btnTest;
    protected Button btnSave;
    protected Button btnCancel;
	
	public AS3EditionWindow(AS3FileSystemPolicy policy) {
		super();
		this._policy = policy;
	}
	
	protected Control createContents(Composite parent) {
        Composite ret = new Composite(parent, SWT.NONE);
        ret.setLayout(new GridLayout(1, false));

        ListPane tabs = new ListPane(ret, SWT.NONE, false);
        GridData dt = new GridData(SWT.FILL, SWT.FILL, true, true);
        tabs.setLayoutData(dt);

        Composite itm1 = tabs.addElement("General", "General");
        this.initMainPanel(itm1);       
        
        this.buildSaveComposite(ret);
        this.initValues();
        
        tabs.setSelection(0);
        ret.pack();
        return ret;
    }
	
	private void initValues() {
		if (this._policy == null) { return; } 
		//
		try {
			txtAS3Key.setText(this._policy.getKey());
			txtAS3Secret.setText(this._policy.getSecret());
			txtBucket.setText(this._policy.getBucket());
			txtPrefix.setText(this._policy.getPrefix());
		} catch(IllegalArgumentException iaEx) { }
	}
	
	private GridLayout initLayout(int nbCols) {
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.numColumns = nbCols;
        layout.marginHeight = 0;
        layout.verticalSpacing = 10;
        layout.horizontalSpacing = 10;
        return layout;
    }
	
	private Composite initMainPanel(Composite parent) {
        parent.setLayout(new FillLayout());
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(initLayout(1));
        
        Group grpServer = new Group(composite, SWT.NONE);
        grpServer.setText("Amazon S3");
        grpServer.setLayout(new GridLayout(3, false));
        grpServer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        Label lblAS3Key = new Label(grpServer, SWT.NONE);
        lblAS3Key.setText("Key");
        txtAS3Key = new Text(grpServer, SWT.BORDER);
        GridData dt = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        dt.widthHint = computeWidth(300);
        txtAS3Key.setLayoutData(dt);
        monitorControl(txtAS3Key);
        
        Label lblAS3Secret = new Label(grpServer, SWT.NONE);
        lblAS3Secret.setText("Secret");
        txtAS3Secret = new Text(grpServer, SWT.BORDER);
        GridData dt2 = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        dt.widthHint = computeWidth(300);
        txtAS3Secret.setLayoutData(dt2);
        monitorControl(txtAS3Secret);
        
        Label lblBucket = new Label(grpServer, SWT.NONE);
        lblBucket.setText("Bucket");
        txtBucket = new Text(grpServer, SWT.BORDER);
        GridData dt3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        dt.widthHint = computeWidth(300);
        txtBucket.setLayoutData(dt3);
        monitorControl(txtBucket);
        
        Label lblPrefix = new Label(grpServer, SWT.NONE);
        lblPrefix.setText("Prefix");
        txtPrefix = new Text(grpServer, SWT.BORDER);
        GridData dt4 = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
        dt.widthHint = computeWidth(300);
        txtPrefix.setLayoutData(dt4);
        monitorControl(txtPrefix);
        
        return composite;
    }

	private void buildSaveComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        composite.setLayout(new GridLayout(3, false));
        
        btnTest = new Button(composite, SWT.PUSH);
        btnTest.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        btnTest.setText("Test");
        btnTest.addListener(SWT.Selection, new Listener(){
            public void handleEvent(Event event) {
                final AS3FileSystemPolicy policy = new AS3FileSystemPolicy();
                initPolicy(policy);
                
                Runnable rn = new Runnable() {
                    public void run() {
                        validate(policy);    
                    }
                };
                Thread th = new Thread(rn, "AS3 Test #" + Util.getRndLong());
                th.setDaemon(true);
                registerCurrentRunningTest(th);
                th.start();
            }
        });
        
        btnSave = new Button(composite, SWT.PUSH);
        btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
        btnSave.setText("Save");
        btnSave.addListener(SWT.Selection, new Listener(){
            public void handleEvent(Event event) {
            	registerCurrentRunningTest(null);
                saveChanges();
            }
        });
        
        btnCancel = new Button(composite, SWT.PUSH);
        btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        btnCancel.setText("Cancel");
        btnCancel.addListener(SWT.Selection, new Listener(){
            public void handleEvent(Event event) {
            	registerCurrentRunningTest(null);
            	cancelChanges();
            }
        });
    }	
	
	private boolean checkText(Text fld) {
		 if (fld.getText() == null || fld.getText().trim().length() == 0) {
			 this.setInError(fld);
	         return false;
		 }
	     return true;
	}
	
	@Override
	protected boolean checkBusinessRules() {
		this.resetErrorState(txtAS3Key);
        this.resetErrorState(txtAS3Secret);
        this.resetErrorState(txtBucket);
        this.resetErrorState(txtPrefix);
        //
        if (! checkText(txtAS3Key)) { return false; }
        if (! checkText(txtAS3Secret)) { return false; }
        if (! checkText(txtBucket)) { return false; }
        if (! checkText(txtPrefix)) { return false; }
        //
        return true;
	}

	@Override
	public String getTitle() {
		return "Amazon S3";
	}

	@Override
	protected void saveChanges() {
		/******************
		*      CHECK
		*****************/		
		try {
			AS3FileSystemPolicy policy = new AS3FileSystemPolicy();
			this.initPolicy(policy);
			policy.validate(false);
			
		} catch(ApplicationException aEx) {
			application.showErrorDialog("invalid configuration: " + aEx.getMessage(), "Invalid Configuration", true);
			return;
		}
		/******************
		 *      SAVE
		 *****************/
		if(this._policy == null) { 
			this._policy = new AS3FileSystemPolicy();
		}
		//
		this.initPolicy(this._policy);
		//
		this.hasBeenUpdated = false;
		this.close();
	}
	
	private void initPolicy(AS3FileSystemPolicy policy) {
		policy.setKey(txtAS3Key.getText());
		policy.setSecret(txtAS3Secret.getText());
		policy.setBucket(txtBucket.getText());
		policy.setPrefix(txtPrefix.getText());
	}

	@Override
	protected void updateState(boolean rulesSatisfied) {
		btnSave.setEnabled(rulesSatisfied);
        btnTest.setEnabled(rulesSatisfied);
	}
	
	public AS3FileSystemPolicy getCurrentPolicy() {
        return this._policy;
    }
	
	private void registerCurrentRunningTest(Thread th) {
        try {
            if (this._currentRunningTest != null) {
                this._currentRunningTest.interrupt();
            }
        } catch (Exception e) {
            Logger.defaultLogger().error(e);
        }
        this._currentRunningTest = th;
    }
	
	protected void validate(AS3FileSystemPolicy policy) {   	
        SecuredRunner.execute(new Runnable() {
            public void run() {
                btnTest.setEnabled(false);
            }
        });

        try {
            policy.validate(true);
            SecuredRunner.execute(new Runnable() {
                public void run() {
                    application.showInformationDialog("configuration is valid", "Valid Configuration", false);
                }
            });
        } catch (final Throwable e) {
            SecuredRunner.execute(new Runnable() {
                public void run() {
                	application.showErrorDialog("invalid Configuration: " + e.getMessage(), "Invalid Configuration", true);
                }
            });
        } finally {
            SecuredRunner.execute(new Runnable() {
                public void run() {
                    btnTest.setEnabled(true);
                }
            });
        }
    }

}
