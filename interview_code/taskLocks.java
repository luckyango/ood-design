// Problem: Concurrent Task Lock with Type-Based Mutual Exclusion

// Description
// You are designing a concurrent task execution system where multiple tasks run in parallel using threads.

// Each task belongs to one of two types:
// "gen"
// "score"

// Each task has:
// an id
// a type
// a duration (in seconds)

// 🧠 Execution Model
// Each task runs in its own thread and follows this workflow:
// acquire lock → execute task → release lock
// The system provides a shared TaskLock that controls how tasks are scheduled.

// 📋 Requirements
// You need to implement a custom lock with the following behavior:
// 1️⃣ Concurrency Rules
// Tasks of the same type can run concurrently
// Tasks of different types must NOT run at the same time
// 2️⃣ Mutual Exclusion Constraint
// At any given time:
// Either multiple "gen" tasks can run
// Or multiple "score" tasks can run
// But never both types simultaneously
// 3️⃣ Blocking Behavior
// If a task tries to start while tasks of a different type are running:
// it must wait
// Once all running tasks finish:
// waiting tasks may proceed

// 🎯 Goal
// Implement a thread-safe lock class that enforces the above rules.

// 🧠 Class Design (Java)
// class SampleTask {
//     int taskId;
//     String taskType; // "gen" or "score"
//     int duration;

//     public SampleTask(int taskId, String taskType, int duration) {
//         this.taskId = taskId;
//         this.taskType = taskType;
//         this.duration = duration;
//     }
// }
// 🔒 Lock Interface
// interface TaskLock {
//     void acquire(SampleTask task) throws InterruptedException;
//     void release(SampleTask task);
// }
// 🚧 Your Task

// Implement the following class:

// class MutexTaskLock implements TaskLock {
//     @Override
//     public void acquire(SampleTask task) throws InterruptedException {
//         // TODO
//     }

//     @Override
//     public void release(SampleTask task) {
//         // TODO
//     }
// }
// 📥 Execution Function

// You may assume tasks are executed like this:

// void runTasks(List<SampleTask> tasks, TaskLock lock) {
//     for (SampleTask task : tasks) {
//         new Thread(() -> {
//             try {
//                 lock.acquire(task);
//                 System.out.println("Task " + task.taskId + " started");
//                 Thread.sleep(task.duration * 1000);
//                 System.out.println("Task " + task.taskId + " finished");
//                 lock.release(task);
//             } catch (InterruptedException e) {
//                 Thread.currentThread().interrupt();
//             }
//         }).start();
//     }
// }
// 🧪 Example
// Input Tasks
// Task 1: gen
// Task 2: gen
// Task 3: score
// Task 4: score
// Expected Behavior
// ✔ Task 1 & 2 can run together
// ✘ Task 3 cannot start until all gen tasks finish
// ✔ Task 3 & 4 can run together afterward
// ⚠️ Constraints
// Use Java concurrency primitives (Lock, Condition, synchronized, etc.)
// Avoid busy waiting
// Ensure thread safety
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class taskLocks {
    class SampleTask {
        int taskId;
        String taskType; // "gen" or "score"
        int duration;

        public SampleTask(int taskId, String taskType, int duration) {
            this.taskId = taskId;
            this.taskType = taskType;
            this.duration = duration;
        }
    }
    interface TaskLock {
        void acquire(SampleTask task) throws InterruptedException;
        void release(SampleTask task);
    }
    
    void runTasks(List<SampleTask> tasks, TaskLock lock) {
        for (SampleTask task : tasks) {
            new Thread(() -> {
                try {
                    lock.acquire(task);
                    System.out.println("Task " + task.taskId + " started");
                    Thread.sleep(task.duration * 1000);
                    System.out.println("Task " + task.taskId + " finished");
                    lock.release(task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    class MutexTaskLock implements TaskLock {
        private String currentType = null;
        private int count = 0;

        private final Lock lock = new ReentrantLock();
        private final Condition condition = lock.newCondition();
        @Override
        public void acquire(SampleTask task) throws InterruptedException {
            // TODO
            lock.lock();
            try{
                while(currentType!=null && !task.taskType.equals(currentType)){
                    condition.await();
                }
                if(currentType == null) currentType = task.taskType;
                count++;

            }finally{
                lock.unlock();
            }
        }

        @Override
        public void release(SampleTask task) {
            // TODO
            lock.lock();
            try{
                count--;
                if(count==0){
                    currentType = null;
                }
                condition.signalAll();
            }finally{
                lock.unlock();
            }

        }
    }
}
