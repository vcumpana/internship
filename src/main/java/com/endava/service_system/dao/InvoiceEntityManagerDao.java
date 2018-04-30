package com.endava.service_system.dao;

import com.endava.service_system.dto.ContractToUserDto;
import com.endava.service_system.dto.InvoiceDisplayDto;
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
public class InvoiceEntityManagerDao {
    @PersistenceContext
    private EntityManager entityManager;
    private ConversionService conversionService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<InvoiceDisplayDto> getAllInvoices(InvoiceFilter filter){
        if (filter.getSize() == null) {
            filter.setSize(DEFAULT_PAGE_SIZE);
        }
        String hql = createQueryForSearch(filter);

        System.out.println("hql:"+hql);
        Query query = entityManager.createQuery(hql);
        setParamsForFilter(query, filter);
        List<Object[]> list=query.getResultList();
        List<InvoiceDisplayDto> result = list.stream().map(ob -> conversionService.convert(ob, InvoiceDisplayDto.class)).collect(Collectors.toList());
        return result;

    }

    private String createQueryForSearch(InvoiceFilter filter) {
        StringBuilder builder = new StringBuilder("SELECT concat(user.name, ' ', user.surname),company.name,invoice.id," +
                " invoice.price,invoice.invoiceStatus,service.title," +
                " invoice.dueDate,invoice.fromDate,invoice.tillDate" +
                " FROM Invoice invoice INNER JOIN invoice.contract contract " +
                " INNER JOIN contract.service service INNER JOIN contract.user user " +
                " INNER JOIN service.category category INNER JOIN "+
                " contract.company company ");
        if(filter.getUserType()== UserType.COMPANY){
            builder.append(" INNER JOIN company.credential credential ");
        }else{
            builder.append(" INNER JOIN user.credential credential ");
        }
        System.out.println(filter);

        builder.append(" WHERE credential.username=:username ");

        if(filter.getOrderByDueDateDirection()==null&&filter.getInvoiceStatus()!=null){
            builder.append(" AND invoice.invoiceStatus=:invoiceStatus ");
        }

        if(filter.getUserType()== UserType.USER) {
            if (filter.getCompanyTitle() != null) {
                builder.append(" AND company.name=:companyName ");
            } else if (filter.getCompanyId() != null) {
                builder.append(" AND company.id=:companyId ");
            }
        }
//
        if (filter.getCategoryName() != null) {
            builder.append(" AND category.name=:categoryName ");
        }else if(filter.getCategoryId() != null) {
            builder.append(" AND category.id=:categoryId ");
        }
//
        if (filter.getOrderByDueDateDirection() != null) {
            builder.append(" AND invoice.invoiceStatus='SENT' ");
            builder.append(" ORDER BY invoice.dueDate");
            if (filter.getOrderByDueDateDirection()== Sort.Direction.ASC) {
                builder.append(" ASC ");
            } else {
                builder.append(" DESC ");
            }
        }
        return builder.toString();
    }

    private void setParamsForFilter(Query query, InvoiceFilter filter) {

        query.setParameter("username",filter.getCurrentUserUsername());
        if(filter.getOrderByDueDateDirection()==null&&filter.getInvoiceStatus()!=null){
            query.setParameter("invoiceStatus",filter.getInvoiceStatus());
        }

        if(filter.getUserType()== UserType.USER) {
            if (filter.getCompanyTitle() != null) {
                query.setParameter("companyName",filter.getCompanyTitle());
            } else if (filter.getCompanyId() != null) {
                query.setParameter("companyId",filter.getCompanyId());
            }
        }
//
        if (filter.getCategoryName() != null) {
            query.setParameter("categoryName",filter.getCategoryName());
        }else if(filter.getCategoryId() != null) {
            query.setParameter("categoryId",filter.getCategoryId());
        }



        if (filter.getPage() != null)
            query.setFirstResult((filter.getPage() - 1) * filter.getSize());
        query.setMaxResults(filter.getSize());
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
