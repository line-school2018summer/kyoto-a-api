package com.example.apiSample.swagger

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2 // swagger(v2)を有効にする
class Swagger2Config {

    @Bean
    fun swaggerSpringMvcPlugin(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(paths())
                .build()
                .apiInfo(apiInfo())
    }

    private fun paths(): Predicate<String> {

        // 仕様書生成対象のURLパスを指定する
        return Predicates.and(
                Predicates.or(
                        Predicates.containsPattern("/users/*"),
                        Predicates.containsPattern("/messages/*"),
                        Predicates.containsPattern("/rooms/*")))
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
                "Kyoto A Web API", // title
                "Kyoto AのWebAPIの仕様書です", // description
                "0.0.1", // version
                "", // terms of service url
                "Line summer intern kyoto A", // created by
                "Line summer intern kyoto A", // license
                "")
    }
}