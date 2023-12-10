import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // read in the edge lists which also creates the parents list
        AuxUtils auxUtilsA = new AuxUtils();
//        auxUtilsA.readEdgeList("src/main/resources/sample_taxonomy_a");
        auxUtilsA.readEdgeList("src/main/resources/BFOOwlA");
        HashMap<Vertex, ArrayList<Vertex>> parentsA = auxUtilsA.parents;
        HashMap<Vertex, ArrayList<Vertex>> childrenA = auxUtilsA.children;
        HashSet<Vertex> allVerticesA = auxUtilsA.allVertices;
        Vertex rootA = auxUtilsA.root;

        // read in the edge lists which also creates the parents list for graph B
        AuxUtils auxUtilsB = new AuxUtils();
//        auxUtilsB.readEdgeList("src/main/resources/sample_taxonomy_b");
        auxUtilsB.readEdgeList("src/main/resources/BFOOwlB");
        HashMap<Vertex, ArrayList<Vertex>> parentsB = auxUtilsB.parents;
        HashMap<Vertex, ArrayList<Vertex>> childrenB = auxUtilsB.children;
        HashSet<Vertex> allVerticesB = auxUtilsB.allVertices;
        Vertex rootB = auxUtilsB.root;

        System.out.println("-----");
        printGraph(auxUtilsA);
        System.out.println("-----");
        printGraph(auxUtilsB);
        System.out.println("-----");

        // calculate ksv of each graph
        CalcUtils graphACalcUtils = new CalcUtils(parentsA, childrenA, allVerticesA, rootA, 0.05, 0.1);
        CalcUtils graphBCalcUtils = new CalcUtils(parentsB, childrenB, allVerticesB, rootB, 0.05, 0.1);

//        System.out.println("--- depthFirstKVSA ---");
//        HashMap<String, Double> depthFirstKSVA = graphACalcUtils.depthFirstCalculateKSV(rootA);
//        System.out.println("--- depthFirstKVSB ---");
//        HashMap<String, Double> depthFirstKSVB = graphBCalcUtils.depthFirstCalculateKSV(rootB);

        System.out.println("--- depthFirstKVSA ---");
        HashMap<String, Double> breadthFirstKSVA = graphACalcUtils.breadthFirstCalculateKSV(rootA);
        System.out.println("--- depthFirstKVSB ---");
        HashMap<String, Double> breadthFirstKSVB = graphBCalcUtils.breadthFirstCalculateKSV(rootB);

        System.out.println("--- slowKSVA ---");
        HashMap<String, Double> slowKSVA = graphACalcUtils.slowCalculateKSV();
        System.out.println("--- slowKSVB ---");
        HashMap<String, Double> slowKSVB = graphBCalcUtils.slowCalculateKSV();

//        System.out.println("breadthFirstKSVA: " + breadthFirstKSVA);
//        System.out.println("breadthFirstKSVB: " + breadthFirstKSVB);

        // calculate KGS
//        double ksg = graphACalcUtils.calculateKGS(slowKSVA, slowKSVB);
//        double ksg = graphACalcUtils.calculateKGS(depthFirstKSVA, depthFirstKSVB);
        double kgs = graphACalcUtils.calculateKGS(breadthFirstKSVA, breadthFirstKSVB);
        System.out.println("\nThe KGS = " + kgs);
    }

    public static void printGraph(AuxUtils auxUtils){
        System.out.println("Root: " + auxUtils.root);
        System.out.println("Full vertex list: " + auxUtils.allVertices);
    }
}