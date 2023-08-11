package model;

public enum Status {
    NEW("Новый"),
    IN_PROGRESS("В процессе"),
    DONE("Готово");

    private final String rusStatus;

    Status(String rusStatus) {
        this.rusStatus = rusStatus;
    }

    @Override
    public String toString() {
        return "[" + rusStatus + "]";
    }
}
