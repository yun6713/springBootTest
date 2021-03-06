package com.bonc.test.qs;

import java.io.Externalizable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.AttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.util.Scanner;

import javax.naming.directory.Attributes;

import org.junit.Test;

public class IoClass {
	@Test
	public void file() {
		File file=new File("");
		FilenameFilter ff;
		file.deleteOnExit();
	}
	@Test
	public void stream() {
		InputStream is;
		Reader reader;
		OutputStream os;
		Writer writer;
		Scanner s;
		RandomAccessFile raf;
	}
	
	public void serialize() {
		Serializable s;
		Externalizable e;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		
	}
	public void nio() {
		Buffer buf;
		ByteBuffer bb;
		CharBuffer cb;
		Channel chn;

	}
	@Test
	public void charset() {
		Charset c=StandardCharsets.UTF_8;
		System.out.println(System.getProperty("file.encoding"));
		c.encode("");
		CharsetEncoder en=c.newEncoder();
	}
	public void nio2(){
		Paths ps;
		Path p;
		Files fs;
		AttributeView av;
		FileAttributeView fav;
		Attributes a;
	}
}
