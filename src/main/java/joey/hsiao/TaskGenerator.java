package joey.hsiao;

import java.util.Queue;

import joey.hsiao.entity.Task;

public class TaskGenerator extends Thread{
    
    Queue<Task> queue ;
    
    public TaskGenerator(Queue queue) {
        this.queue = queue;
    }
    
    @Override
    public void run(){
        while(true){
            Task t = new Task();
            queue.add(t);
            System.out.println("task is generated..."+ t);
            try {
                Thread.sleep(500);
                
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }

}
