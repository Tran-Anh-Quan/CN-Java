package vn.edu.eaut.lab16.shared.enums;

/**
 * Enum representing the three platforms in the system.
 * Used for audit trail and tracking which platform made changes.
 */
public enum Platform {
    JAKARTA("Customer Portal - Jakarta EE"),
    SWING("Warehouse Desktop - Java Swing"),
    SPRING("Management Portal - Spring Boot");

    private final String displayName;

    Platform(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
