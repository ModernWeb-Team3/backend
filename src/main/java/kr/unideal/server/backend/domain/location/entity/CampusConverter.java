package kr.unideal.server.backend.domain.location.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CampusConverter implements AttributeConverter<Campus, String> {

    @Override
    public String convertToDatabaseColumn(Campus attribute) {
        return attribute != null ? attribute.getDescription() : null;
    }

    @Override
    public Campus convertToEntityAttribute(String dbData) {
        return dbData != null ? Campus.fromDescription(dbData) : null;
    }
}
