package com.admin.catalog.infrastructure.genre.e2e;

import com.admin.catalog.infrastructure.E2ETest;
import com.admin.catalog.infrastructure.MockDsl;
import com.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class GenresE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private GenreRepository genreRepository;
    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
        .withPassword("123456")
        .withUsername("root")
        .withDatabaseName("catalog");

    @Override
    public MockMvc mvc() {
        return mvc;
    }

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

}
