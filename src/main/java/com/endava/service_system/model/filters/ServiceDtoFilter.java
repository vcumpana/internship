package com.endava.service_system.model.filters;

import com.endava.service_system.model.filters.order.ServiceOrderBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ServiceDtoFilter {
    private Integer page;
    private Integer size;
    private Integer min;
    private Integer max;
    private Long categoryId;
    private Long companyId;
    private Sort.Direction order;
    private String categoryName;
    private String companyName;
    private ServiceOrderBy orderBy;

}
