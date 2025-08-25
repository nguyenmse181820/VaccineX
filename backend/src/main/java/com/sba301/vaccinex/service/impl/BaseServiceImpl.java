package com.sba301.vaccinex.service.impl;

import com.sba301.vaccinex.dto.internal.PagingResponse;
import com.sba301.vaccinex.service.spec.BaseService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    private final JpaRepository<T, ID> repository;

    public BaseServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public PagingResponse findAll(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);

        var pageData = repository.findAll(pageable);

        return PagingResponse.builder()
                .currentPage(currentPage)
                .pageSize(pageSize)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent())
                .build();
    }

    @Override
    public T findById(ID id) {
        return this.repository.findById(id).orElse(null);
    }

    @Override
    public T save(T entity) {
        return this.repository.save(entity);
    }

}

