package com.admin.catalog.infrastructure;

import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);
        cleanUp(List.of(
            appContext.getBean(GenreRepository.class),
            appContext.getBean(CategoryRepository.class)
        ));
    }

    private void cleanUp(final List<JpaRepository> repositories) {
        repositories.forEach(JpaRepository::deleteAll);
    }

}
