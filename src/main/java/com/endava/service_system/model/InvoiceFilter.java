package com.endava.service_system.model;

import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceFilter {
    private String currentUserUsername;
    //complete only if userType == User
    private String companyTitle;
    private Integer companyId;
    private InvoiceStatus invoiceStatus;
    private String categoryName;
    private Integer categoryId;
    private Integer size;
    private Integer page;
    private Sort.Direction orderByDueDateDirection;
    @NotNull
    private UserType userType;

}
