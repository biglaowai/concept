package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.SourceFactory;
import com.github.linyuzai.download.core.source.prefix.PrefixSourceFactory;

import java.io.File;

/**
 * 用户目录的工厂 / Factory for user home
 */
public class UserHomeSourceFactory extends PrefixSourceFactory {

    public static final String[] PREFIXES = new String[]{
            "user.home:",
            "user_home:",
            "user-home:",
            "userHome:",
            "userhome:"};

    public static final String USER_HOME = System.getProperty("user.home");

    private final SourceFactory factory = new FileSourceFactory();

    /**
     * Use {@link FileSource}
     *
     * @param source  需要下载的数据对象 / Object to download
     * @param context 下载上下文 / Context of download
     * @return 下载源 / Source {@link FileSource}
     */
    @Override
    public Source create(Object source, DownloadContext context) {
        String path = getContent((String) source);
        return factory.create(new File(USER_HOME, path), context);
    }

    @Override
    protected String[] getPrefixes() {
        return PREFIXES;
    }
}
