package mrZip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

public class Compressor {
	public static void main(String[] args) {
		HashMap<String, String> cycleMap = new HashMap<String, String>();
		HashMap<String, String> map = new HashMap<String, String>();
		Scanner fileInput = new Scanner(System.in);
		System.out.println("Enter file address (filename.txt)");
		String fileName = fileInput.next();
		String dirFileName = "uncompressed/" + fileName;
		fileInput.close();
		File f = new File(dirFileName);
		Scanner cycleScanner;
		Scanner fileScanner;
		try {
			int uniqueSegments = 0;
			cycleScanner = new Scanner(f);
			while (cycleScanner.hasNextLine()) {
				String line = cycleScanner.nextLine();
				char[] lineArray = line.toCharArray();
				lineLoop: for (int i = 0; i < lineArray.length; i++) {
					String tempString = "";
					char lastChar = lineArray[i];
					while (cycleMap.containsKey(tempString + lastChar)) {
						tempString += "" + lineArray[i];
						if (i + 1 >= lineArray.length) {
							uniqueSegments++;
							break lineLoop;
						}
						lastChar = lineArray[++i];
					}
					cycleMap.put(tempString + lastChar, "");
					uniqueSegments++;
				}
			}
			int numbSegChars = (int) Math.ceil(Math.log(uniqueSegments) / Math.log(93));
			String segmentKey = "";
			for (int i = 0; i < numbSegChars; i++) {
				segmentKey += '"';
			}
			String spaceKey = "";
			for (int i = 0; i < numbSegChars; i++) {
				spaceKey += " ";
			}
			PrintWriter writer = new PrintWriter(("compressed/" + fileName), "UTF-8");
			writer.print(numbSegChars);
			fileScanner = new Scanner(f);
			boolean newLine = false;
			while (fileScanner.hasNextLine()) {
				if (newLine) {
					writer.print(System.getProperty("line.separator"));
				}
				newLine = true;
				String line = fileScanner.nextLine();
				char[] lineArray = line.toCharArray();
				lineLoop: for (int i = 0; i < lineArray.length; i++) {
					String tempString = "";
					boolean reusedSegment = false;
					char lastChar = lineArray[i];
					while (map.containsKey(tempString + lastChar)) {
						reusedSegment = true;
						tempString += "" + lineArray[i];
						// If the end of the line is reached
						// and the current segment is in the map
						if (i + 1 >= lineArray.length) {
							writer.print("!" + map.get(tempString));
							segmentKey = incrementSegmentKey(segmentKey);
							break lineLoop;
						}
						lastChar = lineArray[++i];
					}
					if (!reusedSegment) {
						writer.print(spaceKey + lineArray[i]);
					} else {
						writer.print(map.get(tempString) + lastChar);
					}
					map.put(tempString + lastChar, segmentKey);
					segmentKey = incrementSegmentKey(segmentKey);
				}
			}
			writer.close();
			fileScanner.close();
			File compressedFile = new File("compressed/" + fileName);
			System.out.println("Original File Size: " + f.length() + " Bytes\nCompressed File Size: "
					+ compressedFile.length() + " Bytes\nRatio:"
					+ ((100 * (double) compressedFile.length() / f.length()) + "% of original"));
		} catch (FileNotFoundException e) {
			System.out.println("No file found. Check for syntax errors.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new AssertionError("UTF-8 is unknown");
		}
	}

	public static String incrementSegmentKey(String key) {
		char[] keyArray = key.toCharArray();
		boolean rollover = true;
		for (int i = keyArray.length - 1; i >= 0; i--) {
			if (rollover) {
				if ((int) keyArray[i] == 127) {
					keyArray[i] = '"';
				} else {
					keyArray[i]++;
					rollover = false;
				}
			}
		}
		return (new String(keyArray));
	}
}
