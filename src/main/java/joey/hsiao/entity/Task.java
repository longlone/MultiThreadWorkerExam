

package joey.hsiao.entity;

import java.util.Calendar;
import java.util.Random;

public class Task {
    String content;
    
    public Task(){
        content = String.valueOf(Calendar.getInstance().hashCode());
    }
    
    public void execute(){
        Random r = new Random();
        
        try {
            System.out.println("handling '"+content+"'");
            int milli = r.nextInt(10)*1000;
//            System.out.println(milli);
            Thread.sleep(milli);
            System.out.println("done '"+content+"'");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Task [content=" + content + "]";
    }   
}
