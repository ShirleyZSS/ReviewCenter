package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequiredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Shirley on 2017/7/16.
 */
@Component        //否则不会初始化  将拦截器添加到MVC中
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);//将拦截器注册进来 全局
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");//处理setting页面的时候 调用拦截器（比如某些页面需要访问权限）

        super.addInterceptors(registry);
    }
}
