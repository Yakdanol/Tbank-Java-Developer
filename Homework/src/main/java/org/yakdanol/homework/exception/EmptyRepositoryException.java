package org.yakdanol.homework.exception;

public class EmptyRepositoryException extends RuntimeException {
    public EmptyRepositoryException(String repositoryName) {
        super(repositoryName + " is empty.");
    }
}
