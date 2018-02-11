package Elements;


    /**
     * This Pair class is used to combine two objects into one.
     * @param <T> the first type of object
     * @param <T2> the second type of object
     */
    public class Pair<T, T2> {

        public T first;
        public T2 second;

        /**
         * 
         * @param first the first object of the pair
         * @param second the second object of the pair
         */
      public Pair(T first, T2 second) {
            this.first = first;
            this.second = second;
        }
        
        
    }