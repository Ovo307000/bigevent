package com.ovo307000.bigevent.core.utils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ThreadLocalUtilTest
{
    private static final Logger log = LoggerFactory.getLogger(ThreadLocalUtilTest.class);

    @Test
    void contextLoads()
    {
        ThreadLocal<String> nameThreadLocal = new ThreadLocal<>();

        List<Thread> threadList = List.of(new Thread(() -> new Thread(() -> nameThreadLocal.set(Thread.currentThread()
                                                                                                      .getName())).start()),
                                          new Thread(nameThreadLocal::get));

        threadList.forEach(Thread::start);

        threadList.forEach((Thread thread) ->
                           {
                               try
                               {
                                   thread.join();
                               }
                               catch (InterruptedException e)
                               {
                                   log.debug("Thread interrupted", e);
                               }
                           });
    }
}