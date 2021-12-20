package com.sh.coupon.config;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程类
 */
@Slf4j
@Configuration
@EnableAsync
public class AsyncPoolConfig implements AsyncConfigurer {
    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setQueueCapacity(20);//线程队列大小
        executor.setKeepAliveSeconds(60); //线程最大等待时间
        executor.setMaxPoolSize(20); //线程最大数量
        executor.setCorePoolSize(10);//线程核心数量
        executor.setThreadNamePrefix("SHSync_");//线程前缀
        //执行完毕后子线程是否自动关闭
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setAwaitTerminationSeconds(60);//关闭时线程等待时间
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    class  AsyncExceptionHandler implements AsyncUncaughtExceptionHandler{

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            throwable.getStackTrace();
            log.error("throwable {} , method {} , object {}",throwable.getMessage(),method.getName(), JSON.toJSONString(objects));
            //继续执行可能的发送邮件等操作
            // TODO

        }
    }
}
