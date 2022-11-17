package com.admin.catalog.application.category.update;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryID;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

@RequiredArgsConstructor
public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
        final var anId = CategoryID.from(aCommand.id());
        final var aCategory = this.categoryGateway.findById(anId)
            .orElseThrow(NotFoundException.notFoundSupplier(anId, Category.class));

        final var notification = Notification.create();
        aCategory.update(aCommand.name(), aCommand.description(), aCommand.isActive())
            .validate(notification);

        return notification.hasError() ? Left(notification) : update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return Try(() -> this.categoryGateway.update(aCategory))
            .toEither()
            .bimap(Notification::create, UpdateCategoryOutput::from);
    }

}
