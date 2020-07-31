package com.yxy.config;

import org.apache.xpath.operations.Bool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: yxydemo
 * @description: swagger相关
 * @author: yuxinyu
 * @create: 2020-07-24 15:26
 **/
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value(value = "${spring.swagger.enable}")
    Boolean swaggerEnable;

    @Bean
    public Docket createRestApi () {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).enable(swaggerEnable).select().apis(RequestHandlerSelectors.basePackage("com.yxy")).paths(PathSelectors.any()).build().pathMapping("/demo-server");
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("swagger").description("springboot | swagger").contact(new Contact("snows", "https://home.cnblogs.com/u/snowstorm/", "1083270492@qq.com")).version("1.0.0").build();
    }
}
