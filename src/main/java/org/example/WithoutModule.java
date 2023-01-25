package org.example;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.example.wrapped.XmlElementWrapperModule;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class WithoutModule {
    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper()
                //.registerModule(new XmlElementWrapperModule())
                .registerModule(new JaxbAnnotationModule());
        String jsonWithout = objectMapper.writeValueAsString(new WithoutXewPlugin());
        String jsonWith = objectMapper.writeValueAsString(new WithXewPlugin());
        System.out.println(jsonWith);
        System.out.println(jsonWithout);
        // Will fail with exception, add XmlElementWrapperModule to fix!
        //System.out.println(objectMapper.readValue(jsonWith, WithoutXewPlugin.class).getValues().getValue());
        //System.out.println(objectMapper.readValue(jsonWithout, WithXewPlugin.class).getValues());

        StringWriter xmlWithout = new StringWriter();
        JAXBContext.newInstance(WithoutXewPlugin.class).createMarshaller().marshal(new JAXBElement<>(
                new QName("element"),
                WithoutXewPlugin.class,
                new WithoutXewPlugin()
        ), xmlWithout);
        System.out.println(xmlWithout);
        StringWriter xmlWith = new StringWriter();
        JAXBContext.newInstance(WithXewPlugin.class).createMarshaller().marshal(new JAXBElement<>(
                new QName("element"),
                WithXewPlugin.class,
                new WithXewPlugin()
        ), xmlWith);
        System.out.println(xmlWith);
        System.out.println(JAXBContext.newInstance(WithoutXewPlugin.class).createUnmarshaller().unmarshal(
                new StreamSource(new StringReader(xmlWith.toString())),
                WithoutXewPlugin.class
        ).getValue().getValues().getValue());
        System.out.println(JAXBContext.newInstance(WithXewPlugin.class).createUnmarshaller().unmarshal(
                new StreamSource(new StringReader(xmlWithout.toString())),
                WithXewPlugin.class
        ).getValue().getValues());
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class WithoutXewPlugin {

        @XmlElement(name = "values")
        private Values values = new Values();

        public void setValues(Values values) {
            this.values = values;
        }

        public Values getValues() {
            return values;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Values {

        @XmlElement(name = "value")
        private final List<String> value = new ArrayList<>();

        public Values() {
            value.add("foo");
            value.add("bar");
        }

        public List<String> getValue() {
            return value;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class WithXewPlugin {

        @XmlElement(name = "value")
        @XmlElementWrapper(name = "values")
        private final List<String> values = new ArrayList<>();

        public WithXewPlugin() {
            values.add("foo");
            values.add("bar");
        }

        public List<String> getValues() {
            return values;
        }
    }
}