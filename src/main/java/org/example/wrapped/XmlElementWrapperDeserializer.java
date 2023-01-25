package org.example.wrapped;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;

import java.io.IOException;

class XmlElementWrapperDeserializer extends BeanDeserializer {

    boolean alive;

    XmlElementWrapperDeserializer(BeanDeserializer source) {
        super(source);
        SettableBeanProperty[] properties = _beanProperties.getPropertiesInInsertionOrder();
        for (int index = 0; index < properties.length; index++) {
            PropertyName name = XmlElementWrapperModule.wrapper(properties[index].getMember());
            if (name != null) {
                alive = true;
                _beanProperties.replace(properties[index], properties[index].withName(name).withValueDeserializer(new ValueDeserializer(
                        properties[index].getName(),
                        properties[index].getValueDeserializer(),
                        properties[index].getType()
                )));
            }
        }
    }

    BeanDeserializerBase resolve() {
        return withBeanProperties(new BeanPropertyMap(
                _beanProperties,
                _beanProperties.isCaseInsensitive()
        ) {
        });
    }

    private static class ValueDeserializer extends JsonDeserializer<Object> {

        private final String name;

        private final JsonDeserializer<Object> delegate;

        private final JavaType type;

        private ValueDeserializer(String name, JsonDeserializer<Object> delegate, JavaType type) {
            this.name = name;
            this.delegate = delegate;
            this.type = type;
        }

        @Override
        public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
            if (!parser.nextFieldName().equals(name) || !parser.nextToken().equals(JsonToken.START_ARRAY)) {
                throw new JsonParseException(parser, "Expected array with name: " + name);
            }
            Object value = delegate == null
                    ? parser.getCodec().readValue(parser, type)
                    : delegate.deserialize(parser, context);
            if (!parser.nextToken().equals(JsonToken.END_OBJECT)) {
                throw new JsonParseException(parser, "Expected end of object for name: " + name);
            }
            return value;
        }
    }
}