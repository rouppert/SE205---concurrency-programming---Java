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
      }
      return done;
   }

   // Extract an element from buffer. If the attempted operation is not
   // possible immedidately, the method call blocks until it is, but
   // waits no longer than the given deadline. Return the element if
   // successful. Otherwise, return NULL.
   synchronized Object poll(long deadline) {
      while(size==0) {
         try{wait(deadline - System.currentTimeMillis());break;}
         catch(Exception e) {}
      }
      if(size==0) return null;
      else {
         if(size==maxSize) notifyAll();
         return super.get();
      }
   }

   // Insert an element into buffer. If the attempted operation is not
   // possible immedidately, the method call blocks until it is, but
   // waits no longer than the given deadline. Return 0 if not
   // successful. Otherwise, return 1.
   synchronized boolean offer(Object value, long deadline) {
      while(size==maxSize) {
         try{wait(deadline - System.currentTimeMillis());break;}
         catch(Exception e) {}
      }
      if(size==maxSize) return false;
      else {
         super.put(value);
         if(size==0) notifyAll();
         return true;
      }
   }
}
