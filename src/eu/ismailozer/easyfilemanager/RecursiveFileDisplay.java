package eu.ismailozer.easyfilemanager;

//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.io.Writer;
////import java.nio.Buffer;
//import java.nio.charset.Charset;

public class RecursiveFileDisplay {

	// private static StringBuffer buffer = new StringBuffer();
	// private int dir_counter = 0;
	int file_counter = 0;

	public static void main(String[] args) {
		// String path = "C:\\Musik\\";
		// StringBuffer buf = new StringBuffer();
		// System.out.println("Start: ");
		// // File currentDir = new File(path); // current directory
		// // new RecursiveFileDisplay().displayDirectoryContents(currentDir);
		//
		// new EasyFilewalkerOld().walk(path, buf);
		// try {
		// RecursiveFileDisplay.writeToFile("C:\\Temp\\list-musik-mp3.log", buf);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println("Ende: ");
	}

	// public void displayDirectoryContents(File dir) {
	// try {
	//
	// File[] files = dir.listFiles();
	// for (File file : files) {
	// if (file.isDirectory()) {
	// buffer.append(dir_counter++).append(" .) directory: " +
	// file.getCanonicalPath().toString() + "\n");
	// // System.out.println("directory:" +
	// // file.getCanonicalPath());
	// displayDirectoryContents(file);
	// } else {
	// // System.out.println(" file:" + file.getCanonicalPath());
	// buffer.append(file_counter++).append(" .) file: " +
	// file.getCanonicalPath().toString() + "\n");
	// }
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// // write it to file
	// try {
	// writeToFile("C:\\Temp\\list-musik-mp3.log", buffer);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// public static void writeToFile(String pFilename, StringBuffer pData) throws
	// IOException {
	// // Writer out = new BufferedWriter(new OutputStreamWriter(new
	// // FileOutputStream(pFilename), "UTF-8"));
	// BufferedWriter out = new BufferedWriter(
	// new OutputStreamWriter(new FileOutputStream(pFilename),
	// Charset.forName("UTF-8").newEncoder()));
	//
	// // OutputStreamWriter char_output = new OutputStreamWriter(new
	// // FileOutputStream(out.toString()),Charset.forName("UTF-8").newEncoder());
	// // char_output.write(pFilename);
	// // char_output.flush();
	// // char_output.close();
	//
	// // BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));
	// out.write(pData.toString());
	// out.flush();
	// out.close();
	// }

}