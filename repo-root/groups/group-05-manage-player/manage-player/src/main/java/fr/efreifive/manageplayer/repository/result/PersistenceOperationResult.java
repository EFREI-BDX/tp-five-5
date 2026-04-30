package fr.efreifive.manageplayer.repository.result;

public record PersistenceOperationResult(boolean success, String message) {
    public static PersistenceOperationResult ok() {
        return new PersistenceOperationResult(true, null);
    }

    public static PersistenceOperationResult failure(String message) {
        return new PersistenceOperationResult(false, message);
    }
}
