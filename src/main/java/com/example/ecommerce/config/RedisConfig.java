package com.example.ecommerce.config;

import com.example.ecommerce.cache.constant.CacheNames;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Production Redis configuration providing Jackson 2 JSON serializers with {@link JavaTimeModule}
 * support, string key serializers, imperative {@link RedisTemplate}, and custom per-cache TTL configurations.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    private ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        return objectMapper;
    }

    private GenericJackson2JsonRedisSerializer redisValueSerializer() {
        return new GenericJackson2JsonRedisSerializer(redisObjectMapper());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer serializer = redisValueSerializer();
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer()));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        GenericJackson2JsonRedisSerializer serializer = redisValueSerializer();
        RedisSerializationContext.SerializationPair<Object> valuePair = RedisSerializationContext.SerializationPair.fromSerializer(serializer);

        return builder -> builder
                .withCacheConfiguration(CacheNames.PRODUCTS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)).serializeValuesWith(valuePair))
                .withCacheConfiguration(CacheNames.PRODUCT_DETAILS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)).serializeValuesWith(valuePair))
                .withCacheConfiguration(CacheNames.CATEGORIES, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).serializeValuesWith(valuePair))
                .withCacheConfiguration(CacheNames.CATEGORY_TREE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).serializeValuesWith(valuePair))
                .withCacheConfiguration(CacheNames.BRANDS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).serializeValuesWith(valuePair))
                .withCacheConfiguration(CacheNames.ACTIVE_BRANDS, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(24)).serializeValuesWith(valuePair))
                .withCacheConfiguration(CacheNames.SEARCH_AUTOCOMPLETE, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)).serializeValuesWith(valuePair))
                .withCacheConfiguration(CacheNames.SEARCH_TRENDING, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)).serializeValuesWith(valuePair))
                .withCacheConfiguration(CacheNames.DASHBOARD_SUMMARY, RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)).serializeValuesWith(valuePair));
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration())
                .build();
    }
}
