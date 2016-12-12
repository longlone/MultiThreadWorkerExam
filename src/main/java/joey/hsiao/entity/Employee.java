package joey.hsiao.entity;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

import joey.hsiao.exception.WorkerException;

public class Employee extends Thread{
    String name;
    Random r = new Random();
    TaskDispatcher dispatcher;
    int failPercentage = 10;
    BlockingQueue<Task> taskQueue = new SynchronousQueue<Task>();
    
    public Employee(){}

    public Employee(String name){
        this.name = name;
    }
    
    public Employee(String name, TaskDispatcher dispatcher){
        this.name = name;
        this.dispatcher = dispatcher;
    }
    
    public Employee(String name, TaskDispatcher dispatcher, int failPercentage){
        this.name = name;
        this.dispatcher = dispatcher;
        this.failPercentage = failPercentage;
    }
    
    public synchronized void handle(Task t){
        try{
            taskQueue.add(t);
        }catch(IllegalStateException e){
            System.err.println(name +" got problem while handling ruquest "+ t +". queue size:"+taskQueue);
        }
    }

    @Override
    public void run() {
        if(dispatcher == null){
            throw new WorkerException("no dispatcher defined!");
        }
        if(name == null){
            System.out.println("no name define, use a random name...");
            name = String.valueOf(r.nextInt());
        }
        
        while(true){
            executeTask();
        }
    }

    private void executeTask(){
        try{
            Task t = taskQueue.take();
            System.out.println(name + " start to execute task "+t);
            t.execute();
            if(r.nextInt(100) < failPercentage){// failure ratio
                //fail
                dispatcher.escalate(t);
            }else{
                System.out.println(name +" finish to execute task "+t);
            }
        } catch (InterruptedException e) {
            System.out.println(name + " got interrupted!");
            e.printStackTrace();
        }finally{
            System.out.println(name +" is back to waiting queue again");
            dispatcher.queue.offer(this);
        }
    }
}
