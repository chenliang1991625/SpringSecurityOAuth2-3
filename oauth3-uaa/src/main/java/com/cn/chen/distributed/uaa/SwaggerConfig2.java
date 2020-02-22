//package com.cn.chen.distributed.order.config;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.OAuthBuilder;
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.schema.ModelRef;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.AuthorizationScope;
//import springfox.documentation.service.Contact;
//import springfox.documentation.service.GrantType;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
//import springfox.documentation.service.SecurityReference;
//import springfox.documentation.service.SecurityScheme;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
///* * @Description Swagger api 配置
// * @Author wwz
// * @Date 2019/08/05*/
//@Configuration
//@EnableSwagger2
//public class SwaggerConfig2 {
//   @Value("${swagger.is.enable}")
//   private boolean SWAGGER_IS_ENABLE; //是否激活开关,在application.yml中配置注入
//    @Value("${swagger.auth.server}")
//    private String AUTH_SERVER_TYPE;//使用哪种认证方式，我这里使用密码模式,但配置文件中没填
//    @Value("${swagger.service.name}")
//    private String SERVICE_NAME;
//    @Bean
//    public Docket docket() {
//        //添加head参数配置start
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<>();
////        授权令牌access_token:如果是get请求前面要加Bearer,即"Bearer 你的access_token",其它请求直接填你的access_token
//        tokenPar.name("Authorization").description("请输入access_token授权令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        pars.add(tokenPar.build());
//        return new Docket(DocumentationType.SWAGGER_2)
////                .groupName("spring security OAuth2权限管理API文档")
//                .enable(SWAGGER_IS_ENABLE)//是否激活swagger API文档开关
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.cn.chen.distributed.uaa.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .globalOperationParameters(pars);//注意这里;
//    }
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                // 页面标题
//                .title("OAuth2 "+SERVICE_NAME+"服务API文档")
//                .contact(new Contact("陈亮", "", "1484157619@qq.com"))
//                .description("OAuth2维护文档")
//                .version("1.0")
//                .extensions(Collections.emptyList())
//                .build();
//    }
//// 这个方法决定了你使用哪种认证方式,我这里使用密码模式
//    private SecurityScheme securityScheme() {
//        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(AUTH_SERVER_TYPE);
//        return new OAuthBuilder()
//                .name("OAuth2")
//                .grantTypes(Collections.singletonList(grantType))
//                .scopes(Arrays.asList(scopes()))
//                .build();
//    }
//// 这里设置swagger2认证的安全上下文
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(Collections.singletonList(new SecurityReference("OAuth2", scopes())))
//                .forPaths(PathSelectors.any())
//                .build();
//    }
////这里是写允许认证的scope
//    private AuthorizationScope[] scopes() {
//        return new AuthorizationScope[]{
//        };
//    }
//}
