package com.github.linyuzai.plugin.core.concept;

public interface Plugin {

    String PREFIX = "CONCEPT_PLUGIN@";

    String getId();

    void initialize();

    void destroy();
}