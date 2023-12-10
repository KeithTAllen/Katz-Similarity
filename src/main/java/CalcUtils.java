import java.util.*;

public class CalcUtils {

    // instance variables
    HashSet<Vertex> allVertices;
    HashMap<String, Double> katzSimilarities;
    HashMap<Vertex, ArrayList<Vertex>> parents;
    HashMap<Vertex, ArrayList<Vertex>> children;
    HashMap<String, Double> ksv;
    Vertex root;
    double alpha = 0.05;
    double gamma = 0.05;

    /*
     * Constructors
     */

    // Used default value of 0.05 for alpha and gamma
    public CalcUtils(HashMap<Vertex, ArrayList<Vertex>> parents, HashMap<Vertex, ArrayList<Vertex>> children, HashSet<Vertex> allVertices, Vertex root){
        this.katzSimilarities = new HashMap<>();
        this.ksv = new HashMap<>();
        this.parents = parents;
        this.children = children;
        this.allVertices = allVertices;
        this.root = root;
    }

    // sets alpha and gamma to the given parameters
    public CalcUtils(HashMap<Vertex, ArrayList<Vertex>> parents, HashMap<Vertex, ArrayList<Vertex>> children, HashSet<Vertex> allVertices, Vertex root , double alpha, double gamma){
        this.katzSimilarities = new HashMap<>();
        this.ksv = new HashMap<>();
        this.parents = parents;
        this.children = children;
        this.allVertices = allVertices;
        this.root = root;
        this.alpha = alpha;
        this.gamma = gamma;
    }

    /*
     * Used to calculate the Katz Similarity Vector (ksv). Does assume that parents is an exhaustive
     *      list of vertices from the graph. Each graph gets its own calcUtils
     */
    public HashMap<String, Double> slowCalculateKSV(){
        HashMap<String, Double> slowKsv = new HashMap<>();
        // for every vertex u in the graph
        for (Vertex u : allVertices) {
            // for every vertex v in the graph
            for (Vertex v: allVertices){
                // create the key for the ksv
                String key = u.toString() + v.toString();
                // calculate ks(u,v,alpha) and place it in the hashmap
                slowKsv.put(key, calculateKS(u,v));
//                System.out.println(key + "|" + ksv.get(key));
            }
        }
        return slowKsv;
    }

    // depthFirst search of the child nodes
    public HashMap<String, Double> depthFirstCalculateKSV(Vertex u){
        // for every vertex
        for (Vertex v : allVertices) {
            // create the key for the ksv
            String key = u.toString() + v.toString();
            // calculate ks(u,v,alpha) and place it in the hashmap
            ksv.put(key, calculateKS(u,v));
        }
        // base case: if u has no children
        if(children.get(u) == null || children.get(u).isEmpty()) {
            // do nothing
        }
        // recursive step
        else {
            // for each of the children
            for (Vertex v : children.get(u)) {
                // call this recursively on all the child's children
                depthFirstCalculateKSV(v);
            }
        }
        // once done return the ksv
        return ksv;
    }

    // breadthFirst search of the child nodes
    public HashMap<String, Double> breadthFirstCalculateKSV(Vertex root){
        // create a queue of what node to visit next and a HashSet of nodes we have visited
        Queue<Vertex> vertexQueue = new LinkedList<>();
        HashSet<Vertex> visited = new HashSet<>();
        HashMap<String, Double> tempKSV = new HashMap<>();

        // add the root to the queue
        visited.add(root);
        vertexQueue.add(root);

        // while we still have nodes to visit
        while ( !vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            for (Vertex m : allVertices) {
                // create the key for the ksv
                String key = u.toString() + m.toString();
                // calculate ks(u,v) and place it in the hashmap
                tempKSV.put(key, calculateKS(u, m));
            }

            // if u has no children we have nothing to do. Skip ahead to next dequeue
            if(children.get(u) == null || children.get(u).isEmpty()) {
                // do nothing
            }

            // otherwise u has children, then we need to add them to the queue.
            else {
                for ( Vertex v : children.get(u)) {
                    // if we have already visited v
                    if ( !visited.contains(v)) {
                        // we have visited v
                        visited.add(v);
                        // add v to the queue, so we can do the same in the next run.
                        vertexQueue.add(v);
                    }
                }
            }
        }
        // once done return the ksv
        return tempKSV;
    }

    // calculates the KS of two given nodes using the global alpha
    public double calculateKS(Vertex u, Vertex v){
        String key = u.toString() + v.toString();
        // base case
        if(u.equals(v)){ return 0.0; }
        // the value has already been calculated, just return it
        else if(katzSimilarities.containsKey(key)){ return katzSimilarities.get(key); }
        // otherwise calculate the value
        else {
            double parentsSum = 0;
            if (parents.get(v) == null) { return 0; } // special root case
            // get the sum of the values of all the parents
            for ( Vertex p : parents.get(v) ) {
                parentsSum += calculateKS(u, p);
            }
            // calculate the final katz similarity and place in the map
            double katzSim = (alpha * parentsSum) + (alpha * isConnected(u,v));
            katzSimilarities.put(key, katzSim);
            return katzSim;
        }
    }

    // returns 1 if there is a path from parent to child returns 0 otherwiseA
    private double isConnected(Vertex u, Vertex v) {
        if (parents.get(v).contains(u))
            return 1.0;
        else
            return 0.0;
    }

    // calculate the Katz Graph Similarity measurement
    public double calculateKGS(HashMap<String, Double> ksva, HashMap<String, Double> ksvb){
        double denominator = (1 + (Math.exp(gamma * lpNorm(ksva, ksvb))));
        return 2/denominator;
    }

    private double lpNorm(HashMap<String, Double> ksva, HashMap<String, Double> ksvb) {
        double total = 0.0;
        for (String key : ksva.keySet()){
            try {
                double v = Math.abs(ksva.get(key)) - Math.abs(ksvb.get(key));
                total += v;
            }
            // if the current vertex pair is not one of the ksvs
            catch (NullPointerException n){
                System.out.println(n + "KSV is not complete");
                System.out.println(key);
            }
        }
        return total;
    }
}