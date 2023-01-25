package org.example.wrapped;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

class XmlElementWrapperDeserializerModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription description, JsonDeserializer<?> deserializer) {
        if (deserializer instanceof BeanDeserializer) {
            XmlElementWrapperDeserializer wrapped = new XmlElementWrapperDeserializer((BeanDeserializer) deserializer);
            if (wrapped.alive) {
                deserializer = wrapped.resolve();
            }
        }
        return deserializer;
    }
}
