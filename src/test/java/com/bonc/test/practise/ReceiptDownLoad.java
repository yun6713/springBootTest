package com.bonc.test.practise;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.http.HttpUtil;

/**
 * 通过jsoup下载网页，解析内容；存入csv中
 * @author litianlin
 * @date   2019年12月24日上午8:46:39
 * @Description TODO
 */
public class ReceiptDownLoad {
	private String info="https://e-receipt.carrefour.com.cn/receipt?334,90,36370,93346524,20191223182054,52.03=";
	private String TEMPLATE="https://e-receipt.carrefour.com.cn/receipt/receiptService/eticket/receipt/showReceiptDetails?shopNo=%1$s&machineNo=%2$s&orderId=%3$s&time=%4$s";
	@Test
	public void download() throws IOException {
		String[] params=info.split("\\?")[1].split(",");	
		String url=String.format(TEMPLATE, params[0], params[1], params[2], params[4]);
//		System.out.println(url);
		//调用hutool工具类获取请求结果
		String result=HttpUtil.get(url);
//		System.out.println(result);
		//获取JSONObject，用于后期写入
		JSONObject jObj=JSON.parseObject(result).getJSONObject("data");
		String time=jObj.getString("time");
		String date=jObj.getString("date");
		JSONArray arr=jObj.getJSONArray("commodityVos");
		List<String[]> list=new ArrayList<>();
		for(int i=0; i<arr.size(); i++) {
			JSONObject obj=arr.getJSONObject(i);
			String[] temp=new String[] {time,date,
					obj.getString("commodityName"),
					obj.getString("commodityPrice"),
					obj.getString("totalPrice"),
					obj.getString("weight")
					};
			list.add(temp);
		}
		CsvWriter writer=CsvUtil.getWriter("C:/Users/Administrator/Desktop/test.csv", StandardCharsets.UTF_8);
		writer.write(list);
	}
	@Test
	public void baiduTest() {
		String result=HttpUtil.get("https://www.baidu.com");
		System.out.println(result);
	}
}
