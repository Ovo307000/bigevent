package com.ovo307000.bigevent.global.utils;

import org.springframework.stereotype.Component;

@Component("threadLocalUtil")
public class ThreadLocalUtil<T>
{
    private final ThreadLocal<T> threadLocal = new ThreadLocal<>();

    public void set(T t)
    {
        this.threadLocal.set(t);
    }

    public T getAndRemove()
    {
        T object = this.get();

        this.remove();

        return object;
    }

    public void remove()
    {
        this.threadLocal.remove();
    }

    public T get()
    {
        return this.threadLocal.get();
    }

}
