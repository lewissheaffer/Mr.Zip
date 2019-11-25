package mrZip;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.googlecode.concurrenttrees.common.Iterables;
import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radix.node.util.PrettyPrintable;

public class Compressor {
	public static void main(String[] args) {
		RadixTree<Integer> tree = new ConcurrentRadixTree<Integer>(new DefaultCharArrayNodeFactory());
		Scanner fileInput = new Scanner(System.in);
		System.out.println("Enter file address (/..filename.txt)");
		String fileName = fileInput.next();
		fileInput.close();
		File f = new File(fileName);
		Scanner fileScanner;
		try {
			fileScanner = new Scanner(f);
			while(fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				char[] lineArray = line.toCharArray();
				int keyCount = 0;
				for(int i = 0; i<lineArray.length; i++) {
					String tempString = "" + lineArray[i];
					while(tree.getValueForExactKey(tempString) != null) {
						if(i+1 < lineArray.length) {
							i++;
							tempString += lineArray[i];
						}
					}
					System.out.println(tempString);
					tree.put(tempString, keyCount);
					keyCount++;
				}
				System.out.println("Keys starting with 'H': " + Iterables.toString(tree.getKeysStartingWith("H")));
				PrettyPrinter.prettyPrint((PrettyPrintable) tree, System.out);
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("No file found. Check for syntax errors.");
		}
	}
}
