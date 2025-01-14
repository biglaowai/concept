package com.github.linyuzai.domain.core.proxy;

import com.github.linyuzai.domain.core.DomainObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 领域代理
 */
public interface DomainProxy extends InvocationHandler {

    @SuppressWarnings("unchecked")
    default <I extends DomainObject> I create(Class<I> cls) {
        if (cls.isInterface()) {
            return (I) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, this);
        }
        throw new IllegalArgumentException("Class must be interface");
    }
}
