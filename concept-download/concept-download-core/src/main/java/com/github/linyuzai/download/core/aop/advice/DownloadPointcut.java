package com.github.linyuzai.download.core.aop.advice;

import com.github.linyuzai.download.core.aop.annotation.Download;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

/**
 * 方法上的 {@link Download} 注解作为切点。
 * <p>
 * Use annotation {@link Download} on method for pointcut.
 */
public class DownloadPointcut extends AnnotationMatchingPointcut {

    public DownloadPointcut() {
        super(null, Download.class, true);
    }
}
