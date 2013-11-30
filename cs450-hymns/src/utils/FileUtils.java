package utils;

import java.io.File;
import java.io.FilenameFilter;

public class FileUtils {
	public static File[] getWavFiles(String dirPath) {
		File folder = new File(dirPath);
		return folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".wav");
			}
		});
	}
}
