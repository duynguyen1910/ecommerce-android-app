package enums;

public enum OrderStatus {
    CANCELLED(OrderStatus.CANCELLED_LABEL, OrderStatus.CANCELLED_VALUE),
    PENDING_CONFIRMATION(OrderStatus.PENDING_CONFIRMATION_LABEL, OrderStatus.PENDING_CONFIRMATION_VALUE),
    PENDING_SHIPMENT(OrderStatus.PENDING_SHIPMENT_LABEL, OrderStatus.PENDING_SHIPMENT_VALUE),
    IN_TRANSIT(OrderStatus.IN_TRANSIT_LABEL, OrderStatus.IN_TRANSIT_VALUE),
    DELIVERED(OrderStatus.DELIVERED_LABEL, OrderStatus.DELIVERED_VALUE);

    private final int orderStatusValue;
    private final String label;

    private static final int CANCELLED_VALUE = 0;
    private static final int PENDING_CONFIRMATION_VALUE = 1;
    private static final int PENDING_SHIPMENT_VALUE = 2;
    private static final int IN_TRANSIT_VALUE = 3;
    private static final int DELIVERED_VALUE = 4;


    private static final String CANCELLED_LABEL = "Đã hủy";
    private static final String PENDING_CONFIRMATION_LABEL = "Chờ xác nhận";
    private static final String PENDING_SHIPMENT_LABEL = "Chờ giao hàng";
    private static final String IN_TRANSIT_LABEL = "Đang giao hàng";
    private static final String DELIVERED_LABEL = "Hoàn thành";


    OrderStatus(String label, int value) {
        this.label = label;
        this.orderStatusValue = value;
    }

    public int getOrderStatusValue() {
        return orderStatusValue;
    }
    public String getOrderLabel() {
        return label;
    }

    public static OrderStatus fromInt(int value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getOrderStatusValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value for OrderStatus: " + value);
    }
}
