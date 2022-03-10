package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolver.PluginResolverChain;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolver;
import com.github.linyuzai.plugin.jar.JarPlugin;

import java.util.Collection;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@DependOnResolver(JarEntryPluginResolver.class)
public class JarFileNamePluginResolver extends AbstractPluginResolver {

    @Override
    public void resolve(PluginContext context, PluginResolverChain chain) {
        Collection<JarEntry> entries = context.get(JarPlugin.ENTRIES);
        List<String> filenames = entries.stream()
                .map(ZipEntry::getName)
                .collect(Collectors.toList());
        context.set(JarPlugin.FILE_NAMES, filenames);
        chain.next(context);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(JarPlugin.ENTRIES);
    }
}
