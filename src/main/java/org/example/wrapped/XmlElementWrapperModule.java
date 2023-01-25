package org.example.wrapped;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSchema;

public class XmlElementWrapperModule extends SimpleModule {

    private static final String DEFAULT = "##default";

    public XmlElementWrapperModule() {
        super(XmlElementWrapper.class.getName() + "Module");
        setSerializerModifier(new XmlElementWrapperSerializerModifier());
        setDeserializerModifier(new XmlElementWrapperDeserializerModifier());
    }

    static PropertyName wrapper(AnnotatedMember member) {
        XmlElementWrapper annotation = member.getAnnotation(XmlElementWrapper.class);
        if (annotation == null) {
            return null;
        }
        String namespace = null;
        if (annotation.namespace().equals(DEFAULT)) {
            Package location = member.getDeclaringClass().getPackage();
            if (location != null) {
                XmlSchema schema = location.getAnnotation(XmlSchema.class);
                if (schema != null && !schema.namespace().isEmpty()) {
                    namespace = schema.namespace();
                }
            }
        } else {
            namespace = annotation.namespace();
        }
        return new PropertyName(annotation.name().equals(XmlElementWrapperModule.DEFAULT)
                ? member.getName()
                : annotation.name(), namespace);
    }
}
