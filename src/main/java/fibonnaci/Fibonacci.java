/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fibonnaci;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dennisschmock
 */
public class Fibonacci {
    
    private static BlockingQueue S1;
    private static BlockingQueue S2;
    private static Long initializer[]= {4L,5L,8L,12L,21L,22L,34L,35L,36L,37L,42L};
    private static Long sum;


    public static void main(String[] args) {
      
        S1 = new ArrayBlockingQueue<>(initializer.length,true,new ArrayList<>(Arrays.asList(initializer)));
        S2 = new ArrayBlockingQueue(S1.size());
    }

    

    class Producer implements Runnable {

        @Override
        public void run() {
            Long number = (Long)S1.poll();
            while(number!=null){
                number = (Long)S1.poll();
                try {
                    S2.put(fib(number));
                } catch (InterruptedException ex) {
                    System.out.println("No room in S2");
                    Logger.getLogger(Fibonacci.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }
            } 
            
            
        }
        private long fib(long n) {
        if ((n == 0) || (n == 1)) {
            return n;
        } else {
            return fib(n - 1) + fib(n - 2);
        }
    }
    }
    
    class Consumer implements Runnable {
        private Long sum;
        @Override
        public void run() {
            
            try {
                
                sum = sum + (Long)S2.take();
                System.out.println("Number added, now: " + sum);
            } catch (InterruptedException ex) {
                Logger.getLogger(Fibonacci.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Out of numbers, sum: " + sum);
            }
        }
        
    }

    

}
