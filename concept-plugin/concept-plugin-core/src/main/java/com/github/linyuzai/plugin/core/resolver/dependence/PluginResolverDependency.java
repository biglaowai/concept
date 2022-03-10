package com.github.linyuzai.plugin.core.resolver.dependence;

import com.github.linyuzai.plugin.core.resolver.PluginResolver;

public interface PluginResolverDependency {

    @SuppressWarnings("unchecked")
    default Class<? extends PluginResolver>[] dependencies() {
        Class<?> clazz = getClass();
        while (clazz != null) {
            DependOnResolver annotation = clazz.getAnnotation(DependOnResolver.class);
            if (annotation != null) {
                return annotation.value();
            }
            clazz = clazz.getSuperclass();
        }
        return new Class[0];
    }
}
