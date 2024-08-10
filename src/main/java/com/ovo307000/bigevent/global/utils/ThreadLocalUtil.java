package com.ovo307000.bigevent.global.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("threadLocalUtil")
// 通过注解设置此 Bean 为多例模式
@Scope(value = "prototype")
public class ThreadLocalUtil
{
}
