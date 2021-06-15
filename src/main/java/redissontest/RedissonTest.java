package redissontest;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedissonTest {
	public static void main(String[] args) throws InterruptedException {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://192.168.99.100:6379");

		final RedissonClient redisson = Redisson.create(config);
    
    RLock lock = redisson.getLock("lock");
    lock.lock(1, TimeUnit.SECONDS);
    System.out.println("1 " + lock.isLocked());

    Thread t = new Thread() {
        public void run() {
            RLock lock1 = redisson.getLock("lock");
            System.out.println("2 " + lock1.isLocked());
        };
    };

    t.start();
    t.join();

    Thread t2 = new Thread() {
        public void run() {
            RLock lock1 = redisson.getLock("lock");
    				try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            System.out.println("3 " + lock1.isLocked());
        };
    };

    t2.start();
    t2.join();

    redisson.shutdown();
	}
}
