package com.example.apiSample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.runApplication

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan
class ApiSampleApplication

fun main(args: Array<String>) {
    runApplication<ApiSampleApplication>(*args)
}
