package mrZip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

public class Decompressor {
	public static void main(String[] args) {
		HashMap<String, String> map = new HashMap<String, String>();
		Scanner fileInput = new Scanner(System.in);
		System.out.println("Decompressor");
		System.out.println("Enter file address (filename.txt)");
		String fileName = fileInput.next();
		String dirFileName = "compressed/" + fileName;
		fileInput.close();
		File f = new File(dirFileName);
		Scanner fileScanner;
		try {
			PrintWriter writer = new PrintWriter(("decompressed/" + fileName), "UTF-8");
			fileScanner = new Scanner(f);
			String encodedChar;
			String addedChar;
			String retrievalKey = "";
			String segmentKey = "";
			int numbSegChars = 0;
			boolean newLine = false;
			int buffer = 1;
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				char[] lineArray = line.toCharArray();
				if (newLine) {
					writer.print(System.getProperty("line.separator"));
				} else {
					numbSegChars = Character.getNumericValue(lineArray[0]);
					for (int i = 0; i < numbSegChars; i++) {
						retrievalKey += '"';
					}
					segmentKey = retrievalKey;
				}
				newLine = true;
				for (int i = 0 + buffer; i < (lineArray.length); i += 0) {
					// First key is assumed to start at "" and continue upward ("#,"$,"%)
					encodedChar = "";
					for (int j = 0; j < numbSegChars; j++) {
						encodedChar += lineArray[i++];
					}
					char[] encodedArray = encodedChar.toCharArray();
					// If new digit is found
					if (encodedArray[0] == ' ') {
						// Third Digit
						addedChar = "" + lineArray[i++];
					}
					// represents a duplicate segment from a previous key.
					else if (encodedArray[0] == '!') {
						String dupKey = "";
						for (int j = 1; j < encodedArray.length; j++) {
							dupKey += encodedArray[j];
						}
						dupKey += lineArray[i++];
						addedChar = map.get(dupKey);
					}
					// If encoded segment references a previous segment
					else {
						addedChar = "" + map.get(encodedChar) + lineArray[i++];
					}
					map.put(segmentKey, addedChar);
					segmentKey = incrementSegmentKey(segmentKey);
				}
				buffer = 0;
				while (!retrievalKey.equals(segmentKey)) {
					writer.print(map.get(retrievalKey));
					retrievalKey = incrementSegmentKey(retrievalKey);
				}
			}
			writer.close();
			fileScanner.close();
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
