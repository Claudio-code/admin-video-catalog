package com.admin.catalog.application.category.create;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

@RequiredArgsConstructor
public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public Either<Notification, CreateCategoryOutput> execute(CreateCategoryCommand aIn) {
        final var notification = Notification.create();
        final var aCategory = Category.newCategory(
            aIn.name(),
            aIn.description(),
            aIn.isActive());
        aCategory.validate(notification);
        return notification.hasError() ? Left(notification) : create(aCategory);
    }

    private Either<Notification, CreateCategoryOutput> create(final Category aCategory) {
        return Try(() -> categoryGateway.create(aCategory))
            .toEither()
            .bimap(Notification::create, CreateCategoryOutput::from);
    }

}
