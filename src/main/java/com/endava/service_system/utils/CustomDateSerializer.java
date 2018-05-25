package com.endava.service_system.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateSerializer extends StdSerializer<LocalDateTime> {
    private static DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CustomDateSerializer(){
        super(LocalDateTime.class);
    }

    protected CustomDateSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    protected CustomDateSerializer(JavaType type) {
        super(type);
    }

    protected CustomDateSerializer(Class<LocalDateTime> t, boolean dummy) {
        super(t, dummy);
    }

    protected CustomDateSerializer(StdSerializer<?> src) {
        super(src);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(formatter.format(value));
    }
}
