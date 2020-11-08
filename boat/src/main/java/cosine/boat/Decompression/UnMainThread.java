package cosine.boat.Decompression;
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.util.Enumeration; 
import java.util.zip.ZipEntry; 
import java.util.zip.ZipFile; 
import java.util.zip.ZipInputStream; 

import cosine.boat.Decompression.ProgressUtil.ZipListener; 
public class UnMainThread extends Thread { 
	String zipFileString; 
	String outPathString; 
	ZipListener listener; 
	public UnMainThread(String zipFileString, String outPathString, ZipListener listener) { 
		this.zipFileString = zipFileString; 
		this.outPathString = outPathString; 
		this.listener = listener; 
	} 
	@Override 
	public void run() { 
		super.run(); 
		try { 
			listener.zipStart(); 
			long sumLength = 0; 
			long ziplength = getZipTrueSize(zipFileString); 
			FileInputStream inputStream = new FileInputStream(zipFileString); 
			ZipInputStream inZip = new ZipInputStream(inputStream); 
			ZipEntry zipEntry; 
			String szName = ""; 
			while ((zipEntry = inZip.getNextEntry()) != null) { 
				szName = zipEntry.getName(); 
				if (zipEntry.isDirectory()) { 
					szName = szName.substring(0, szName.length() - 1); 
					File folder = new File(outPathString + File.separator + szName); 
					folder.mkdirs(); 
				} else { 
					File file = new File(outPathString + File.separator + szName); 
					file.createNewFile(); 
					FileOutputStream out = new FileOutputStream(file); 
					int len; 
					byte[] buffer = new byte[1024]; 
					while ((len = inZip.read(buffer)) != -1) { 
						sumLength += len; 
						int progress = (int) ((sumLength * 100) / ziplength); 
					
						updateProgress(progress, listener); 
						out.write(buffer, 0, len); 
						out.flush(); 
					} 
					out.close(); 
				} 
			} 
			listener.zipSuccess(); 
			inZip.close(); 
		} catch (Exception e) { 
			listener.zipFail(); 
		} 
	} 
	int lastProgress = 0; 
	private void updateProgress(int progress, ZipListener listener2) { 
		if (progress > lastProgress) { 
			lastProgress = progress; 
			listener2.zipProgress(progress); 
		} 
	} 
	public long getZipTrueSize(String filePath) { 
		long size = 0; 
		ZipFile f; 
		try { 
			f = new ZipFile(filePath); 
			Enumeration<? extends ZipEntry> en = f.entries(); 
			while (en.hasMoreElements()) { 
				size += en.nextElement().getSize(); 
			} 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return size; 
	} 
} 
