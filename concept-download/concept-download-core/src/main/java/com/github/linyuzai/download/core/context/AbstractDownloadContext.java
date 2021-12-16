package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;

/**
 * 下载上下文 / Context of download
 * 在下载操作中提供数据的隔离和共享 / Provides data isolation and sharing during download operations
 */
public abstract class AbstractDownloadContext implements DownloadContext {

    @Getter
    private final DownloadOptions options;

    /**
     * 上下文依赖一个下载操作的参数 / Context depend on a download options
     *
     * @param options 下载操作参数 / Options of download
     */
    public AbstractDownloadContext(DownloadOptions options) {
        this.options = options;
    }
}