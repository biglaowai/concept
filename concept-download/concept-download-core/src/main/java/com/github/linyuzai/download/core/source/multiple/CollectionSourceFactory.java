package com.github.linyuzai.download.core.source.multiple;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.SourceFactoryAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionSourceFactory implements SourceFactory {

    @Override
    public boolean support(Object source, DownloadContext context) {
        return source instanceof Collection;
    }

    @Override
    public Source create(Object source, DownloadContext context) {
        SourceFactoryAdapter adapter = context.get(SourceFactoryAdapter.class);
        List<Source> sources = new ArrayList<>();
        for (Object o : ((Collection<?>) source)) {
            SourceFactory factory = adapter.getOriginalSourceFactory(o, context);
            sources.add(factory.create(o, context));
        }
        return new MultipleSource(sources);
    }


}