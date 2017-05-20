import java.util.concurrent.Callable;

public interface CancellableCallable<V> extends Callable<V> {

    void cancel();
}
