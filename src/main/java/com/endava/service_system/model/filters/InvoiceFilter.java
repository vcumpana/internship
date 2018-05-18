package com.endava.service_system.model.filters;

import com.endava.service_system.model.filters.order.InvoiceOrderBy;
import com.endava.service_system.model.enums.InvoiceStatus;
import com.endava.service_system.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceFilter {
    private String currentUserUsername;
    private Long companyId;
    private InvoiceStatus invoiceStatus;
    private String categoryName;
    private Long categoryId;
    private Long contractId;
    private Integer size;
    private Integer page;
    private Sort.Direction orderDirection;
    private InvoiceOrderBy orderBy;
    @NotNull
    private UserType userType;
    private LocalDate fromStartDate;
    private LocalDate tillStartDate;
    private LocalDate fromTillDate;
    private LocalDate tillTillDate;
    private LocalDate fromDueDate;
    private LocalDate tillDueDate;

    //Only For User
    private String companyTitle;

    //Only For Company
    private String usersFirstName;
    private String usersLastName;
    private Long serviceId;


}
