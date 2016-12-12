package joey.hsiao;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import joey.hsiao.entity.Employee;
import joey.hsiao.entity.Task;
import joey.hsiao.entity.TaskDispatcher;

/**
 * Hello world!
 *
 */
public class App 
{
    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<Task>();
    
    private TaskDispatcher freshers;
    private TaskDispatcher productManagers;
    private TaskDispatcher teamleaders;

    public void init(){
        productManagers = new TaskDispatcher("productManagers");
        teamleaders = new TaskDispatcher("teamleaders",productManagers);
        freshers = new TaskDispatcher("freshers",teamleaders);
        
        for(int i = 0 ; i < 20 ; i++){
            Employee fresher = new Employee("fresher "+i, freshers);
            freshers.addEmployee(fresher);
        }
        
        Employee teamleader = new Employee("teamleader", teamleaders);
        teamleaders.addEmployee(teamleader);

        Employee productManager = new Employee("productManager", productManagers);
        productManagers.addEmployee(productManager);
    }
    
    public void start(){
        TaskGenerator t = new TaskGenerator(taskQueue);
        t.start();
        while(true){
            Task task;
            try {
                task = taskQueue.take();
                freshers.handle(task);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App app = new App();
        
        app.init();
        app.start();
    }
}
