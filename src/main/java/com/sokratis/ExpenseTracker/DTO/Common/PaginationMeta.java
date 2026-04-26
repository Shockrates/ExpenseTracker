package com.sokratis.ExpenseTracker.DTO.Common;

public record PaginationMeta(
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        int numberOfElements,
        boolean last) {
}