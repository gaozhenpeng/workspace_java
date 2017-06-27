package com.at.java8_lambda;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class LambdaMain {
	private File srcDir = new File("e:/temp/");

	private void fileExamplePlainOldStyle() {
		FileFilter directoryFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
		File[] dirs = srcDir.listFiles(directoryFilter);
		travelFileArray(dirs);
	}

	private void fileExampleLambdaStyle() {
		FileFilter directoryFilter = (File f) -> f.isDirectory();
		File[] dirs1 = srcDir.listFiles(directoryFilter);
		travelFileArray(dirs1);

		FilenameFilter nameFilter = (File f, String n) -> {
			return n.toLowerCase().startsWith("c");
		}; // 如果返回的值与接口方法返回的值不一致，则需要使用{}包裹语句，并使用 return 返回合适的结果。
		File[] dirs2 = srcDir.listFiles(nameFilter); // FilenameFilter
		travelFileArray(dirs2);
	}

	private void fileExampleLambdaShortStyle() {
		File[] dirs1 = srcDir.listFiles(f -> f.isDirectory()); // FileFilter
		travelFileArray(dirs1);
		File[] dirs2 = srcDir.listFiles((File f, String n) -> {
			return n.toLowerCase().startsWith("c");
		}); // FilenameFilter
		travelFileArray(dirs2);
	}

	private void travelFileArray(File[] fs) {
		if (fs == null || fs.length <= 0) {
			return;
		}
		int fs_length = fs.length;
		for (int i = 0; i < fs_length; i++) {
			File f = fs[i];
			System.out.println("" + i + ": " + f.getName());
		}
	}

	public static void main(String[] args) {
		LambdaMain lambdaMain = new LambdaMain();
		System.out.println("fileExamplePlainOldStyle()");
		lambdaMain.fileExamplePlainOldStyle();
		System.out.println("");

		System.out.println("fileExampleLambdaStyle()");
		lambdaMain.fileExampleLambdaStyle();
		System.out.println("");

		System.out.println("fileExampleLambdaShortStyle()");
		lambdaMain.fileExampleLambdaShortStyle();
		System.out.println("");
	}
}
