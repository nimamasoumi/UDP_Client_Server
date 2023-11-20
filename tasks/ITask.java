package tasks;

/* Tasks are basically wrappers for the listeners of the KUKAserver
 * Each task implements the listeners inside it's body and manages their 
 * creation and deleting.
 */

public interface ITask {
     /**
     * Task run() method is the task entry point.
     */
    public boolean run();

    /**
     * Initialize method is used to allocate and hook resources.
     * 
     * This method should be called at the beginning of the run() method inside
     * the task.
     */
    public boolean initialize();

    /**
     * Reset task to it's state before initialize would be called.
     * 
     * This method should be called at the end of the run() method inside the
     * task.
     */
    public boolean reset();
}
