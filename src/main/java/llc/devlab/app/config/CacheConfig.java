package llc.devlab.app.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import llc.devlab.app.security.model.dto.PermissionResponse;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        var responseSerializer = new JacksonJsonRedisSerializer<>(PermissionResponse.class);
        var booleanSerializer = new JacksonJsonRedisSerializer<>(Boolean.class);
        RedisCacheConfiguration defaultCacheConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10))
                        .computePrefixWith(cacheName -> "v4::" + cacheName + "::")
                        .disableCachingNullValues()
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(new JacksonJsonRedisSerializer<>(Object.class)));
        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withCacheConfiguration(CacheNames.USER_PERMISSIONS,
                        defaultCacheConfig.serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(responseSerializer)))
                .withCacheConfiguration(CacheNames.ROLE_PERMISSIONS,
                        defaultCacheConfig.serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(responseSerializer)))
                .withCacheConfiguration(CacheNames.HAS_PERMISSION,
                        defaultCacheConfig.serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(booleanSerializer)))
                .build();
    }

    public static class CacheNames {
        private CacheNames() {}

        public static final String HAS_PERMISSION = "hasPermission";
        public static final String ROLE_PERMISSIONS = "rolePermissions";
        public static final String USER_PERMISSIONS = "userPermissions";
    }
}
