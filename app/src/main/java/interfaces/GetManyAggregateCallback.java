package interfaces;

public interface GetManyAggregateCallback {
    // (các hàm tổng hợp dữ liệu sum, count, average)
    void onSuccess(double... aggregateResults);
    void onFailure(String errorMessage);
}
