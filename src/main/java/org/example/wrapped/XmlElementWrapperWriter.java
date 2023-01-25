package org.example.wrapped;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
import com.fasterxml.jackson.databind.util.Annotations;

class XmlElementWrapperWriter extends VirtualBeanPropertyWriter {

    private final BeanSerializer serializer;
    private boolean resolved;

    XmlElementWrapperWriter(BeanPropertyDefinition propDef,
                            Annotations contextAnnotations,
                            JavaType declaredType,
                            BeanSerializer serializer) {

        super(propDef, contextAnnotations, declaredType);
        this.serializer = serializer;
    }

    @Override
    public void serializeAsField(Object value, JsonGenerator generator, SerializerProvider provider) throws Exception {
        generator.writeFieldName(_name);
        serializeAsElement(value, generator, provider);
    }

    @Override
    public void serializeAsElement(Object value, JsonGenerator generator, SerializerProvider provider) throws Exception {
        if (!resolved) {
            serializer.resolve(provider);
            resolved = true;
        }
        serializer.serialize(value, generator, provider);
    }

    @Override
    protected Object value(Object value, JsonGenerator generator, SerializerProvider provider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public VirtualBeanPropertyWriter withConfig(MapperConfig<?> config, AnnotatedClass declaringClass, BeanPropertyDefinition definition, JavaType type) {
        throw new UnsupportedOperationException();
    }
}
