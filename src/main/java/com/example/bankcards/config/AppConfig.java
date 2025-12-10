package com.example.bankcards.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация приложения
 */
@Configuration
public class AppConfig {
    /**
     * Настраивает Jackson для использования snake_case именования
     *
     * @return кастомайзер для Jackson ObjectMapper
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    /**
     * Конфигурирует OpenAPI для Bank Rest App.
     * Задаёт метаданные (название, версия, описание, контакт)
     * для отображения в Swagger UI.
     *
     * @return объект OpenAPI с информацией об API
     */
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Rest App")
                        .version("1.0")
                        .description("Banking REST API for account management")
                        .contact(new Contact().name("Rodnenkov Ivan").email("steamivana2@gmail.com"))
                );
    }

}
