package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.options.DownloadOptions;

import java.io.IOException;
import java.util.function.Function;

/**
 * 执行下载的统一且唯一接口
 */
public interface DownloadConcept {

    /**
     * 根据 {@link DownloadOptions} 执行下载操作
     *
     * @param options 下载参数
     */
    default void download(DownloadOptions options) throws IOException {
        download(downloadConfiguration -> options);
    }

    void download(Function<DownloadConfiguration, DownloadOptions> function) throws IOException;
}
