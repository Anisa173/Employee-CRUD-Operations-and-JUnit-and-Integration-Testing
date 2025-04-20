package junitAndIntegrationSprongBootTesting.demoTest.repository;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

public abstract class AbstractContainersTestBase {

    static final MySQLContainer MY_SQL_CONTAINER;

    static {

        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
                .withUsername("root")
                .withPassword("rootSQL@@1703")
                .withDatabaseName("employee");
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void dynamicPropertySource(DynamicPropertyRegistry pRegistry) {
        pRegistry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        pRegistry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        pRegistry.add("spring.datasource.jdbcUrl", MY_SQL_CONTAINER::getJdbcUrl);
    }


}
