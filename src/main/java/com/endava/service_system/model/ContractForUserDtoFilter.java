package com.endava.service_system.model;

import com.endava.service_system.enums.ContractStatus;
import com.endava.service_system.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ContractForUserDtoFilter {
    private String username;
    private Integer page;
    private Integer size;
    private Long categoryId;
    //Just For Users;
    private Long companyId;
    private Sort.Direction direction;
    private String categoryName;
    //Just For Users;
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

}
