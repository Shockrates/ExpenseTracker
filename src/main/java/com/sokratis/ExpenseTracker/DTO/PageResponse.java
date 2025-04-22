package com.sokratis.ExpenseTracker.DTO;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private int numberOfElements;
    private boolean last;

    public PageResponse(Page<T> page){
       this.content  = page.getContent();
       this.pageNumber = page.getNumber();
       this.pageSize = page.getSize();
       this.totalElements = page.getTotalElements();
       this.totalPages = page.getTotalPages();
       this.numberOfElements = page.getNumberOfElements();
       this.last = page.isLast();
    }
}
