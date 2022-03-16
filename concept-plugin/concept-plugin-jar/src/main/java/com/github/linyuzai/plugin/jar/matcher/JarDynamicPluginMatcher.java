package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.matcher.DynamicPluginMatcher;
import com.github.linyuzai.plugin.core.matcher.PluginContextMatcher;
import com.github.linyuzai.plugin.core.matcher.PluginObjectMatcher;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.resolver.JarBytesPluginResolver;
import com.github.linyuzai.plugin.jar.resolver.JarInstancePluginResolver;
import com.github.linyuzai.plugin.jar.resolver.JarPropertiesPluginResolver;
import lombok.NonNull;

@DependOnResolvers({
        JarInstancePluginResolver.class,
        JarPropertiesPluginResolver.class,
        JarBytesPluginResolver.class})
public class JarDynamicPluginMatcher extends DynamicPluginMatcher {

    public JarDynamicPluginMatcher(@NonNull Object target) {
        super(target);
        matchers.add(new PluginContextMatcher<PluginContext>() {
            @Override
            public void onMatched(PluginContext plugin) {
            }
        });
        matchers.add(new PluginObjectMatcher<Plugin>() {
            @Override
            public void onMatched(Plugin plugin) {
            }
        });
        matchers.add(new ClassMatcher<Object>() {
            @Override
            public void onMatched(Object plugin) {
            }
        });
        matchers.add(new InstanceMatcher<Object>() {
            @Override
            public void onMatched(Object plugin) {
            }
        });
        matchers.add(new PropertiesMatcher<Object>() {
            @Override
            public void onMatched(Object plugin) {
            }
        });
    }
}
