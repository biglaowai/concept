package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.matcher.GenericTypePluginMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import com.github.linyuzai.plugin.jar.resolver.JarClassPluginResolver;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@DependOnResolvers(JarClassPluginResolver.class)
public abstract class ClassMatcher<T> extends GenericTypePluginMatcher<Class<? extends T>> {

    private boolean equals;

    @Override
    public boolean ifMatch(PluginContext context) {
        List<Class<?>> classes = context.get(JarPlugin.CLASSES);
        List<Class<?>> matchedClasses = classes.stream()
                .filter(this::matchClass)
                .collect(Collectors.toList());
        if (matchedClasses.isEmpty()) {
            return false;
        }
        if (matchedClasses.size() > 1) {
            throw new PluginException("Multi class found, try ClassListMatcher");
        }
        context.set(this, matchedClasses.get(0));
        return true;
    }

    public boolean matchClass(Class<?> clazz) {
        return matchClass(clazz, equals);
    }

    public Class<?> getMatchingClass() {
        return null;
    }
}