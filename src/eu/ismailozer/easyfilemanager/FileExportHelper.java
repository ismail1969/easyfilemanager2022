package eu.ismailozer.easyfilemanager;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JTable;

public class FileExportHelper {
	
	public static void writeCSVFile(String pCSVFile, JTable pTable, int pStartInd, int pEndInd) {
//		try {
//			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pCSVFile), "UTF-8"));
//			int total_Tbl_Items = pTable.getRowCount();
//			for (int i = pStartInd; i < pEndInd; i++) {
//				if (i < total_Tbl_Items) {
//					String filename = (String) tableSearchResult.getValueAt(i, 1);
//					String[] piece = filename.trim().split("[\\\\]");
//					out.write(piece[piece.length - 3] + ";" + piece[piece.length - 2] + ";" + piece[piece.length - 1]
//							+ System.getProperty("line.separator"));
//					FileUtilityHelper.log("GetValueAt : " + (String) tableSearchResult.getValueAt(i, 1),
//							boolean pLoggingCommandLine);
//				} else {
//					break;
//				}
//			}
//			out.close();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
	}
	

}
