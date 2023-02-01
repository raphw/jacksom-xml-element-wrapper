package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.example.wrapped.XmlElementWrapperModule;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

public class WithModule {

    public static void main(String[] args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new XmlElementWrapperModule())
                .registerModule(new JaxbAnnotationModule());
        String jsonWith = objectMapper.writeValueAsString(new WithXewPlugin());
        String jsonWithout = "{\"value\":[\"foo\",\"bar\"]}";
        System.out.println(jsonWith);
        System.out.println(jsonWithout);
        System.out.println(objectMapper.readValue(jsonWith, WithXewPlugin.class).getValue());
        System.out.println(objectMapper.readValue(jsonWithout, WithXewPlugin.class).getValue());
    }

    public static class WithXewPlugin {

        @XmlElement(name = "value")
        @XmlElementWrapper(name = "values")
        private final List<String> value = new ArrayList<>();

        public WithXewPlugin() {
            value.add("foo");
            value.add("bar");
        }

        public List<String> getValue() {
            return value;
        }
    }
}
