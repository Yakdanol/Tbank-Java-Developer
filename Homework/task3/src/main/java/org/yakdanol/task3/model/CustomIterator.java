package org.yakdanol.task3.model;

import java.util.function.Consumer;

public interface CustomIterator<E> {
    boolean hasNext();

    E next();

    default void forEachRemaining(Consumer<? super E> action) {
        while (hasNext()) {
            action.accept(next());
        }
    }
}
