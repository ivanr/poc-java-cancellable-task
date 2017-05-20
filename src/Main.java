import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        ExecutorService exec = new CancellableExecutor(2);

        // This task we will cancel.
        CancellableTask t1 = new CancellableTask(10000);

        // This task will complete on its own.
        CancellableTask t2 = new CancellableTask(10);

        // This task will be cancelled when we shut down the executor.
        CancellableTask t3 = new CancellableTask(10000);

        Future<?> f = exec.submit(t1);
        exec.submit(t2);
        exec.submit(t3);

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException ignored) {}

        f.cancel(true);

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException ignored) {}

        exec.shutdownNow();
    }
}
