package cosine.boat.Decompression;


public class ProgressUtil { 
	public static void UnZipFile(final String zipFileString, final String outPathString, final ZipListener listener) { 
		Thread zipThread = new UnMainThread(zipFileString, outPathString, listener); 
		zipThread.start(); 
	} 
	public interface ZipListener { 
		void zipStart(); 
		void zipSuccess(); 
		void zipProgress(int progress); 
		void zipFail(); 
	} 
} 
