package com.endava.service_system.converter;

import com.endava.service_system.model.dto.NormalTransaction;
import com.endava.service_system.model.dto.ShortTransaction;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ShortTransactionToNormalTransactionConverter implements Converter<ShortTransaction,NormalTransaction> {
    @Override
    public NormalTransaction convert(ShortTransaction source) {
        NormalTransaction nr=new NormalTransaction();
        nr.setCorrespondentCount(source.getC());
        nr.setDate(source.getD());
        nr.setDescription(source.getDr());
        nr.setMainCount(source.getM());
        nr.setSum(source.getS());
        return nr;
    }
}
