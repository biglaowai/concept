package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.aop.annotation.CompressCache;
import com.github.linyuzai.download.aop.annotation.Download;
import com.github.linyuzai.download.aop.annotation.SourceCache;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.handler.StandardDownloadHandlerInterceptor;
import com.github.linyuzai.download.core.options.DownloadOptions;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/concept-download")
public class ConceptDownloadController {

    @Download(source = "file:/Users/Shared/README.txt")
    @GetMapping("/s1")
    public void s1() {
    }

    @Download
    @GetMapping("/s2")
    public String s2() {
        return "file:/Users/Shared/README.txt";
    }

    @Download
    @GetMapping("/s3")
    public File s3() {
        return new File("/Users/Shared/README.txt");
    }

    @Download(source = "user.home:/Public/README.txt")
    @GetMapping("/s4")
    public void s4() {
    }

    @Download
    @GetMapping("/s5")
    public String s5() {
        return "user.home:/Public/README.txt";
    }

    @Download(source = "classpath:/download/README.txt")
    @GetMapping("/s6")
    public void s6() {
    }

    @Download
    @GetMapping("/s7")
    public String s7() {
        return "classpath:/download/README.txt";
    }

    @Download
    @GetMapping("/s8")
    public ClassPathResource s8() {
        return new ClassPathResource("/download/README.txt");
    }

    @Download(filename = "s9.txt")
    @GetMapping("/s9")
    public String s9() {
        return "任意的文本将会直接作为文本文件处理";
    }

    @Download(source = "http://127.0.0.1:8080/concept-download/text.txt?a=1&b=2")
    @GetMapping("/s10")
    public void s10() {
    }

    @Download
    @GetMapping("/s11")
    public String s11() {
        return "http://127.0.0.1:8080/concept-download/text.txt";
    }

    @Download(source = "classpath:/download/README.txt", forceCompress = true)
    @GetMapping("/s12")
    public void s12() {
    }

    @Download(source = "classpath:/download/README_GBK.txt",
            filename = "readme.zip",
            charset = "GBK",
            forceCompress = true)
    @GetMapping("/s13")
    public void s13() {
    }

    @Download(source = {
            "classpath:/download/text.txt",
            "http://127.0.0.1:8080/concept-download/image.jpg",
            "http://127.0.0.1:8080/concept-download/video.mp4"},
            filename = "压缩包14.zip")
    @GetMapping("/s14")
    public void s14() {
    }

    @Download(filename = "压缩包15.zip")
    @GetMapping("/s15")
    public List<Object> s15() {
        List<Object> list = new ArrayList<>();
        list.add(new File("/Users/Shared/README.txt"));
        list.add(new ClassPathResource("/download/image.jpg"));
        list.add("http://127.0.0.1:8080/concept-download/video.mp4");
        return list;
    }

    @Download(filename = "压缩包15.zip")
    @CompressCache(group = "s16", name = "s16.zip", delete = true)
    @GetMapping("/s16")
    public List<Object> s16() {
        return s15();
    }

    @Download(filename = "压缩包17.zip")
    @SourceCache(group = "s17")
    @CompressCache(group = "s17", name = "s17.zip")
    @GetMapping("/s17")
    public String[] s17() {
        return new String[]{
                "http://127.0.0.1:8080/concept-download/text.txt",
                "http://127.0.0.1:8080/concept-download/image.jpg",
                "http://127.0.0.1:8080/concept-download/video.mp4"
        };
    }

    @Download(source = "classpath:/download/README.txt")
    @GetMapping("/s18")
    public DownloadOptions.Rewriter s18() {
        return new DownloadOptions.Rewriter() {
            @Override
            public DownloadOptions rewrite(DownloadOptions options) {
                System.out.println("在这里可以修改本次下载的参数！");
                return options.toBuilder()
                        .interceptor(new StandardDownloadHandlerInterceptor() {

                            @Override
                            public void onResponseWritten(DownloadContext context) {
                                System.out.println("在这里可以拦截每个步骤！");
                            }
                        })
                        .build();
            }
        };
    }

    @Download
    @GetMapping("/s19")
    public File s19() {
        return new File("/Users/Shared");
    }

    @Download(source = "classpath:/download/text.txt", inline = true, contentType = "text/plain")
    @GetMapping("/text.txt")
    public void readme() {
    }

    @Download(source = "classpath:/download/image.jpg", inline = true, contentType = "image/jpeg")
    @GetMapping("/image.jpg")
    public void image() {
    }

    @Download(source = "classpath:/download/video.mp4", inline = true, contentType = "video/mpeg4")
    @GetMapping("/video.mp4")
    public void video() {
    }
}
