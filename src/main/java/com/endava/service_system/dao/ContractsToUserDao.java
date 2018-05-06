package com.endava.service_system.dao;

import com.endava.service_system.dto.ContractForShowingDto;
import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.enums.UserType;
import com.endava.service_system.model.ContractForUserDtoFilter;
import com.endava.service_system.model.InvoiceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ContractsToUserDao {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ConversionService conversionService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<ContractForShowingDto> getContracts(ContractForUserDtoFilter filter) {
        if (filter.getSize() == null) {
            filter.setSize(DEFAULT_PAGE_SIZE);
        }
        String hql = createQueryForSearch(filter);

        System.out.println("hql:"+hql);
        Query query = entityManager.createQuery(hql);
        setParamsForFilter(query, filter);
        List<ContractForShowingDto> result = (List<ContractForShowingDto>) query.getResultList().stream()
                .map(ob -> conversionService.convert(ob, ContractForShowingDto.class))
                .collect(Collectors.toList());
        return result;
    }

    public Long getPagesSizeForFilter(ContractForUserDtoFilter filter){
        if (filter.getSize() == null||filter.getSize()<=0) {
            filter.setSize(DEFAULT_PAGE_SIZE);
        }
        String hql = getPagesSql(filter);
        System.out.println("hql:"+hql);
        Query query = entityManager.createQuery(hql);
        setParamsForFilterWithoutLimit(query, filter);
        Long totalNrOfInvoices= (Long)query.getSingleResult();
        Long number=totalNrOfInvoices/filter.getSize();
        if(totalNrOfInvoices%filter.getSize()!=0){
            number++;
        }
        return number;
    }

    private String getPagesSql(ContractForUserDtoFilter filter) {
        StringBuilder builder=new StringBuilder(" SELECT COUNT(cont) ");
        builder.append(getSqlWithoutOrder(filter));
        return builder.toString();
    }

    private String getSqlWithoutOrder(ContractForUserDtoFilter filter){
        StringBuilder builder=new StringBuilder(" FROM Contract cont INNER JOIN cont.user u INNER JOIN cont.company comp ");

        if(filter.getUserType()== UserType.USER){
            builder.append(" INNER JOIN u.credential credent ");
        }else{
            builder.append(" INNER JOIN comp.credential credent ");
        }

        builder.append(" INNER JOIN cont.service s INNER JOIN s.category cat");
        System.out.println(filter);

        builder.append(" WHERE credent.username=:username ");



        if(filter.getContractStatus()!=null){
            builder.append(" AND cont.status=:contractStatus ");
        }
        if(filter.getUserType()==UserType.USER) {
            if (filter.getCompanyName() != null) {
                builder.append(" AND comp.name=:companyName ");
            } else if (filter.getCompanyId() != null) {
                builder.append(" AND comp.id=:companyId ");
            }
        }

        if (filter.getCategoryName() != null) {
            builder.append(" AND cat.name=:categoryName ");
        }else if(filter.getCategoryId() != null) {
            builder.append(" AND cat.id=:categoryId ");
        }
        return builder.toString();
    }

    private String createQueryForSearch(ContractForUserDtoFilter filter) {
        StringBuilder builder = new StringBuilder("SELECT cont.id,comp.name,s.title,cat.name,s.price,cont.startDate,cont.endDate,cont.status,concat(u.name,' ',u.surname) ");
        builder.append(getSqlWithoutOrder(filter));

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
        setParamsForFilterWithoutLimit(query,filter);
        if (filter.getPage() != null)
            query.setFirstResult((filter.getPage() - 1) * filter.getSize());
        query.setMaxResults(filter.getSize());
    }

    private void setParamsForFilterWithoutLimit(Query query, ContractForUserDtoFilter filter) {

        query.setParameter("username",filter.getUsername());

        if (filter.getCategoryName() != null) {
            query.setParameter("categoryName", filter.getCategoryName());
        }else if (filter.getCategoryId() != null)
            query.setParameter("categoryId", filter.getCategoryId());

        if(filter.getUserType()==UserType.USER) {
            if (filter.getCompanyName() != null) {
                query.setParameter("companyName", filter.getCompanyName());
            } else if (filter.getCompanyId() != null) {
                query.setParameter("companyId", filter.getCompanyId());
            }
        }

        if (filter.getContractStatus() != null)
            query.setParameter("contractStatus", filter.getContractStatus());
    }
}
