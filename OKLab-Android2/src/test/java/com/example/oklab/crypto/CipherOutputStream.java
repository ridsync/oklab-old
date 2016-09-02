/**
 * (c)Copyright 2010-2010, BaruSoft Co., Ltd. All rights reserved <br/>
 * 
 * @description <br/>
 *
 * @create 2013. 1. 7.
 * @author freelky
 */
package com.oklab.crypto;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;

/**
 *
 */
public class CipherOutputStream extends FilterOutputStream
{
	/** The underlying cipher. */
	private Cipher cipher;
	private boolean isBase64;
	
	/**
	* Create a new cipher output stream. The cipher argument must have already
	* been initialized.
	* 
	* @param out The sink for transformed data.
	* @param cipher The cipher to transform data with.
	*/
	public CipherOutputStream(OutputStream out, Cipher cipher, boolean isBase64){
		super(out);
		this.isBase64 = isBase64;
		this.cipher = (cipher != null) ? cipher : new NullCipher();
	}
	
	/**
	* Create a cipher output stream with no cipher.
	*
	* @param out The sink for transformed data.
	*/
	protected CipherOutputStream(OutputStream out)
	{
		super(out);
	}
	
	/**
	* Close this output stream, and the sink output stream.
	* <p>
	* This method will first invoke the {@link Cipher#doFinal()} method of the
	* underlying {@link Cipher}, and writes the output of that method to the
	* sink output stream.
	* 
	* @throws IOException If an I/O error occurs, or if an error is caused by
	*           finalizing the transformation.
	*/
	public void close() throws IOException {
		try {
			byte[] b = cipher.doFinal();
//			if(isBase64){
//				b = Base64.encode(b, Base64.DEFAULT);
//			}
		    out.write(b);
		    out.flush();
		    out.close();
		} catch (Exception cause) {
			IOException ioex = new IOException(String.valueOf(cause));
			ioex.initCause(cause);
			throw ioex;
		}
	}
	
	/**
	* Flush any pending output.
	*
	* @throws IOException If an I/O error occurs.
	*/
	public void flush() throws IOException {
		out.flush();
	}
	
	/**
	* Write a single byte to the output stream.
	* 
	* @param b The next byte.
	* @throws IOException If an I/O error occurs, or if the underlying cipher is
	*           not in the correct state to transform data.
	*/
	public void write(int b) throws IOException {
		write(new byte[] { (byte) b }, 0, 1);
	}
	
	/**
	* Write a byte array to the output stream.
	* 
	* @param buf The next bytes.
	* @throws IOException If an I/O error occurs, or if the underlying cipher is
	*           not in the correct state to transform data.
	*/
	public void write(byte[] buf) throws IOException {
		write(buf, 0, buf.length);
	}
	
	/**
	* Write a portion of a byte array to the output stream.
	* 
	* @param buf The next bytes.
	* @param off The offset in the byte array to start.
	* @param len The number of bytes to write.
	* @throws IOException If an I/O error occurs, or if the underlying cipher is
	*           not in the correct state to transform data.
	*/
	public void write(byte[] buf, int off, int len) throws IOException {
		byte[] b = cipher.update(buf, off, len);
		if (b != null) {
//			if(isBase64){
//				b = Base64.encode(b, Base64.DEFAULT);
//			}
			out.write(b);
		}
	}
}
