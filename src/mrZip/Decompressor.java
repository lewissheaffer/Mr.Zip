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
			char segmentKeyChar1 = '"'; // First two characters in ascii at index 32
			char segmentKeyChar2 = '"';
			char retrievalKeyChar1 = '"';
			char retrievalKeyChar2 = '"';
			boolean newLine = false;
			while (fileScanner.hasNextLine()) {
				int lineIndex = 0;
				if(newLine) {
					writer.print(System.getProperty("line.separator"));
				}
				newLine = true;
				String line = fileScanner.nextLine();
				char[] lineArray = line.toCharArray();
				for (int i = 0; i < (lineArray.length) / 3; i++) {
					// First key is assumed to start at "" and continue upward (example "#,"$,"%
					// First Digit
					char encodedChar1 = lineArray[lineIndex++];
					encodedChar = "" + encodedChar1;
					// Second Digit
					char encodedChar2 = lineArray[lineIndex++];
					encodedChar += encodedChar2;

					// Third digit
					// If new digit it found
					if (encodedChar.equals("  ")) {
						// Third Digit
						addedChar = "" + lineArray[lineIndex++];
					}
					// ! As the first character of a segment
					//represents a duplicate segment from a previous key.
					else if (encodedChar1 == '!') {
						addedChar = map.get("" + encodedChar2 + lineArray[lineIndex++]);
					}
					
					// If encoded segment references a previous segment
					else {
						addedChar = "" + map.get(encodedChar) + lineArray[lineIndex++];
					}
					String segmentKey = "" + segmentKeyChar1 + segmentKeyChar2;
					map.put(segmentKey, addedChar);

					// Rollover statements for both segmentKeyChars
					if ((int) segmentKeyChar2 == 127) {
						segmentKeyChar2 = '"';
						segmentKeyChar1++;
					} else {
						segmentKeyChar2++;
					}
					
				}
				while(!(retrievalKeyChar2 == segmentKeyChar2 && retrievalKeyChar1 == segmentKeyChar1)) {
						String retrievalKey = "" + retrievalKeyChar1 + retrievalKeyChar2;
						writer.print(map.get(retrievalKey));
						if ((int) retrievalKeyChar2 == 127) {
							retrievalKeyChar2 = '"';
							retrievalKeyChar1++;
						} else {
							retrievalKeyChar2++;
						}
				}
				retrievalKeyChar2 = segmentKeyChar2;
				retrievalKeyChar1 = segmentKeyChar1;
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
}
