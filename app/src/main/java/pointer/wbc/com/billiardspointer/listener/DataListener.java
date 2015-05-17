package pointer.wbc.com.billiardspointer.listener;

public interface DataListener<T extends Object> {

	public void onFinish(T result);
}
