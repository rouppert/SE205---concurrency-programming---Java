public class BoundedBufferMain {

    public static void main (String[] args) {
        BoundedBuffer buffer;

        // Check the arguments of the command line
        if (args.length != 1){
            System.out.println ("PROGRAM FILENAME");
            System.exit(1);
        }
        Utils.init(args[0]);

        Consumer consumers[] = new Consumer[(int)Utils.nConsumers];
        Producer producers[] = new Producer[(int)Utils.nProducers];
        
        // Create a buffer
        if (Utils.sem_impl == 0)
            buffer = new NatBoundedBuffer(Utils.bufferSize);
        else
            buffer = new SemBoundedBuffer(Utils.bufferSize);

        int nConsumers = (int)Utils.nConsumers;
        int nProducers = (int)Utils.nProducers;
        for (int i=0;i<nConsumers;i++) {
            Consumer consumer = new Consumer(i, buffer);
            new Thread(consumer).start();
        }
        for (int i=nConsumers;i<nConsumers+nProducers;i++) {
            Producer producer = new Producer(i-nConsumers, buffer);
            new Thread(producer).start();
        }
    }
}
