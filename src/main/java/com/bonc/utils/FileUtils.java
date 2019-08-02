package com.bonc.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;
/**
 * 文件工具类，方法：遍历查找文件、读写文件、获取项目路径
 * @author litianlin
 * @date   2019年7月30日下午12:51:22
 * @Description TODO
 */
public class FileUtils {
	private static final int K=1024;
	private static String PROJECT_PATH;
	private static final Logger log=LoggerFactory.getLogger(FileUtils.class);
	// /、$进行正则替换时，必须使用Matcher.quoteReplacement预处理
	private static final String JAVA_PRE="/src/main/java/".replaceAll(Matcher.quoteReplacement("//"), File.separator);
	private static final String RESOURCE_PRE="/src/main/resources/".replaceAll(Matcher.quoteReplacement("//"), File.separator);
	public static final Charset DEFAULT_CHARSET=StandardCharsets.UTF_8;
	static {
		try {
			PROJECT_PATH=getProjectPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Files.walkFileTree遍历文件
	 * @param path
	 * @param callback
	 * @throws IOException
	 */
	public static void walkFileTree(String path,Function<File,FileVisitResult> callback) throws IOException{
		walkFileTree(Paths.get(path),callback);
	}
	public static void walkFileTree(Path path,Function<File,FileVisitResult> callback) throws IOException{
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				return callback.apply(file.toFile());
			}
		});
	}
	/**
	 * Files.walkFileTree遍历文件，返回符合条件的文件路径
	 * @param path
	 * @param callback
	 * @return 
	 * @throws IOException
	 */
	public static Set<Path> searchFiles(String path,Function<File,Boolean> callback) throws IOException{
		return searchFiles(Paths.get(path),callback);
	}
	public static Set<Path> searchFiles(Path path,Function<File,Boolean> callback) throws IOException{
		Set<Path> paths = new HashSet<>();
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if(callback.apply(file.toFile()))
					paths.add(file);
				return FileVisitResult.CONTINUE;
			}
		});
		return paths;
	}
	/**
	 * Files.walkFileTree遍历文件，是否存在符合条件的文件
	 * @param path
	 * @param callback
	 * @return
	 * @throws IOException
	 */
	public static  boolean existsFile(String path,Function<File,Boolean> callback) throws IOException{
		return existsFile(Paths.get(path),callback);
	}
	public static  boolean existsFile(URI path,Function<File,Boolean> callback) throws IOException{
		return existsFile(Paths.get(path),callback);
	}
	public static  boolean existsFile(File path,Function<File,Boolean> callback) throws IOException{
		return existsFile(path.toPath(),callback);
	}
	public static  boolean existsFile(Path path,Function<File,Boolean> callback) throws IOException{
		Set<Path> paths = new HashSet<>();
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if(callback.apply(file.toFile())) {
					paths.add(file);
					return FileVisitResult.TERMINATE;
				}
				return FileVisitResult.CONTINUE;
			}
		});
		return !paths.isEmpty();
	}
	/**
	 * 读取文件内容，存入字符串；对utf8-bom类型文件，剔除首字符。
	 * classpath下资源，通过Resource读入。
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String file2String(String path) throws IOException {
		path=path.trim();
		return path.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)?resource2String(getResource(path))
				:file2String(getFile(path));
	}
	public static String file2String(File file) throws IOException {
		StringBuffer sb = new StringBuffer();
		try(
				FileReader fr = new FileReader(file);
			){
			CharBuffer cb = CharBuffer.allocate(8*K);
			while(fr.read(cb)!=-1) {
				cb.flip();
				sb.append(cb.toString());
				cb.clear();
			}
		}
		String info = sb.toString();
		//io读取时首位不可见
//			System.out.println(info.codePointAt(0));
		if(info.codePointAt(0)==65279)//UTF-8-BOM模式
			info=info.substring(1);
		
		return info;
	}
	public static String resource2String(Resource resource) throws IOException {
		StringBuffer sb = new StringBuffer();
		try(
				BufferedInputStream bis = new BufferedInputStream(resource.getInputStream());
		){
			int size=8*K;
			byte[] bytes = new byte[size];
			long len=resource.contentLength();
			//Math.ceil中转double计算，否则计算值不准
			for(long i=0;i<Math.ceil((double)len/size);i++) {
				int temp=(int) Math.min(size, len-i*size);
				bis.read(bytes, 0, temp);
				sb.append(new String(bytes,0,temp,DEFAULT_CHARSET));
			}
		}
		String info = sb.toString();
		//io读取时首位不可见
//			System.out.println(info.codePointAt(0));
		if(info.codePointAt(0)==65279)//UTF-8-BOM模式
			info=info.substring(1);
		
		return info;
	}
	/**
	 * 将字符串写入文件，append选择写入模式
	 * @param str
	 * @param path
	 * @param append
	 * @throws IOException 
	 */
	public static void string2File(String str,String path,boolean append) throws IOException {
		
		string2File(str,getFile(path),append);
	}
	/**
	 * 将字符串写入文件，append选择写入模式 
	 * @param str
	 * @param file
	 * @param append
	 * @throws IOException 
	 */
	public static void string2File(String str, File file, boolean append) throws IOException {
		if(file.isDirectory())
			throw new RuntimeException("file can't be a directory.");
		try(
				RandomAccessFile raf = new RandomAccessFile(file,"rw");
			){			
			//追加
			if(append) {
				raf.seek(file.length());
			}
			raf.write(str.getBytes(DEFAULT_CHARSET));
		}
	}
	/**
	 * 完全覆盖文件；通过先删除，后创建实现。
	 * @param str
	 * @param file
	 * @throws IOException
	 */
	public static void string2File(String str, String path) throws IOException {
		string2File(str, getFile(path));
	}
	public static void string2File(String str, File file) throws IOException {
		if(file.exists()) {
			file.delete();
		}
		string2File(str, file, false);
	}
	/**
	 * 获取类class文件所在包、classpath绝对路径
	 * @param clazz
	 * @param classpath
	 * @return
	 */
	public static String getClassPath(Class<?> clazz,boolean classpath) {
		return classpath?clazz.getClassLoader().getResource("").toString()
				:clazz.getResource("").toString();
	}
	/**
	 * java文件所在包、classpath路径；test包classpath路径定位到/src/main/resources/
	 * 项目路径+src/main/java/+package；
	 * 项目路径+src/main/resources/
	 * @param clazz
	 * @return
	 */
	public static String getJavaPath(Class<?> clazz,boolean classpath) {		
		return classpath?String.format("%1$s%2$s", PROJECT_PATH,RESOURCE_PRE)
				:String.format("%1$s%2$s%3$s%4$s", PROJECT_PATH,JAVA_PRE,
						clazz.getPackage().getName().replaceAll("\\.", Matcher.quoteReplacement(File.separator)),File.separator);
	}
	/**
	 * 获取项目根路径
	 * @return
	 */
	public static String getProjectPath() throws URISyntaxException {
		if(PROJECT_PATH==null) {
			String str = FileUtils.class.getResource("/").toURI().toString().trim();
			//非jar包运行,剔除target/classes;jar包运行，剔除.jar!/及以后内容
			if(str.indexOf(".jar!/")==-1) {
				int end = str.endsWith("/target/classes/")?str.lastIndexOf("/target/classes/")
						:str.lastIndexOf("/target/test-classes/");
				PROJECT_PATH = str.substring(str.indexOf("/"),end);
			}else{
				str = str.substring(str.indexOf("/"), str.indexOf(".jar!/"));
				PROJECT_PATH = str.substring(0, str.lastIndexOf("/")+1);
			}
			log.info("Project path is: {}",PROJECT_PATH);
		}
		return PROJECT_PATH;
	}
	/**
	 * 封装ResourceUtils，避免打包后加载classpath资源异常
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static File getFile(String path) throws IOException {
		log.debug(path);
		path = path.trim();
		if(path.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
			throw new RuntimeException("不可获取classpath路径的File，打包后会报错");
		}
		return ResourceUtils.getFile(path);
	}
	public static Resource getResource(String path) throws IOException {
		log.debug(path);
		path = path.trim();
		if(path.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
			path=path.substring(10).trim();
			return new ClassPathResource(path);
		}
		try {
			return new UrlResource(path);
		}catch (Exception e) {
			return new FileSystemResource(path);
		}
	}
	public static void main(String[] args) throws Exception {
		File r=FileUtils.getFile("classpath:es1");
		System.out.println(r.length());
	}
	public static String readPoi(String path) throws IOException {
		return readPoi(new File(path));
	}
	/**
	 * poi读取doc、docx；xls、ppt缺
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readPoi(File file) throws IOException {
		if(!file.exists()||file.isDirectory())
			return "";
		String content="",path=file.getPath().trim();
		path=path.substring(path.lastIndexOf('.')+1);        
		switch(path) {
		case "doc":
			try(HWPFDocument doc = new HWPFDocument(new BufferedInputStream(new FileInputStream(file)));){
				content = doc.getDocumentText();
			}
			break;
		case "docx":
			try(
					XWPFDocument docx = new XWPFDocument(new BufferedInputStream(new FileInputStream(file)));
		            XWPFWordExtractor extractor = new XWPFWordExtractor(docx);){
				content = extractor.getText();
			}
			break;
		case "xls":
			break;
		case "xlsx":
			break;
		default:
			break;
		}
		
		return content;
	}
}
