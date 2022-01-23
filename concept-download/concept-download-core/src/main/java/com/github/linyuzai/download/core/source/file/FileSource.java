package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.web.ContentType;
import com.github.linyuzai.download.core.source.AbstractSource;
import lombok.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 文件下载源 / A source that holds a file
 * 该下载源持有一个文件对象，可能是文件，可能是目录 / The source holds a file object, which may be a file or a directory
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileSource extends AbstractSource {

    @NonNull
    @Setter
    protected File file;

    protected Part part;

    @SneakyThrows
    @Override
    public InputStream openInputStream() {
        return file.isFile() ? new FileInputStream(file) : new EmptyInputStream();
    }

    /**
     * 如果没有指定名称 / If no name is specified
     * 返回文件名称 / Return file name
     *
     * @return 名称 / Name
     */
    @Override
    public String getName() {
        String name = super.getName();
        if (!StringUtils.hasText(name)) {
            setName(file.getName());
        }
        return super.getName();
    }

    @Override
    public String getContentType() {
        String contentType = super.getContentType();
        if (!StringUtils.hasText(contentType)) {
            setContentType(ContentType.file(file));
        }
        return super.getContentType();
    }

    /**
     * 如果是文件则返回文件大小 / If it is a file, the file size is returned
     * 如果是文件夹则返回整个文件夹的大小 / If it is a folder, returns the size of the entire folder
     *
     * @return 文件或文件夹大小 / File or folder size
     */
    @Override
    public Long getLength() {
        return length0(file);
    }

    private long length0(File file) {
        if (file.isFile()) {
            return file.length();
        } else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return 0;
            }
            long length = 0;
            for (File f : files) {
                length += length0(f);
            }
            return length;
        }
    }

    /**
     * 如果是文件则返回true / Returns true if it is a file
     * 如果是文件夹则返回false / False if it is a folder
     *
     * @return 是否是单个的 / If single
     */
    @Override
    public boolean isSingle() {
        return file.isFile();
    }

    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    @Override
    public boolean isCacheExisted() {
        return file.exists();
    }

    @Override
    public String getCachePath() {
        return file.getParent();
    }

    @Override
    public String getDescription() {
        return "FileSource(" + file.getAbsolutePath() + ")";
    }

    /**
     * 每次都新建，返回文件目录的实时结构
     *
     * @return 返回一个文件或整个目录结构 / Returns a file or the entire directory structure
     */
    @Override
    public Collection<Part> getParts() {
        if (part != null) {
            part.release();
        }
        String name = getName();
        part = new FilePart(file, name, name);
        List<Part> parts = new ArrayList<>();
        Downloadable.addPart(part, parts);
        return parts;
    }

    @Override
    public void release() {
        super.release();
        if (part != null) {
            part.release();
            part = null;
        }
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends FileSource, B extends Builder<T, B>> extends AbstractSource.Builder<T, B> {

        private File file;

        public B file(File file) {
            this.file = file;
            return (B) this;
        }

        @Override
        protected T build(T target) {
            target.setFile(file);
            return super.build(target);
        }

        @Override
        public T build() {
            return build((T) new FileSource());
        }
    }
}
