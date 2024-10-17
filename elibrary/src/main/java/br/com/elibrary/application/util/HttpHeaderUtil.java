package br.com.elibrary.application.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class HttpHeaderUtil {
    private HttpHeaderUtil() {}

    public static URI getLocationURI(String path, Object ...parameters) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path(path)
                .buildAndExpand(parameters)
                .toUri();
    }
}