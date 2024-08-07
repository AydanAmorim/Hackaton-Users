package com.payment.system.authentication_service.util.pagination;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PagedResponse<T> {

    private Pagination page;

    private List<T> data;

    public PagedResponse(Integer page, Integer size, Integer totalOfPages, List<T> data) {
        this.page = new Pagination(page, size, totalOfPages);
        this.data = data;
    }
}
