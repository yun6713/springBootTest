package com.bonc.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import com.bonc.constant.DocType;
import com.bonc.constant.ProjectConstant;
import com.bonc.entity.DocEntity;
import com.bonc.service.DocService;
/**
 * 阻塞队列实现bulk,key为uuid;30构建DocEntity，3个bulk线程，标志位判定是否结束
 * 
 * 阻塞队列+死循环，保证取出全部数据；
 * @author litianlin
 * @date   2019年11月1日上午9:24:31
 * @Description TODO
 */
@Service
public class DocServiceImpl2 implements DocService {
	private static final BlockingQueue<IndexQuery> QUEUE=new LinkedBlockingQueue<>(8*ProjectConstant.K);
	private static final TXTParser txtParser=new TXTParser();
	private static final int readSize=50,bulkSize=7,indexSize=10;
	private static final ExecutorService es=Executors.newFixedThreadPool(readSize+bulkSize);
	//	读、bulk线程数
	private static final Semaphore reads=new Semaphore(readSize),bulks=new Semaphore(bulkSize);
	@Autowired
	AutoDetectParser parser;
	@Autowired
//	es必须填写配置文件信息后才自动装配
	ElasticsearchTemplate et;
	@Override
	public String add(String path) {
		try {
			System.out.println(path);
			dir(Paths.get(path));
			bulk();
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}
//	单独抽出，便于递归
	private void dir(Path p) throws Exception {
		Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
				try {
					reads.acquire();
					es.execute(()->{
						try {
							parseExample(path.toFile());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally {
//							只能在任务内部释放信号量，否则信号量无法限流
							reads.release();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}
	private void bulk() throws InterruptedException {
//		使用全部bulk线程以提高并发性能
		while(bulks.availablePermits()>0){
			bulks.acquire();
			es.execute(()->{
				try {
					sendBulk();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
//					System.out.println("release:"+id);
					bulks.release();
				}
			});
		}
	}
	//	从阻塞队列中读取index，做bulk操作；通过队列为空判定是否读取完毕
	private void sendBulk() throws InterruptedException {
		do {
			List<IndexQuery> queries = new ArrayList<>();
			for (; queries.size() < indexSize;) {
				IndexQuery iq=QUEUE.poll(100, TimeUnit.MILLISECONDS);
//				到达延时后为null，退出循环
				if(iq!=null) {
					queries.add(iq);
				}else{
					break;
				}
			}
			if(!queries.isEmpty()) {
//				System.out.println("id:"+id);
				et.bulkIndex(queries);
			}
			if(queries.size()<10) {
				break;
			}
		}while(true);
	}
//	通过tika读取文档内容，构建index存入阻塞队列
	private void parseExample(File file) throws Exception {
		if(file.isDirectory()) {
			dir(file.toPath());
		}
	    BodyContentHandler handler = new BodyContentHandler();
	    Metadata metadata = new Metadata();
	    //拼接DocEntity对象
	    try (InputStream stream = new FileInputStream(file)) {
	    	String fName=file.getName();
	    	int loc=fName.lastIndexOf(".");
	    	DocType dt=loc==-1?DocType.OTHERS
					:DocType.of(fName.substring(loc).toLowerCase());
	    	if(DocType.TXT.equals(dt)) {
	    		txtParser.parse(stream, handler, metadata, new ParseContext());
	    	}else {
		    	parser.parse(stream, handler, metadata);
	    	}
	    	Map<String,Object> map=new HashMap<>();
	    	for(String name:metadata.names()) {
	    		map.put(name,metadata.get(name));	
	        }
	    	DocEntity de=new DocEntity(fName,handler.toString(),dt,map);
	    	IndexQuery query=new IndexQueryBuilder()
//	    			.withIndexName("doc").withType("_doc")
	    			.withObject(de)
	    			.build();
//	    	System.out.println(fName);
    		QUEUE.offer(query);
//    		避免延迟大时bulk线程提前终止，漏发文档
    		if(bulks.availablePermits()>0) {
    			bulk();
    		}
	    	//	        System.out.println(handler.toString());
//	        for(String name:metadata.names()) {
//	        	System.out.println(name+":"+metadata.get(name));	
//	        }
	    }
//	    System.out.println(file.getName());
//	    System.out.println("end");
	}
	@Override
	public Object searchByContent(String content) {
		QueryBuilder qb=QueryBuilders.matchQuery("content", content);
		Object obj=et.queryForPage(new StringQuery(qb.toString()), DocEntity.class);
		return obj;
	}

}
