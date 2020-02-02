# Mr.Zip
A simple lossless txt file compressor and decompressor using a variant of the Lempel-Ziv compression algorithm. 
## About:
Bored over a long weekend, I thought it would be interesting to learn about something that I used fairly regularly: lossless compression. With a little research, I was surprised to find that the Lempel-Ziv compression algorithms developed in the 70s and 80s are still used as the basis for almost every lossless compression program today. Since the overall concept seemed fairly straight forward, I decided to try my hand at building my own compressor and decompressor. My compression program is based on the LZ78 algorithm and uses character encoding. While functional, this compressor is purely concept based and somewhat inefficient. I would still recommend using any of the mainstream compression programs as they are much more effective. 
## To Compress
Since I did not include a jar or executable file for the compressor and the decompressor, you will need to compile the Compressor.java and Decompressor.java files before running.

Once compiled, the Compressor.java file should be run first. To use the Compressor, the target txt file must be in the project's uncompressed folder. When the program is run, the console will prompt the user to enter the file name. 
``` 
Enter file address (filename.txt)    
test.txt
```
Once the file has been compressed, the console will list the following compression info specific to the file. 
```     
Original File Size: 28510 Bytes      
Compressed File Size: 19906 Bytes     
Ratio:69.82111539810593% of original 
```
The compressed txt file can be found in the project's compressed folder under the original name.

## To Decompress 
To use the decompressor, the compressed txt file must be in the project's compressed folder. Similar to the compressor, the running Decompressor will prompt the user to enter the file name. If the compressed file was found, a decompressed version of the text file will be created and placed in the decompressed folder.  
<br>
Example files for each step can be found here: [Original](uncompressed/test.txt) [Compressed](compressed/test.txt) [Decompressed](decompressed/test.txt)  
<br>
Once this process is complete, feel free to compare the decompressed file with the original. I can guarantee that they will be exactly the same! 

