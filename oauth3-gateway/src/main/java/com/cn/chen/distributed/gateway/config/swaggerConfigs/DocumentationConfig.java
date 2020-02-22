package com.cn.chen.distributed.gateway.config.swaggerConfigs;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;
//用于整合配置接口文档
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {
    private final RouteLocator routeLocator;
    public DocumentationConfig(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
//        模块少想自定义名字是用:手动加入swagger资源API:name自定义
 /*       resources.add(swaggerResource("订单系统", "/consumer/v2/api-docs", "1.0"));
        resources.add(swaggerResource("授权系统", "/uaa/v2/api-docs", "1.0"));*/
//        模块多推荐用:自动加入swagger资源API,名字项目名
        routes.forEach(route -> {
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"), "1.0"));
        });
        return resources;
    }
//    @Override
//        public List<SwaggerResource> get() {
//            List resources = new ArrayList<>();
//            resources.add(swaggerResource("外接设备系统", "/management-device/v2/api-docs", "1.0"));
//            resources.add(swaggerResource("设备管理系统", "/management-equip/v2/api-docs", "1.0"));
//            return resources;
//        }
    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
