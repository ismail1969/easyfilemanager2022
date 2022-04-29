package eu.ismailozer.easyfilemanager;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DefaultToolkitScreenSizeReader {
	
	public DefaultToolkitScreenSizeReader(){
		
	}

	public  int getZoomFactor() {
		return 100;
	}

	public  int getScreenWidth() {
		//System.out.println(" getScreenWidth = " + (int)getDefaultToolkitDimension().width);
		return (int)getDefaultToolkitDimension().width;
	}

	public  int getScreenHeight() {
		//System.out.println(" getScreenHeight = " + (int)getDefaultToolkitDimension().height);
		return (int)getDefaultToolkitDimension().height;
	}

	public  int getFrameHeight() {
		//heightFrame = (heightScreen * zoomFactor / 100);
		//int heightFrame = getScreenHeight() * getZoomFactor() / 100;
		int heightFrame =  (getScreenHeight() * getZoomFactor() / 100) >= getScreenHeight() ? getScreenHeight() : getFrameHeight();
		//System.out.println(" heightFrame = " + heightFrame);
		return heightFrame;
	}

	public  int getFrameWidth() {
		//widthFrame = (widthScreen * zoomFactor / 100);
		//int widthFrame = getScreenWidth() * getZoomFactor() / 100;
		int widthFrame = (getScreenWidth() * getZoomFactor() / 100) >= getScreenWidth() ? getScreenWidth() : getFrameWidth();
		//System.out.println(" widthFrame = " + widthFrame);
		return widthFrame;
		
		//return widthFrame;
	}	
	
//	private int zoomFactor = 100;
//	private int widthScreen;
//	private int widthFrame;
//	private int heightScreen;
//	private int heightFrame;

//	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//	WIDTH_SCR = d.width;
//	HEIGHT_SCR = d.height;
//
//	WIDTH_FRM = (WIDTH_SCR * ZOOM_FACTOR / 100);
//	WIDTH_FRM = WIDTH_FRM > WIDTH_SCR ? WIDTH_SCR : WIDTH_FRM;
//
//	HEIGHT_FRM = (HEIGHT_SCR * ZOOM_FACTOR / 100);
//	HEIGHT_FRM = HEIGHT_FRM > HEIGHT_SCR ? HEIGHT_SCR : HEIGHT_FRM;	
	
	
	private void determinePreferedSizes() {
//		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		
//		widthScreen = getDefaultToolkitDimension().width;
//		heightScreen = getDefaultToolkitDimension().height;

//		widthFrame = (widthScreen * zoomFactor / 100);
//		widthFrame = widthFrame > widthScreen ? widthScreen : widthFrame;

//		heightFrame = (heightScreen * zoomFactor / 100);
//		heightFrame = heightFrame > heightScreen ? heightScreen : heightFrame;
	}
	
	public Dimension getDefaultToolkitDimension() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		 return d;
	}
	

}
