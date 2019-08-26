package com.bonc.integrate;

import java.util.Arrays;
import java.util.Map;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.bonc.entity.jpa.Role;
import com.bonc.utils.MapUtils;
public class SpEL {

	
	public void spelTest() {
		//普通表达式
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("'1' == '12'");		
		Object val=exp.getValue();
		System.out.println("result:"+val);
		//传入rootObject，属性或getter必须为public。根对象属性直接引用。
		Role role=new Role();
		role.setRoleName("admin");
		exp = parser.parseExpression("roleName == 'admin'");		
		val=exp.getValue(role);
		System.out.println("result:"+val);
		//EvaluationContext;非根对象属性，加#前缀。
		Long start=System.currentTimeMillis();
		Map<String,Object> map=MapUtils.builder("obj",new Object())
				.put("c", "b")
				.put("list", Arrays.asList(role)).build();
		EvaluationContext ec=new StandardEvaluationContext();
		map.forEach(ec::setVariable);
		exp = parser.parseExpression("#c == 'b'");		
		val=exp.getValue(ec);
		System.out.println(System.currentTimeMillis()-start);
		System.out.println("result:"+val);
		//静态引用
		System.out.println(parser.parseExpression("T(java.lang.Math).random()").getValue());
		//集合操作
		System.out.println(parser.parseExpression("#list.![roleName]").getValue(ec));
		
	}
}
