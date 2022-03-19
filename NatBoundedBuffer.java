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
    Object get() {
      synchronized(this.content) {
         while(size<=0) {
            try{this.content.wait();}
            catch(Exception e) {}
         }
         if(size<=maxSize) this.content.notifyAll();
         return super.get();
      }
    }

    // Insert an element into buffer. If the attempted operation is
    // not possible immedidately, the method call blocks until it is.
    boolean put(Object value) {
      synchronized(this.content) {
         while(size>=maxSize) {
            try{this.content.wait();}
            catch(Exception e) {}
         }
         super.put(value);
         if(size>0) {this.content.notifyAll();}
         return true;
      }
    }

    // Extract an element from buffer. If the attempted operation is not
    // possible immedidately, return NULL. Otherwise, return the element.
    Object remove() {
      synchronized(this.content) {
         while(size<=0) {
            try{this.content.wait();}
            catch(Exception e) {}
         }
         size--;
         if(size<maxSize) this.content.notifyAll();
         return super.get();
       }
    }

    // Insert an element into buffer. If the attempted operation is
    // not possible immedidately, return 0. Otherwise, return 1.
    boolean add(Object value) {
       boolean done;

       // Enter mutual exclusion
            
          // Signal or broadcast that a full slot is available (if needed)

          return super.put(value);

          // Leave mutual exclusion
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
