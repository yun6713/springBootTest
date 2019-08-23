package com.bonc.integrate;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

import javax.validation.ConstraintValidator;

import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.data.redis.connection.convert.StringToPropertiesConverter;
import org.springframework.format.Formatter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;

import com.bonc.entity.jpa.User;
import com.bonc.utils.MapUtils;
public class SpringArgsHandle {
	Validator a;
	ValidationUtils a2;
	Errors a3;	
	BeanPropertyBindingResult a4;
	ConstraintValidator a5;
	//如何使用？基于注解的自动验证，基于Validator的手动验证
	DataBinder b;
	WebDataBinder b2;
	BeanWrapper bw;
	PropertyEditor c;
	PropertyEditorManager c1;
	ConversionService c2;
	Converter c3;
	ConverterFactory c4;
	GenericConverter c5;
	ConversionServiceFactoryBean c6;//注册ConversionService bean
	Formatter d;
	@Test
	public void testValidator() {
		User user=new User();
		Errors errors=new BeanPropertyBindingResult(user,"user");
		ValidationUtils.invokeValidator(new UserValidator(), user, errors);
		System.out.println(errors.getFieldErrors());
	}
	@Test
	public void testBeanWrapper() {
		//需要无参构造器
		BeanWrapper bw=new BeanWrapperImpl(User.class);
		bw.setPropertyValues(MapUtils.builder("username", "li")
				.put("password", "tianlin").build());
		System.out.println(bw.getPropertyValue("password"));
		ClassEditor ce=new ClassEditor();
		ce.setAsText("java.lang.String");
		System.out.println(ce.getValue());
		System.out.println(ce.getSource());
		ConversionServiceFactoryBean csfb= new ConversionServiceFactoryBean();
		csfb.setConverters(new HashSet<>(Arrays.asList(new StringToPropertiesConverter())));
		csfb.afterPropertiesSet();
		ConversionService cs=csfb.getObject();
		System.out.println(cs.convert("a=1,b=2", Properties.class));
	}
	public static class UserValidator implements Validator{

		@Override
		public boolean supports(Class<?> clazz) {
			return User.class.isAssignableFrom(clazz);
		}

		@Override
		public void validate(Object target, Errors errors) {
			User user=(User) target;
			if(user.getAddr()==null)
				errors.rejectValue("addr", "can't be null");
		}
		
	}
}
