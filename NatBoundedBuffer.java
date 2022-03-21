import java.util.concurrent.Semaphore;
import java.lang.InterruptedException;
import java.util.concurrent.TimeUnit;

class NatBoundedBuffer extends BoundedBuffer {

    // Initialise the protected buffer structure above. 
    NatBoundedBuffer (int maxSize) {
        super(maxSize);
    }

    // Extract an element from buffer. If the attempted operation is
    // not possible immediately, the method call blocks until it is.
    synchronized Object get() {
         while(size==0) {
            try{wait();}
            catch(Exception e) {}
         }
         if(size==maxSize) notifyAll();
         return super.get();
    }

    // Insert an element into buffer. If the attempted operation is
    // not possible immedidately, the method call blocks until it is.
    synchronized boolean put(Object value) {
         while(size==maxSize) {
            try{wait();}
            catch(Exception e) {}
         }
         if(size==0) {notifyAll();}
         super.put(value);
         return true;
    }

    // Extract an element from buffer. If the attempted operation is not
    // possible immedidately, return NULL. Otherwise, return the element.
    synchronized Object remove() {
         if(size==0) return null;
         else {
            notifyAll();
            return super.get();
         }
    }

    // Insert an element into buffer. If the attempted operation is
    // not possible immedidately, return 0. Otherwise, return 1.
    synchronized boolean add(Object value) {
         boolean done;
         if(size==maxSize) done = false;
         else {
            done = true;
            super.put(value);
            notifyAll();
         }
         return done;
    }

    // Extract an element from buffer. If the attempted operation is not
    // possible immedidately, the method call blocks until it is, but
    // waits no longer than the given deadline. Return the element if
    // successful. Otherwise, return NULL.
    Object poll(long deadline) {
       long    timeout;

       // Enter mutual exclusion

          // Wait until a full slot is available but wait
          // no longer than the given deadline
    
          if (size == 0) return null;

          // Signal or broadcast that an full slot is available (if needed)

          return super.get();

          // Leave mutual exclusion 
    }

    // Insert an element into buffer. If the attempted operation is not
    // possible immedidately, the method call blocks until it is, but
    // waits no longer than the given deadline. Return 0 if not
    // successful. Otherwise, return 1.
    boolean offer(Object value, long deadline) {
       long    timeout;

       // Enter mutual exclusion

          // Wait until a empty slot is available but wait
          // no longer than the given deadline
    
          if (size == maxSize) return false;

          // Signal or broadcast that an empty slot is available (if needed)

          super.put(value);

          // Leave mutual exclusion
       return true;
    }
}
