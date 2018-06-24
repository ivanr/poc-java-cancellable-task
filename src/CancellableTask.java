public class CancellableTask implements CancellableCallable<Boolean> {

    private final long sleepTime;

    private boolean cancelled = false;

    public CancellableTask(int sleepTime) {
        System.out.println(this + " NEW");
        this.sleepTime = sleepTime;
    }

    @Override
    public void cancel() {
        // It's a good practice to avoid cancelling the task more than once.
        if (cancelled) {
            System.out.println(this + " CANCELLED CALLED AGAIN");
            return;
        }

        // In reality, you'd here take some action to unblock this task,
        // for example, close all its sockets.

        cancelled = true;

        System.out.println(this + " CANCELLED");
    }

    @Override
    public Boolean call() throws Exception {
        System.out.println(this + " STARTED");

        try {
            for (int i = 1; i < 2; i++) {
                // Before starting a long operation check the interrupted
                // status of the thread. Use Thread#isInterrupted(), which
                // is safe because it doesn't affect the interrupted status
                // of the thread.
                if (Thread.currentThread().isInterrupted()) {
                    // In this case, we just throw an exception ourselves to get out.
                    throw new InterruptedException();
                }

                // Simulation of a long and blocking operation. Sleeping isn't
                // really blocking as it detects InterruptedException, but it's
                // close enough for our simulation needs here.
                System.out.println(this + " SLEEPING " + sleepTime + " ms");
                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException e) {
            // We're catching InterruptedException here just to print
            // the message, normally you wouldn't need to do this.
            System.out.println(this + " INTERRUPTED");
            throw e;
        } finally {
            // Cleanup task resources here.
        }

        System.out.println(this + " COMPLETED NORMALLY");

        return true;
    }
}
