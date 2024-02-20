package com.example.photos.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.example.photos.constant.RabbitMQConstant.*;

/**
 * @Auther: Arrow
 * @Date: 2023/5/5
 * @Description: com.example.photos.config
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue articleQueue() {
        return new Queue(MAXWELL_QUEUE, true);
    }

    @Bean
    public FanoutExchange maxWellExchange() {
        return new FanoutExchange(MAXWELL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingArticleDirect() {
        return BindingBuilder.bind(articleQueue()).to(maxWellExchange());
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }

    @Bean
    public Queue subscribeQueue() {
        return new Queue(SUBSCRIBE_QUEUE, true);
    }

    @Bean
    public FanoutExchange subscribeExchange() {
        return new FanoutExchange(SUBSCRIBE_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingSubscribeDirect() {
        return BindingBuilder.bind(subscribeQueue()).to(subscribeExchange());
    }

    @Bean
    public Queue refreshUserFeatureQueue(){
        return new Queue(REFRESH_USER_FEATURE_QUEUE,true);
    }

    @Bean
    public DirectExchange refreshUserFeatureExchange(){
        return new DirectExchange(REFRESH_USER_FEATURE_EXCHANGE,true,true);
    }
    @Bean
    public Binding bindingRefreshUserFeatureDirect(){
        return BindingBuilder.bind(refreshUserFeatureQueue()).to(refreshUserFeatureExchange()).with("#");
    }

}
