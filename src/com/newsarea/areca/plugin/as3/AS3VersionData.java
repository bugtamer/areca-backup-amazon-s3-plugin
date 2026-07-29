package com.newsarea.areca.plugin.as3;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;

import com.myJava.util.log.Logger;
import com.myJava.util.version.VersionData;


public class AS3VersionData extends VersionData {

    // URLs
	private static final String LATEST_VERSION_URL = "http://localhost/areca/plugins/as3/version.xml";
    private static final String DOWNLOAD_URL       = "https://sourceforge.net/projects/areca-backup/files/plugins/Amazon_S3/";

	// Release date
	private static final int YEAR = 2026;
	private static final int ONE_BASED_MONTH = 7;
	private static final int DAY = 29;
    private static final GregorianCalendar RELEASE_DATE = new GregorianCalendar(YEAR, (ONE_BASED_MONTH - 1), DAY);

	// Release version
	private static final int MAJOR = 3;
	private static final int MINOR = 0;
	private static final int PATCH = 0;
	private static final String SEMANTIC_VERSION = MAJOR + "." + MINOR + "." + PATCH;
	
	// Change log
	private static final String DESCRIPTION = "Fixed rbernhardt's 'Newsarea - Areca Amazon S3 Plugin'.";
	private static final String ADDITIONAL_NOTES = null;
	private static final String IMPLEMENTATION_NOTES = null;


    public AS3VersionData() {
        super();
		Logger.defaultLogger().fine("AS3VersionData");

        setVersionDate(RELEASE_DATE);
		setVersionId(SEMANTIC_VERSION);
        setDescription(DESCRIPTION);

        // Additional Notes
		if (isDefined(ADDITIONAL_NOTES)) {
			setAdditionalNotes(ADDITIONAL_NOTES);
		}

		// Implementation Nodes
		if (isDefined(IMPLEMENTATION_NOTES)) {
	        setImplementationNodes(IMPLEMENTATION_NOTES);
		}

        // Version XML URL
		try {
            final URL versionXmlUrl = new URL(LATEST_VERSION_URL);
            // version.setVersionXmlUrl(versionXmlUrl); // FIXME missing before Areca 8.3.0 and override toString()
        } catch (MalformedURLException e) {
            Logger.defaultLogger().error("AS3VersionData - Malformed version xml url", e);
            e.printStackTrace();
        }

        // Download URL
		try {
            final URL downloadUrl = new URL(DOWNLOAD_URL);
            setDownloadUrl(downloadUrl);
        } catch (MalformedURLException e) {
            Logger.defaultLogger().error("AS3VersionData - Malformed download url", e);
            e.printStackTrace();
        }

		Logger.defaultLogger().finest("AS3VersionData - output=" + this);
    }


	private boolean isDefined(String text) {
		Logger.defaultLogger().finest("AS3VersionData - isDefined(text) - text=" + text);
		return (text != null) && (text.length() > 0);
	}

}
