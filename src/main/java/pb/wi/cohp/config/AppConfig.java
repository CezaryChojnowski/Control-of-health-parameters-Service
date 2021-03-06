package pb.wi.cohp.config;

import io.swagger.annotations.ApiKeyAuthDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
public class AppConfig {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Configuration
    @EnableSwagger2
    public static class SwaggerConfiguration {

        @Bean
        public LinkDiscoverers discoverers() {
            List<LinkDiscoverer> plugins = new ArrayList<>();
            plugins.add(new CollectionJsonLinkDiscoverer());
            return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
        }

        @Bean
        public Docket jsonApi() {
            return new Docket(SWAGGER_2)
                    .securitySchemes(singletonList(new ApiKey("JWT", AUTHORIZATION, ApiKeyAuthDefinition.ApiKeyLocation.HEADER.name())))
                    .securityContexts(singletonList(
                            SecurityContext.builder()
                                    .securityReferences(
                                            singletonList(SecurityReference.builder()
                                                    .reference("JWT")
                                                    .scopes(new AuthorizationScope[0])
                                                    .build()
                                            )
                                    )
                                    .build())
                    )
                    .select()
                    .build();
        }
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET","POST","PATCH","DELETE","PUT")
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*");
            }
        };
    }

}
