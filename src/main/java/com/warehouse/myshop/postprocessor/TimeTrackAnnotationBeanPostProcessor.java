package com.warehouse.myshop.postprocessor;

import com.warehouse.myshop.anotation.TimeTrack;
import com.warehouse.myshop.sheduling.SimpleProductPriceScheduler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

@Component
public class TimeTrackAnnotationBeanPostProcessor implements BeanPostProcessor {
    private boolean needProxy;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof SimpleProductPriceScheduler)
        System.out.println(bean.getClass());
        Class<?> beanClass = bean.getClass();

        // Проверяем, есть ли аннотированные методы
        for (Method method : beanClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(TimeTrack.class)) {
                needProxy = true;
                break;
            }
        }

        if (needProxy) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
                if (!method.isAnnotationPresent(TimeTrack.class)) {
                    return proxy.invokeSuper(obj, args);
                }
                Instant start = Instant.now();
                Object result = proxy.invokeSuper(obj, args);
                Instant end = Instant.now();
                Duration duration = Duration.between(start, end);
                long minutes = duration.toMinutes();
                long seconds = duration.getSeconds() % 60;
                System.out.println(method.getName() + " Время выполнения метода : " + minutes + "мин " + seconds + "сек");
                return result;
            });
            return enhancer.create();
        }

        return bean; // Возвращаем оригинальный бин, если проксирование не требуется
    }
}
