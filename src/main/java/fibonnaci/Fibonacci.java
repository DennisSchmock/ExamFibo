/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fibonnaci;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dennisschmock
 */
public class Fibonacci {
    private static int count = 0;
    private static BlockingQueue<Long> S1;
    private static BlockingQueue<Long> S2;
    

//    private static Long sum;
    public static void main(String args[]) {
        Long initializer[] = {4L, 5L, 8L, 12L, 21L, 22L, 34L, 35L, 36L, 37L,42L};
        S1 = new ArrayBlockingQueue<>(initializer.length, true, new ArrayList<>(Arrays.asList(initializer)));
        S2 = new ArrayBlockingQueue<>(11);

        ExecutorService ex = Executors.newCachedThreadPool();
        ExecutorService cosEx = Executors.newCachedThreadPool();
        for (int i = 0; i < 2; i++) {
            ex.execute(new Producer(S1, S2));
        }
        Consumer c1 = new Consumer(S2, initializer.length);

        cosEx.execute(c1);
        
        ex.shutdown();
        cosEx.shutdown();
        System.out.println(S2);
    }

}





class Producer implements Runnable {

    private final BlockingQueue s1;
    private final BlockingQueue s2;

    public Producer(BlockingQueue s1, BlockingQueue s2) {
        this.s1 = s1;
        System.out.println("From thread" + s1);
        this.s2 = s2;
        
    }

    @Override
    public void run() {
        Long number = (Long) s1.poll();
        while (true) {

            try {
                number = (Long) s1.poll();
                if (number != null) {
                    s2.put(fib(number));
                    System.out.println(s1);
                    System.out.println(s2);
                }
            } catch (InterruptedException ex) {
                System.out.println("Thread ending");
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

    private BlockingQueue s2;
    private int numbers;

    public Consumer(BlockingQueue s2, int numbers) {
        this.s2 = s2;
        this.numbers = numbers;
        System.out.println("S2 in consumer" + s2);
    }

    @Override
    public void run() {
        Long sum = 0L;
        while(true) {
            try {
                Long number = (Long) s2.take();
                sum = sum + number;
                System.out.println(number + " Number added, now: " + sum);
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
