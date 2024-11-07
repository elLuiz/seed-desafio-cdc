package br.com.elibrary.application.dto.list;

import java.util.Collection;

public record ListResponse<T>(Collection<T> data) { }