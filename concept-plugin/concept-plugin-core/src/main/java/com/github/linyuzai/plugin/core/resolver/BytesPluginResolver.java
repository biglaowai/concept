package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@AllArgsConstructor
@DependOnResolvers(FileNamePluginResolver.class)
public abstract class BytesPluginResolver extends AbstractPluginResolver {

    private final int bufferSize;

    public BytesPluginResolver() {
        this(-1);
    }

    @Override
    public boolean support(PluginContext context) {
        return context.contains(Plugin.FILE_NAMES);
    }

    public byte[] toBytes(InputStream stream) {
        return toBytes(stream, bufferSize);
    }

    @SneakyThrows
    public static byte[] toBytes(InputStream stream, int bufferSize) {
        try (InputStream is = stream;
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            if (bufferSize > 0) {
                byte[] buffer = new byte[bufferSize];
                int l;
                while ((l = is.read(buffer)) > 0) {
                    os.write(buffer, 0, l);
                }
            } else {
                int available = stream.available();
                if (available > 0) {
                    byte[] buffer = new byte[available];
                    int l = is.read(buffer);
                    os.write(buffer, 0, l);
                }
            }
            return os.toByteArray();
        }
    }
}