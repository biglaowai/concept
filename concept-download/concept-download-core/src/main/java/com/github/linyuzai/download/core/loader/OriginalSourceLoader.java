package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;

import java.io.IOException;

public interface OriginalSourceLoader {

    void load(OriginalSource source, DownloadContext context) throws IOException;
}
