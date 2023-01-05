package aliaksandrkryvapust.reportmicroservice.config;


import aliaksandrkryvapust.reportmicroservice.controller.validation.StringToTypeConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .featuresToEnable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToEnable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .modules(new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).build();
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
    }

    @Override
    public void addFormatters(FormatterRegistry registry){
        registry.addConverter(new StringToTypeConverter());
    }
}