package cas.fenix.util;

public class ArrayBlockingQueueCAS {
    private byte[][] Array;
    private int      tail;
    private int      head;
    private Object   lock = new Object();
    
    public ArrayBlockingQueueCAS(final int size) {
        tail = 0;
        head = 0;
        Array = new byte[size][];
    }
    
    public void add(final byte[] value) throws InterruptedException {
        synchronized (lock) {
            Array[head] = value;
            head++;
            if (head >= Array.length) {
                head = 0;
            }
            verify();
        }
    }
    
    public byte[] get() throws InterruptedException {
        byte[] result;
        synchronized (lock) {
            if (head != tail) {
                result = Array[tail];
                tail++;
                if (tail >= Array.length) {
                    tail = 0;
                }
                return result;
            }
        }
        return null;
    }
    
    private void verify() {
        if (head == tail) {
            tail++;
            if (tail >= Array.length) {
                tail = 0;
            }
        }
    }
}
