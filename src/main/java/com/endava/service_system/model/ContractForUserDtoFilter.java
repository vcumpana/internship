package com.endava.service_system.model;

import com.endava.service_system.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ContractForUserDtoFilter {
    private String username;
    private Integer page;
    private Integer size;
    private Long categoryId;
    private Long companyId;
    private Sort.Direction direction;
    private String categoryName;
    private String companyName;
    private ContractStatus contractStatus;

}
