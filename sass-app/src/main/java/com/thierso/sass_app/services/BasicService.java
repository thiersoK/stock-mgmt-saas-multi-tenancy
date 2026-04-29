package com.thierso.sass_app.services;

import com.thierso.sass_app.common.PageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BasicService<I, O> {

    void create( final I request);

    void update(final String id, I request);

    PageResponse<O> findAll(final int page, final int size);

    O findById(final String id);

    void delete(final String id);
}
