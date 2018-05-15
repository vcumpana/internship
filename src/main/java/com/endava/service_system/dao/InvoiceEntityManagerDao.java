package com.endava.service_system.dao;

import com.endava.service_system.dto.InvoiceDisplayDto;
import com.endava.service_system.enums.InvoiceStatus;
import com.endava.service_system.enums.UserType;
import com.endava.service_system.model.ContractForUserDtoFilter;
import com.endava.service_system.model.InvoiceFilter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = LogManager.getLogger(InvoiceEntityManagerDao.class);
    @PersistenceContext
    private EntityManager entityManager;
    private ConversionService conversionService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<InvoiceDisplayDto> getAllInvoices(InvoiceFilter filter) {
        String hql = createQueryForSearch(filter);
        LOGGER.log(Level.DEBUG, "hql:" + hql);
        Query query = entityManager.createQuery(hql);
        setParamsForFilter(query, filter);
        List<Object[]> list = query.getResultList();
        List<InvoiceDisplayDto> result = list.stream().map(ob -> conversionService.convert(ob, InvoiceDisplayDto.class)).collect(Collectors.toList());
        return result;
    }

    public Long getPagesSizeForFilter(InvoiceFilter filter) {
        String hql = getPagesSql(filter);
        LOGGER.log(Level.DEBUG, "hql:" + hql);
        Query query = entityManager.createQuery(hql);
        setParamsForFilterWithoutLimit(query, filter);
        Long totalNrOfInvoices = (Long) query.getSingleResult();
        Long number = totalNrOfInvoices / filter.getSize();
        if (totalNrOfInvoices % filter.getSize() != 0) {
            number++;
        }
        return number;
    }

    private void setParamsForFilterWithoutLimit(Query query, InvoiceFilter filter) {
        query.setParameter("username", filter.getCurrentUserUsername());

        if (filter.getUserType() == UserType.COMPANY) {
            if ( filter.getInvoiceStatus() != null) {
                query.setParameter("invoiceStatus", filter.getInvoiceStatus());
            }
            if(filter.getServiceId()!=null&&filter.getServiceId()>0){
                query.setParameter("serviceId", filter.getServiceId());
            }
        } else {
            if ( filter.getInvoiceStatus() != null && filter.getInvoiceStatus() != InvoiceStatus.CREATED) {
                query.setParameter("invoiceStatus", filter.getInvoiceStatus());
            } else {
                query.setParameter("invoiceStatus", InvoiceStatus.CREATED);
            }
        }

        if (filter.getUserType() == UserType.USER) {
            if (filter.getCompanyTitle() != null) {
                query.setParameter("companyName", filter.getCompanyTitle());
            } else if (filter.getCompanyId() != null) {
                query.setParameter("companyId", filter.getCompanyId());
            }
        }else{
            if(filter.getUsersFirstName()!=null&&!filter.getUsersFirstName().isEmpty()){
                query.setParameter("firstName", filter.getUsersFirstName());
            }
            if(filter.getUsersLastName()!=null&&!filter.getUsersLastName().isEmpty()){
                query.setParameter("lastName", filter.getUsersLastName());
            }
        }

        if(filter.getFromStartDate()!=null){
            query.setParameter("fromStartDate", filter.getFromStartDate());
        }

        if(filter.getTillStartDate()!=null){
            query.setParameter("tillStartDate", filter.getTillStartDate());
        }

        if(filter.getFromTillDate()!=null){
            query.setParameter("fromTillDate", filter.getFromTillDate());
        }

        if(filter.getTillTillDate()!=null){
            query.setParameter("tillTillDate", filter.getTillTillDate());
        }

        if(filter.getFromDueDate()!=null){
            query.setParameter("fromDueDate", filter.getFromDueDate());
        }

        if(filter.getTillDueDate()!=null){
            query.setParameter("tillDueDate", filter.getTillDueDate());
        }
//
        if (filter.getCategoryName() != null) {
            query.setParameter("categoryName", filter.getCategoryName());
        } else if (filter.getCategoryId() != null) {
            query.setParameter("categoryId", filter.getCategoryId());
        }

    }

    public String getPagesSql(InvoiceFilter filter) {
        StringBuilder builder = new StringBuilder("SELECT count(invoice) ");
        builder.append(getSqlWithoutOrder(filter));
        return builder.toString();
    }

    private String createQueryForSearch(InvoiceFilter filter) {
        StringBuilder builder = new StringBuilder("SELECT concat(user.name, ' ', user.surname),company.name,invoice.id,");
        builder.append(" invoice.price,invoice.invoiceStatus,service.title,");
        builder.append(" invoice.dueDate,invoice.fromDate,invoice.tillDate,contract.id");
        builder.append(getSqlWithoutOrder(filter));
        builder.append(getOrderSqlExtension(filter));
        return builder.toString();
    }

    private String getOrderSqlExtension(InvoiceFilter filter) {
        StringBuilder builder = new StringBuilder();
        if (filter.getOrderByDueDateDirection() != null) {
            builder.append(" ORDER BY invoice.dueDate");
            if (filter.getOrderByDueDateDirection() == Sort.Direction.ASC) {
                builder.append(" ASC ");
            } else {
                builder.append(" DESC ");
            }
        }
        return builder.toString();
    }

    private String getSqlWithoutOrder(InvoiceFilter filter) {
        if (filter.getSize() == null || filter.getSize() <= 0) {
            filter.setSize(DEFAULT_PAGE_SIZE);
        }
        if (filter.getPage() == null || filter.getPage() == 0) {
            filter.setPage(1);
        }
        StringBuilder builder = new StringBuilder(" FROM Invoice invoice INNER JOIN invoice.contract contract " +
                " INNER JOIN contract.service service INNER JOIN contract.user user " +
                " INNER JOIN service.category category INNER JOIN " +
                " contract.company company ");
        if (filter.getUserType() == UserType.COMPANY) {
            builder.append(" INNER JOIN company.credential credential ");
        } else {
            builder.append(" INNER JOIN user.credential credential ");
        }

        builder.append(" WHERE credential.username=:username ");

        if (filter.getUserType() == UserType.COMPANY) {
            if (filter.getInvoiceStatus() != null) {
                builder.append(" AND invoice.invoiceStatus=:invoiceStatus ");
            }
            if(filter.getServiceId()!=null&&filter.getServiceId()>0){
                builder.append(" AND service.id=:serviceId ");
            }
        } else {
            if (filter.getInvoiceStatus() != null && filter.getInvoiceStatus() != InvoiceStatus.CREATED) {
                builder.append(" AND invoice.invoiceStatus=:invoiceStatus ");
            } else {
                builder.append(" AND invoice.invoiceStatus!=:invoiceStatus ");
            }
        }

        if (filter.getUserType() == UserType.USER) {
            if (filter.getCompanyTitle() != null) {
                builder.append(" AND company.name=:companyName ");
            } else if (filter.getCompanyId() != null) {
                builder.append(" AND company.id=:companyId ");
            }
        }else{
            if(filter.getUsersFirstName()!=null&&!filter.getUsersFirstName().isEmpty()){
                builder.append(" AND lower(user.name) LIKE lower(CONCAT(:firstName,'%')) ");
            }
            if(filter.getUsersLastName()!=null&&!filter.getUsersLastName().isEmpty()){
                builder.append(" AND lower(user.surname) LIKE lower(CONCAT(:lastName,'%')) ");
            }
        }

        if(filter.getFromStartDate()!=null){
            builder.append(" AND invoice.fromDate>=:fromStartDate ");
        }

        if(filter.getTillStartDate()!=null){
            builder.append(" AND invoice.fromDate<=:tillStartDate ");
        }

        if(filter.getFromTillDate()!=null){
            builder.append(" AND invoice.tillDate>=:fromTillDate ");
        }

        if(filter.getTillTillDate()!=null){
            builder.append(" AND invoice.tillDate<=:tillTillDate ");
        }

        if(filter.getFromDueDate()!=null){
            builder.append(" AND invoice.dueDate>=:fromDueDate ");
        }

        if(filter.getTillDueDate()!=null){
            builder.append(" AND invoice.dueDate<=:tillDueDate ");
        }
//
        if (filter.getCategoryName() != null) {
            builder.append(" AND category.name=:categoryName ");
        } else if (filter.getCategoryId() != null) {
            builder.append(" AND category.id=:categoryId ");
        }
//
        return builder.toString();
    }

    private void setParamsForFilter(Query query, InvoiceFilter filter) {
        setParamsForFilterWithoutLimit(query, filter);
        if (filter.getPage() != null)
            query.setFirstResult((filter.getPage() - 1) * filter.getSize());
        query.setMaxResults(filter.getSize());
    }

    @Autowired
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
