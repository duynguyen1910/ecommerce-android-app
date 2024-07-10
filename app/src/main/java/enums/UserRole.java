package enums;

public enum UserRole {
    ADMIN_ROLE(UserRole.ADMIN_VALUE),
    STORE_OWNER_ROLE(UserRole.STORE_OWNER_VALUE),
    CUSTOMER_ROLE(UserRole.CUSTOMER_VALUE);

    private final int roleValue;
    private static final int ADMIN_VALUE = 0;
    private static final int STORE_OWNER_VALUE = 1;
    private static final int CUSTOMER_VALUE = 2;

    UserRole(int value) {
        this.roleValue = value;
    }

    public int getRoleValue() {
        return roleValue;
    }

    public static UserRole fromInt(int value) {
        for (UserRole role : UserRole.values()) {
            if (role.getRoleValue() == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid value for UserRole: " + value);
    }
}
