package eu.ismailozer.easyfilemanager;

//import com.sun.jmx.snmp.Timestamp;

import java.io.*;

class ImageFileFilter implements FileFilter {
	private String extention;

	public ImageFileFilter(String pExtention) {
		extention = pExtention;
	}

	@Override
	public boolean accept(File file) {
		if (file.getName().toLowerCase().endsWith(extention)) {
			return true;
		}
		return false;
	}
}