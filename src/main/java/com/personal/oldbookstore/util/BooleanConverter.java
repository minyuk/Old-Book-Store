package com.personal.oldbookstore.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if(attribute != null && attribute){
            return "TRUE";
        }
        return "FALSE";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if("TRUE".equalsIgnoreCase(dbData)){
            return true;
        }
        return false;
    }
}
