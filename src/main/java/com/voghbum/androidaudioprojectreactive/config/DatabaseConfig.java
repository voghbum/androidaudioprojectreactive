package com.voghbum.androidaudioprojectreactive.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({R2dbcProperties.class, FlywayProperties.class})
public class DatabaseConfig extends AbstractR2dbcConfiguration {

    @Value("${audioproject-webflux.database.host}")
    private String host;

    @Value("${audioproject-webflux.database.port}")
    private int port;

    @Value("${audioproject-webflux.database.name}")
    private String database;

    @Value("${audioproject-webflux.database.schema}")
    private String schema;

    @Value("${audioproject-webflux.database.username}")
    private String username;

    @Value("${audioproject-webflux.database.password}")
    private String password;

    @Value("${audioproject-webflux.database.pool.size.initial}")
    private int initialPoolSize;

    @Value("${audioproject-webflux.database.pool.size.max}")
    private int maxPoolSize;


    @Override
    @Bean
    public ConnectionFactory connectionFactory() {

        final PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .database(database)
                .schema(schema)
                .username(username)
                .password(password)
                .build()
        );

        final ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration
                .builder(connectionFactory)
                .initialSize(initialPoolSize)
                .maxSize(maxPoolSize)
                .build();


        return new ConnectionPool(poolConfiguration);
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

}
