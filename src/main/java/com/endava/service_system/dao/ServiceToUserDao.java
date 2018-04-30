package com.endava.service_system.dao;

import com.endava.service_system.dto.ServiceToUserDto;
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
public class ServiceToUserDao {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ConversionService conversionService;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public List<ServiceToUserDto> getAllServices(ServiceDtoFilter filter) {
        String hql = createQueryForSearch(filter);
        System.out.println("hql:"+hql);
        Query query = entityManager.createQuery(hql);
        setParamsForFilter(query, filter);
        List<ServiceToUserDto> result = (List<ServiceToUserDto>) query.getResultList().stream().map(ob -> conversionService.convert(ob, ServiceToUserDto.class)).collect(Collectors.toList());
        return result;
    }

    private String createQueryForSearch(ServiceDtoFilter filter) {
        StringBuilder builder = new StringBuilder("SELECT c.name as name,s.id,s.title,cat.name,s.description,s.price FROM Company c INNER JOIN c.services s INNER JOIN s.category cat ");
        System.out.println(filter);
        boolean needAnd = false;
        if (filter.getSize() == null) {
            filter.setSize(DEFAULT_PAGE_SIZE);
        }
        if (filter.getMin() != null || filter.getCategoryId() != null ||
                filter.getMax() != null || filter.getDirection() != null ||
                filter.getCompanyId() != null ||filter.getCategoryName()!=null||filter.getCompanyName()!=null) {
            builder.append("WHERE");
        }
        if (filter.getMin() != null) {
            builder.append(" s.price>=:min ");
            needAnd = true;
        }
        if (filter.getMax() != null) {
            appendIf(builder," AND ",needAnd);
            builder.append(" s.price<=:max ");
            needAnd = true;
        }
        if (filter.getCategoryId() != null) {
            appendIf(builder," AND ",needAnd);
            builder.append(" cat.id=:catId ");
            needAnd = true;
        }

        if (filter.getCompanyId() != null) {
            appendIf(builder," AND ",needAnd);
            builder.append(" c.id=:companyId");
            needAnd = true;
        }

        if (filter.getCompanyName() != null) {
            appendIf(builder," AND ",needAnd);
            builder.append(" c.name=:companyName");
            needAnd = true;
        }

        if (filter.getCategoryName() != null) {
            appendIf(builder," AND ",needAnd);
            builder.append(" cat.name=:categoryName");
        }

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

    private void appendIf(StringBuilder body,String appendPart,boolean condition){
        if(condition){
            body.append(appendPart);
        }
    }

    private void setParamsForFilter(Query query, ServiceDtoFilter filter) {
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

        if (filter.getPage() != null)
            query.setFirstResult((filter.getPage() - 1) * filter.getSize());

        if (filter.getCompanyId()!=null)
            query.setParameter("companyId",filter.getCompanyId());

        query.setMaxResults(filter.getSize());
    }
}
