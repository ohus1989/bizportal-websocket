package com.kdax.bizportal.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${application.swagger.basePackage}")
    private String basePackage;

    @Bean
    public Docket apiV1() {

        List<Parameter> global = new ArrayList<>();
        global.add(new ParameterBuilder().name("bizportal-access-token").
                description("bizportal access token").parameterType("header").
                required(false).modelRef(new ModelRef("string")).build());

        ResponseMessage error500 = new ResponseMessageBuilder()
                .code(500)
                .message("500 message")
                // .responseModel(new ModelRef("Error"))
                .build();

        ResponseMessage error403 = new ResponseMessageBuilder()
                .code(403)
                .message("Forbidden!")
                .build();

        List<ResponseMessage> errorList = new ArrayList<>();
        errorList.add(error403);
        errorList.add(error500);

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(global)
                .groupName("v1")
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.ant("/v1/**"))
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, errorList);
    }


    @Bean
    public Docket apiV2() {
        ResponseMessage error500 = new ResponseMessageBuilder()
                .code(500)
                .message("500 message")
                // .responseModel(new ModelRef("Error"))
                .build();

        ResponseMessage error403 = new ResponseMessageBuilder()
                .code(403)
                .message("Forbidden!")
                .build();

        List<ResponseMessage> errorList = new ArrayList<>();
        errorList.add(error403);
        errorList.add(error500);

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("v2")
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.ant("/v2/**"))
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, errorList);
    }

}
