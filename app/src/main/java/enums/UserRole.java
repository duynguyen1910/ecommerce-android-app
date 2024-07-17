package enums;

public enum UserRole {
    ADMIN_ROLE(UserRole.ADMIN_VALUE, UserRole.ADMIN_LABEL),
    STORE_OWNER_ROLE(UserRole.STORE_OWNER_VALUE, UserRole.STORE_OWNER_LABEL),
    CUSTOMER_ROLE(UserRole.CUSTOMER_VALUE, UserRole.CUSTOMER_LABEL);

    private final int roleValue;
    private final String label;

    private static final int ADMIN_VALUE = 0;
    private static final int STORE_OWNER_VALUE = 1;
    private static final int CUSTOMER_VALUE = 2;

    private static final String ADMIN_LABEL = "Admin";
    private static final String STORE_OWNER_LABEL = "Store Owner";
    private static final String CUSTOMER_LABEL = "Customer";


    UserRole(int value, String label) {
        this.roleValue = value;
        this.label = label;
    }

    public int getRoleValue() {
        return roleValue;
    }
    public String getLabelRole() {
        return label;
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
