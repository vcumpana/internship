package com.endava.service_system.model.filters;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import com.endava.service_system.model.enums.*;
import  com.endava.service_system.model.filters.order.ContractOrderBy;
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
    private Sort.Direction order;
    private String categoryName;
    private String companyName;
    private ContractStatus contractStatus;
    private UserType userType;
    private LocalDate fromStartDate;
    private LocalDate tillStartDate;
    private LocalDate fromEndDate;
    private LocalDate tillEndDate;
    //Just For Companies
    private String usersFirstName;
    private String usersLastName;
    private Long serviceId;

    private ContractOrderBy orderBy;

}
