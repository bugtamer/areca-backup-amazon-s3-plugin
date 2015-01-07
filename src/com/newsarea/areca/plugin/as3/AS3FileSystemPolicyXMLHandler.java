package com.newsarea.areca.plugin.as3;

import org.w3c.dom.Node;

import com.application.areca.AbstractRecoveryTarget;
import com.application.areca.adapters.AbstractFileSystemPolicyXMLHandler;
import com.application.areca.adapters.AdapterException;
import com.application.areca.adapters.TargetXMLReader;
import com.application.areca.adapters.TargetXMLWriter;
import com.application.areca.impl.policy.FileSystemPolicy;
import com.myJava.util.Util;

public class AS3FileSystemPolicyXMLHandler extends AbstractFileSystemPolicyXMLHandler {
	
	private static final String XML_AS3KEY_KEY = "as3key";
	private static final String XML_AS3SECRET_KEY = "as3secret";
	private static final String XML_PREFIX_KEY = "prefix";
	private static final String XML_BUCKET_KEY = "bucket";
			
	public FileSystemPolicy read(Node mediumNode, AbstractRecoveryTarget arg1, TargetXMLReader arg2) throws AdapterException {
		Node nameNode = mediumNode.getAttributes().getNamedItem(XML_MEDIUM_ARCHIVENAME);
		Node keyNode = mediumNode.getAttributes().getNamedItem(XML_AS3KEY_KEY);
		Node secretNode = mediumNode.getAttributes().getNamedItem(XML_AS3SECRET_KEY);
		Node prefixNode = mediumNode.getAttributes().getNamedItem(XML_PREFIX_KEY);
		Node bucketNode = mediumNode.getAttributes().getNamedItem(XML_BUCKET_KEY);
		//
		AS3FileSystemPolicy policy = new AS3FileSystemPolicy();
		//
		policy.setId("areca-plugin-as3");
		//
		if(keyNode == null) { throw new AdapterException("as3key"); }
		if(secretNode == null) { throw new AdapterException("as3secret"); }
		if(prefixNode == null) { throw new AdapterException("prefix"); }
		if(bucketNode == null) { throw new AdapterException("bucket"); }
		//
		policy.setArchiveName(nameNode.getNodeValue());
		policy.setKey(keyNode.getNodeValue());
		policy.setSecret(secretNode.getNodeValue());
		policy.setPrefix(prefixNode.getNodeValue());
		policy.setBucket(bucketNode.getNodeValue());
		//
		return policy;
	}

	public void write(FileSystemPolicy fspolicy, TargetXMLWriter arg1, boolean arg2, StringBuffer sb) {
		AS3FileSystemPolicy policy = (AS3FileSystemPolicy)fspolicy;
		sb.append(" ");
        sb.append(XML_MEDIUM_ARCHIVENAME);
        sb.append("=");
        sb.append(encode(policy.getArchiveName()));
        //
		sb.append(" ");
        sb.append(XML_AS3KEY_KEY);
        sb.append("=");
        sb.append(encode(policy.getKey()));
        //
        sb.append(" ");
        sb.append(XML_AS3SECRET_KEY);
        sb.append("=");
        sb.append(encode(policy.getSecret()));
        //
        sb.append(" ");
        sb.append(XML_PREFIX_KEY);
        sb.append("=");
        sb.append(encode(policy.getPrefix()));
        //
        sb.append(" ");
        sb.append(XML_BUCKET_KEY);
        sb.append("=");
        sb.append(encode(policy.getBucket()));
	}

	private static String encode(String orig) {
        String ret = orig;

        ret = Util.replace(ret, "&", "&amp;");
        ret = Util.replace(ret, "\n", "&#xA;");
        ret = Util.replace(ret, "<", "&lt;");
        ret = Util.replace(ret, ">", "&gt;");  
        ret = Util.replace(ret, "\"", "&quot;");
        ret = Util.replace(ret, "'", "&apos;");            
        
        return "\"" + ret + "\"";
    }
	
}
