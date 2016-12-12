package joey.hsiao.entity;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class TaskDispatcher {
    String name;
    TaskDispatcher next;
    EscalateThread escalation;
    
    Queue<Employee> queue = new LinkedList<Employee>();
    
    public TaskDispatcher(){
        escalation = new EscalateThread();
        escalation.start();
    }

    public TaskDispatcher(String name){
        this();
        this.name = name;
    }
    
    public TaskDispatcher(String name, TaskDispatcher next){
        this();
        this.name = name;
        this.next = next;
    }

    public TaskDispatcher(int workerNumber){
        this();
        for(int i = 0 ; i < workerNumber ; i++){
            queue.offer(new Employee());
        }
    }
    
    public void handle(Task t){
        synchronized (queue) {
            if(queue.isEmpty()){
                System.out.println("no available worker, escalate to next stage...");
                escalate(t);
            }else{
                queue.poll().handle(t);
            }
        }
    }
    
    public void escalate(Task t){
        System.out.println("escalate task "+t+" to "+next);
        escalation.escalate(t);
    }
    
    public void addEmployee(Employee e){
        queue.add(e);
        e.start();
    }

    @Override
    public String toString() {
        return "TaskDispatcher [name=" + name + ", next=" + (next == null?"null":next.name) + "]";
    }

    public class EscalateThread extends Thread {
        
        
        BlockingQueue<Task> taskQueue = new SynchronousQueue<Task>();
        
        public void escalate(Task t){
            try {
                taskQueue.put(t);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        public void run(){
            while(true){
                try {
                    Task t = taskQueue.take();
                    if(next != null){
                        next.handle(t);
                    }else{
                        System.err.println("No dispatcher gonna handle task "+t);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}

