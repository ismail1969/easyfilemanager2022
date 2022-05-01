package eu.ismailozer.easyfilemanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EasyFileWalker {

	public static void main(String[] args) {
		System.out.println("Stating....");

		HashMap<Integer, DublicateFiles> map = new HashMap<Integer, DublicateFiles>();
		map = new MainGUI().getDublicateFileList_XXX("C:\\POP", "C:\\Musik");

		// map = new MainGUI().getDublicateFileList("C:\\Musik",
		// "C:\\Temp\\dublicates");

		for (Map.Entry<Integer, DublicateFiles> entry : map.entrySet()) {
			int key = entry.getKey();
			DublicateFiles files = entry.getValue();
			System.out.println("file = " + key + " - " + files.getLeftFile() + " : " + files.getRightFile());
		}

		// FileSearchParameters parameterObject = new FileSearchParameters("", "mp3",
		// "Ali", "", 0, true, false, false,
		// null, null, false);
		//
		// // FileMatchChecker fileMatchChecker = new FileMatchChecker(parameterObject);
		// ProcessTimeController.start();
		// EasyFileWalker fw = new EasyFileWalker("C:\\Musik\\", true, true, false, 0,
		// parameterObject);
		// // fw.walk("c:\\Musik\\", new StringBuffer() );
		// ArrayList<EasyFileCollector> fileArrayList = fw.getFileList(true);
		// //fw.sortFileSize();
		// // StringBuffer pBuffer;
		// int fileCounter = 0;
		// int dirCounter = 0;
		// for (EasyFileCollector f : fileArrayList) {
		// // boolean test = (new FileMatcher()).isSearchedFileMatch(
		// // pFilename,
		// // pResetCounterValue,
		// // pStartsWith,
		// // pEndsWith,
		// // pContainStr,
		// // pSearchPattern,
		// // pFileSize,
		// // pAllFileSize,
		// // pFileSizeGreater,
		// // pFileSizeLower,
		// // pFileSizeEqual,
		// // pCreationDateFrom,
		// // pCreationDateTo,
		// // pSuccessiveUppercaseChars);
		//
		// // if (!fileMatchChecker.matchAllParameter(f)) {
		// // continue;
		// // }
		//
		// // if(!(new FileMatcher()).isSearchedFileMatch(
		// // f.getName(),
		// // false, "",
		// // "mp3", "", "",
		// // "", true, false,
		// // false, false,
		// // "", "", false)){
		// // continue;
		// // }
		//
		//// if (f.getFile().isDirectory()) {
		//// // walk(f.getAbsolutePath(), pBuffer);
		//// System.out.println(dirCounter++ + ".) Dir:" + f.getFile().getAbsoluteFile()
		// + "\n");
		//// // pBuffer.append("Dir:" + f.getAbsoluteFile() + "\n");
		//// } else {
		//// // System.out.println( "File:" + f.getAbsoluteFile() );
		//// System.out.println(fileCounter++ + ".) File:" +
		// f.getFile().getAbsoluteFile() + "\n");
		//// }
		// }
		//
		System.out.println("End....ElapsedTime: " + ProcessTimeController.getElapsedTime());
	}

	private String path;
	private boolean recursiveMode;
	private boolean includeFiles;
	private boolean includeDirectories;
	private int searchLimit;
	private ArrayList<EasyFileInfo> fileList;
	FileSearchParameters parameterObject;

	FileMatchChecker fileMatchChecker;

	public EasyFileWalker(String pPath, boolean pRecursiveMode, boolean pIncludeFiles, boolean pIncludeDirectories,
			int pSearchLimit) {
		path = pPath;
		recursiveMode = pRecursiveMode;
		includeFiles = pIncludeFiles;
		includeDirectories = pIncludeDirectories;
		searchLimit = pSearchLimit;
	}

	public EasyFileWalker(String pPath, boolean pRecursiveMode, boolean pIncludeFiles, boolean pIncludeDirectories,
			int pSearchLimit, FileSearchParameters pParameterObject) {
		path = pPath;
		recursiveMode = pRecursiveMode;
		includeFiles = pIncludeFiles;
		includeDirectories = pIncludeDirectories;
		searchLimit = pSearchLimit;

		if (pParameterObject != null) {
			parameterObject = pParameterObject;
			fileMatchChecker = new FileMatchChecker(parameterObject);
		}
	}

	public ArrayList<EasyFileInfo> getFileList(boolean pSortFileSize) {
		startSearchFile();
		// if(pFileSizeSorting) {
		// //Collections.sort(fileList,new EasyFileComparator());
		// sort(fileList);
		// }
		if (pSortFileSize) {
			// System.out.println("getFileList: sort entries now!!!!");
			sortFileSize();
			// System.out.println("getFileList: sorting finished!!!!");
		}
		return fileList;
	}

	private boolean searchLimitReached() {
		if (searchLimit > 0 && fileList != null && fileList.size() >= searchLimit) {
			return true;
		}
		return false;

	}

	public void setFileList(ArrayList<EasyFileInfo> fileList) {
		this.fileList = fileList;
	}

	public ArrayList<EasyFileInfo> sort(ArrayList<EasyFileInfo> arrayOfFiles) {
		Collections.sort(arrayOfFiles, new FileSizeArraySorter());
		return arrayOfFiles;
	}

	public void sortFileSize() {
		Collections.sort(fileList, new FileSizeArraySorter());
		// return arrayOfFiles;
	}

	public void startSearchFile() {

		fileList = new ArrayList<EasyFileInfo>();
		try {
			walkDirectory(path, recursiveMode, includeFiles, includeDirectories, searchLimit);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void walkDirectory(String pPath, boolean pRecursiveMode, boolean pIncludeFiles, boolean pIncludeDirectories,
			int pSearchLimit) throws IOException {

		File root = new File(pPath);
		File[] list = root.listFiles();

		//System.out.println("searchLimitReached() = " + searchLimitReached());
		if (list == null || list.length <= 0 || searchLimitReached()) {
			return;
		}

		for (File f : list) {
			if (f.isDirectory()) {
				walkDirectory(f.getAbsolutePath(), pRecursiveMode, pIncludeFiles, pIncludeDirectories, pSearchLimit);
				// System.out.println( "Dir:" + f.getAbsoluteFile() );
				if (pIncludeDirectories) {
					if (parameterObject != null && fileMatchChecker != null) {
						if (fileMatchChecker.matchAllParameter(f)) {
							fileList.add(new EasyFileInfo(f.length(), f));
						}
					} else {
						fileList.add(new EasyFileInfo(f.length(), f));
					}
				}

				if (searchLimitReached()) {
					break;
				}

			} else {
				// System.out.println( "File:" + f.getAbsoluteFile() );
				if (pIncludeFiles) {
					if (parameterObject != null && fileMatchChecker != null) {
						if (fileMatchChecker.matchAllParameter(f)) {
							fileList.add(new EasyFileInfo(f.length(), f));
						}
					} else {
						fileList.add(new EasyFileInfo(f.length(), f));
					}
				}
				if (searchLimitReached()) {
					break;
				}
			}
		}
	}
}