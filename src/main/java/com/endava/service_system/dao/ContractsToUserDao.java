package com.endava.service_system.dao;

import com.endava.service_system.dto.ContractToUserDto;
import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.model.ContractForUserDtoFilter;
import com.endava.service_system.model.ServiceDtoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ContractsToUserDao {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ConversionService conversionService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<ContractToUserDto> getUserContracts(ContractForUserDtoFilter filter) {
        if (filter.getSize() == null) {
            filter.setSize(DEFAULT_PAGE_SIZE);
        }
        String hql = createQueryForSearch(filter);

        System.out.println("hql:"+hql);
        Query query = entityManager.createQuery(hql);
        setParamsForFilter(query, filter);
        List<ContractToUserDto> result = (List<ContractToUserDto>) query.getResultList().stream().map(ob -> conversionService.convert(ob, ContractToUserDto.class)).collect(Collectors.toList());
        return result;
    }

    private String createQueryForSearch(ContractForUserDtoFilter filter) {
        StringBuilder builder = new StringBuilder("SELECT cont.id,comp.name,s.title,cat.name,s.price,cont.startDate,cont.endDate,cont.status FROM Contract cont INNER JOIN cont.user u INNER JOIN u.credential credent INNER JOIN cont.service s INNER JOIN s.category cat INNER JOIN cont.company comp ");
        System.out.println(filter);

        builder.append(" WHERE credent.username=:username ");

        if(filter.getContractStatus()!=null){
            builder.append(" AND cont.status=:contractStatus ");
        }

        if (filter.getCompanyName() != null) {
            builder.append(" AND comp.name=:companyName ");
        }else if(filter.getCompanyId()!=null){
            builder.append(" AND comp.id=:companyId ");
        }

        if (filter.getCategoryName() != null) {
            builder.append(" AND cat.name=:categoryName ");
        }else if(filter.getCategoryId() != null) {
            builder.append(" AND cat.id=:categoryId ");
        }

        if (filter.getDirection() != null) {
            builder.append(" ORDER BY cont.endDate ");
            if (filter.getDirection() == Sort.Direction.ASC) {
                builder.append(" ASC ");
            } else {
                builder.append(" DESC ");
            }
        }
        return builder.toString();
    }

    private void setParamsForFilter(Query query, ContractForUserDtoFilter filter) {

        query.setParameter("username",filter.getUsername());

        if (filter.getCategoryName() != null) {
            query.setParameter("categoryName", filter.getCategoryName());
        }else if (filter.getCategoryId() != null)
            query.setParameter("categoryId", filter.getCategoryId());

        if (filter.getCompanyName() != null) {
            query.setParameter("companyName", filter.getCompanyName());
        }else if (filter.getCompanyId()!=null) {
            query.setParameter("companyId", filter.getCompanyId());
        }

        if (filter.getContractStatus() != null)
            query.setParameter("contractStatus", filter.getContractStatus());


        if (filter.getPage() != null)
            query.setFirstResult((filter.getPage() - 1) * filter.getSize());
        query.setMaxResults(filter.getSize());
    }
}
