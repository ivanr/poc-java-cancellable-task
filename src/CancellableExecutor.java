import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class CancellableExecutor extends ThreadPoolExecutor {

    private Set<CancellableCallable> cancellableCallables = new HashSet<>();

    public CancellableExecutor(int poolSize) {
        super(poolSize,poolSize, Long.MAX_VALUE, TimeUnit.DAYS, new LinkedBlockingQueue<>());
    }

    @Override
    protected RunnableFuture newTaskFor(Callable callable) {
        if (callable instanceof CancellableCallable) {
            return new FutureCancellableTask((CancellableCallable)callable);
        } else {
            return super.newTaskFor(callable);
        }
    }

    @Override
    public void beforeExecute(Thread t, Runnable r) {
        if (r instanceof FutureCancellableTask) {
            CancellableCallable c = ((FutureCancellableTask) r).getTask();
            System.out.println("Notified before task executes " + c);
            cancellableCallables.add(c);
        }
    }

    @Override
    public void afterExecute(Runnable r, Throwable t) {
        if (r instanceof FutureCancellableTask) {
            CancellableCallable c = ((FutureCancellableTask) r).getTask();
            System.out.println("Notified after task executes " + c);
            cancellableCallables.remove(c);
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        System.out.println("MyExecutor.shutdownNow called " + cancellableCallables.size());

        for (CancellableCallable cancellableCallable : cancellableCallables) {
            cancellableCallable.cancel();
        }

        return super.shutdownNow();
    }
}
