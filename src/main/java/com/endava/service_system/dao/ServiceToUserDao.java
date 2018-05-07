package com.endava.service_system.dao;

import com.endava.service_system.dto.ServiceToUserDto;
import com.endava.service_system.model.ServiceDtoFilter;
import com.sun.xml.internal.ws.util.ServiceFinder;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ServiceToUserDao {
    private static final Logger LOGGER= LogManager.getLogger(ServiceToUserDao.class);
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ConversionService conversionService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<ServiceToUserDto> getAllServices(ServiceDtoFilter filter) {
        String hql = createQueryForSearch(filter);
        LOGGER.log(Level.DEBUG,"hql:" + hql);
        Query query = entityManager.createQuery(hql);
        setParamsForFilter(query, filter);
        List<ServiceToUserDto> result = (List<ServiceToUserDto>) query.getResultList().stream().map(ob -> conversionService.convert(ob, ServiceToUserDto.class)).collect(Collectors.toList());
        LOGGER.log(Level.DEBUG,result.size());
        return result;
    }

    private String getCommonSql(ServiceDtoFilter filter){
        StringBuilder builder=new StringBuilder("FROM Company c INNER JOIN c.services s INNER JOIN s.category cat ");
        LOGGER.log(Level.DEBUG,filter);
        boolean needAnd = false;
        if (filter.getSize() == null||filter.getSize()<=0) {
            filter.setSize(DEFAULT_PAGE_SIZE);
        }
        if(filter.getPage()==null||filter.getPage()==0){
            filter.setPage(1);
        }
        if (filter.getMin() != null || filter.getCategoryId() != null ||
                filter.getMax() != null ||
                filter.getCompanyId() != null || filter.getCategoryName() != null || filter.getCompanyName() != null) {
            builder.append("WHERE");
        }
        if (filter.getMin() != null) {
            builder.append(" s.price>=:min ");
            needAnd = true;
        }
        if (filter.getMax() != null) {
            appendIf(builder, " AND ", needAnd);
            builder.append(" s.price<=:max ");
            needAnd = true;
        }
        if (filter.getCategoryId() != null) {
            appendIf(builder, " AND ", needAnd);
            builder.append(" cat.id=:catId ");
            needAnd = true;
        }

        if (filter.getCompanyId() != null) {
            appendIf(builder, " AND ", needAnd);
            builder.append(" c.id=:companyId");
            needAnd = true;
        }

        if (filter.getCompanyName() != null) {
            appendIf(builder, " AND ", needAnd);
            builder.append(" c.name=:companyName");
            needAnd = true;
        }

        if (filter.getCategoryName() != null) {
            appendIf(builder, " AND ", needAnd);
            builder.append(" cat.name=:categoryName");
        }
        return builder.toString();
    }
    
    private String createQueryForSearch(ServiceDtoFilter filter) {
        StringBuilder builder = new StringBuilder("SELECT c.name as name,s.id,s.title,cat.name,s.description,s.price ");
        builder.append(getCommonSql(filter));
        if (filter.getDirection() != null) {
            builder.append(" ORDER BY s.price ");
            if (filter.getDirection() == Sort.Direction.ASC) {
                builder.append(" ASC ");
            } else {
                builder.append(" DESC ");
            }
        }
        return builder.toString();
    }

    private void appendIf(StringBuilder body, String appendPart, boolean condition) {
        if (condition) {
            body.append(appendPart);
        }
    }

    private void setParamsForFilter(Query query, ServiceDtoFilter filter) {
        setParamsWithoutLimit(query,filter);
        if (filter.getPage() != null)
            query.setFirstResult((filter.getPage() - 1) * filter.getSize());
        query.setMaxResults(filter.getSize());
    }

    private void setParamsWithoutLimit(Query query,ServiceDtoFilter filter){
        if (filter.getMin() != null)
            query.setParameter("min", new BigDecimal(filter.getMin()));

        if (filter.getMax() != null)
            query.setParameter("max", new BigDecimal(filter.getMax()));

        if (filter.getCategoryId() != null)
            query.setParameter("catId", filter.getCategoryId());

        if (filter.getCompanyName() != null)
            query.setParameter("companyName", filter.getCompanyName());

        if (filter.getCategoryName() != null)
            query.setParameter("categoryName", filter.getCategoryName());
        if (filter.getCompanyId() != null)
            query.setParameter("companyId", filter.getCompanyId());
    }

    public Long getPagesSize(ServiceDtoFilter filter) {
        String hql = getPagesSql(filter);
        LOGGER.log(Level.DEBUG,"hql:"+hql);
        Query query = entityManager.createQuery(hql);
        setParamsWithoutLimit(query, filter);
        Long totalNrOfInvoices= (Long)query.getSingleResult();
        Long number=totalNrOfInvoices/filter.getSize();
        if(totalNrOfInvoices%filter.getSize()!=0){
            number++;
        }
        return number;
    }

    private String getPagesSql(ServiceDtoFilter filter) {
        StringBuilder builder = new StringBuilder("SELECT count(s) ");
        builder.append(getCommonSql(filter));
        return builder.toString();
    }
}
