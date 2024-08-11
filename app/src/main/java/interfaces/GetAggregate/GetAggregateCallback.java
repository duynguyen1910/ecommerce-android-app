package interfaces.GetAggregate;

public interface GetAggregateCallback {
    // (các hàm tổng hợp dữ liệu sum, count, average)
    void onSuccess(double aggregateResult);
    void onFailure(String errorMessage);
}
