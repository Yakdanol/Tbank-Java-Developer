package org.yakdanol.task5_6.exception;

public class EmptyRepositoryException extends RuntimeException {
    public EmptyRepositoryException(String repositoryName) {
        super(repositoryName + " is empty.");
    }
}
