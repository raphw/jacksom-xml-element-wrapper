package org.example.wrapped;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

class XmlElementWrapperSerializerModifier extends BeanSerializerModifier {

    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription description, JsonSerializer<?> serializer) {
        if (serializer instanceof BeanSerializer) {
            XmlElementWrapperSerializer wrapped = new XmlElementWrapperSerializer((BeanSerializer) serializer, config, description);
            if (wrapped.alive) {
                serializer = wrapped;
            }
        }
        return serializer;
    }
}
