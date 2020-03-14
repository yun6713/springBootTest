package com.bonc.entity;

import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.bonc.constant.DocType;
//ES文档描述
@Document(indexName="doc",type="_doc")
public class DocEntity {
	private String title;
	@Field(store=true,type=FieldType.Text,analyzer="ik_max_word",ignoreFields="content")
	private String content;
	private DocType type;
	private Map<String,Object> metadata;
	
	public DocEntity() {
		super();
	}
	public DocEntity(String title, String content, DocType type, Map<String, Object> metadata) {
		super();
		this.title = title;
		this.content = content;
		this.type = type;
		this.metadata = metadata;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
//		处理浏览器显示格式问题
		this.content = content.replaceAll("(\n|\r\n)", "<br/>");
	}
	public String getType() {
		return type==null?"其他":type.getType();
	}
	public void setType(String type) {
		this.type = DocType.of(type);
	}
	public Map<String, Object> getMetadata() {
		return metadata;
	}
	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}
	
}
