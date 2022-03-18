package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

public class InstanceSetMatcher extends InstanceMatcher {

    private final Class<?> setClass;

    public InstanceSetMatcher(Class<?> setClass, Class<?> target, Annotation[] annotations) {
        super(target, annotations);
        this.setClass = setClass;
    }

    @Override
    public Object convert(Map<String, Object> map) {
        Set<Object> set = ReflectionUtils.newSet(setClass);
        set.addAll(map.values());
        return set;
    }
}
