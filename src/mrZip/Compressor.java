package mrZip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

public class Compressor {
	public static void main(String[] args) {
		HashMap<String,String> map = new HashMap<String,String>();
		Scanner fileInput = new Scanner(System.in);
		System.out.println("Enter file address (filename.txt)");
		String fileName = fileInput.next();
		String dirFileName = "uncompressed/" + fileName;
		fileInput.close();
		File f = new File(dirFileName);
		Scanner fileScanner;
		try {
			PrintWriter writer = new PrintWriter(("compressed/" + fileName), "UTF-8");
			fileScanner = new Scanner(f);
			char segmentKeyChar1 = '"'; // First two characters in ascii at index 32
			char segmentKeyChar2 = '"';
			boolean newLine = false;
			while(fileScanner.hasNextLine()) {
				if (newLine) {
					writer.print(System.getProperty("line.separator"));
				}
				newLine = true;
				String line = fileScanner.nextLine();
				char[] lineArray = line.toCharArray();
				lineLoop:
				for(int i = 0; i<lineArray.length; i++) {
					String segmentKey = "" + segmentKeyChar1 + segmentKeyChar2;
					String tempString = "";
					boolean reusedSegment = false;
					char lastChar = lineArray[i];
					while(map.containsKey(tempString + lastChar)) {
						reusedSegment = true;
						tempString += "" + lineArray[i];
						//If the end of the line is reached
						//and the current segment is in the map
						if(i + 1 >= lineArray.length) {
							writer.print("!" + map.get(tempString));
							if ((int) segmentKeyChar2 == 127) {
								segmentKeyChar2 = '"';
								segmentKeyChar1++;
							} else {
								segmentKeyChar2++;
							}
							break lineLoop;
						}
						lastChar = lineArray[++i];
					}
					if(!reusedSegment) {
						writer.print("  " + lineArray[i]);
					}
					else {
						writer.print(map.get(tempString) + lastChar);
					}
					map.put(tempString + lastChar, segmentKey);
					if ((int) segmentKeyChar2 == 127) {
						segmentKeyChar2 = '"';
						segmentKeyChar1++;
					} else {
						segmentKeyChar2++;
					}
				}
			}
			writer.close();
			fileScanner.close();
			File compressedFile = new File("compressed/" + fileName);
			System.out.println("Original File Size: " + f.length() + " Bytes\nCompressed File Size: " + compressedFile.length() + " Bytes\nRatio:" + ((100 * (double)compressedFile.length()/f.length()) + "%"));
		} catch (FileNotFoundException e) {
			System.out.println("No file found. Check for syntax errors.");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new AssertionError("UTF-8 is unknown");
		}
	}
}
