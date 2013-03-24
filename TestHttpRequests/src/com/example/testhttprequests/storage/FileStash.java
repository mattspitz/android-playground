package com.example.testhttprequests.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;

public class FileStash {
	private FileStash() { }

	public static void stashFile(Context context, byte[] data, String filename) throws IOException  {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
			out.write(data);
		} finally {
			if (out != null) out.close();
		}
	}

	public static byte[] getFile(Context context, String filename, boolean deleteAfterLoad) throws IOException {
		InputStream in = null;
		try {
			/* http://stackoverflow.com/a/1264737 */
			
			in = new BufferedInputStream(context.openFileInput(filename));
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			
			int nRead;
			byte[] data = new byte[16384];
			while ((nRead = in.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}

			buffer.flush();

			if (deleteAfterLoad) context.deleteFile(filename);

			return buffer.toByteArray();
		} finally {
			if (in != null) in.close();
		}
	}
}
