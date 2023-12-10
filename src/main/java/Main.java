import java.io.FileNotFoundException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        /*
         * This main method calculates both the close and far assignments of terms discussed in the paper. Attempts
         *  were made to also calculate using both depth first and slow searches. However, since the BFO ontology is
         *  small, accurate measurements were not possible.
         */
        double closeKGS = getKgsBreadthFirst("src/main/resources/LaptopCorrect",
                "src/main/resources/LaptopClose");
        System.out.println("Close assignment KGS: " + closeKGS);

        double farKGS = getKgsBreadthFirst("src/main/resources/LaptopCorrect",
                "src/main/resources/LaptopFar");
        System.out.println("Far assignment KGS: " + farKGS);
    }

    /*
     * Calculates the KGS of two given graph given as edge lists, using breadth first search
     * Order of steps to find KGS
     *  create AuxUtils
     *  auxUtils read edge lists
     *  create CalcUtils
     *  get hashmap returned from a calcKS function
     *  call KGS
     */
    public static double getKgsBreadthFirst(String fileA, String fileB) throws FileNotFoundException {
        // Create representation of each graph
        AuxUtils auxUtilsA = new AuxUtils();
        auxUtilsA.readEdgeList(fileA);

        AuxUtils auxUtilsB = new AuxUtils();
        auxUtilsB.readEdgeList(fileB);

        // Create CalcUtils for each graph
        CalcUtils calcUtilsA = new CalcUtils(auxUtilsA.parents, auxUtilsA.children,
                auxUtilsA.allVertices, auxUtilsA.root);

        CalcUtils calcUtilsB = new CalcUtils(auxUtilsB.parents, auxUtilsB.children,
                auxUtilsB.allVertices, auxUtilsB.root);

        // Calculate KSV
        HashMap<String, Double> KSVA = calcUtilsA.breadthFirstCalculateKSV(auxUtilsA.root);

        HashMap<String, Double> KSVB = calcUtilsB.breadthFirstCalculateKSV(auxUtilsB.root);

        // Calculate and return KGS
        return calcUtilsA.calculateKGS(KSVA, KSVB);
    }

    public static void printGraph(AuxUtils auxUtils){
        System.out.println("Root: " + auxUtils.root);
        System.out.println("Full vertex list: " + auxUtils.allVertices);
    }
}