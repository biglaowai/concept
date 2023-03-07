package com.github.linyuzai.concept.sample.cloud.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("cloud-web")
public class CloudWebController {

    @GetMapping("/test-void")
    public void testVoid() {
        System.out.println(1);
    }

    @GetMapping("/test-map")
    public Map<String, String> testMap() {
        Map<String, String> map = new HashMap<>();
        map.put("x", "y");
        return map;
    }

    @GetMapping("/test-string")
    public String testString() {
        return "z";
    }

    @GetMapping("/test-error")
    public void testError() {
        throw new RuntimeException("error");
    }
}
