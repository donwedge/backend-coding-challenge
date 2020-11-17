package com.journi.challenge.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Using the logic explained by this article:
 * https://dzone.com/articles/dealing-with-javas-localdatetime-in-jpa
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime attribute) {
        return Optional.ofNullable(attribute)
                .map(Timestamp::valueOf)
                .orElse(null);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp attribute) {
        return Optional.ofNullable(attribute)
                .map(Timestamp::toLocalDateTime)
                .orElse(null);
    }
}
