package vn.edu.eaut.lab16.shared.enums;

/**
 * User roles for authentication and authorization.
 */
public enum UserRole {
    CUSTOMER("Khách hàng"),
    WAREHOUSE("Nhân viên kho"),
    MANAGER("Quản lý"),
    ADMIN("Quản trị viên");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canAccess(String platform) {
        switch (this) {
            case ADMIN:
                return true; // Admin can access all platforms
            case MANAGER:
                return "SPRING".equals(platform);
            case WAREHOUSE:
                return "SWING".equals(platform);
            case CUSTOMER:
                return "JAKARTA".equals(platform);
            default:
                return false;
        }
    }
}
