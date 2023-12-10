/*
 * Functions needs to organize and parse through the data are stored here and can be accessed by
 *      creating an AuxUtils object. Originally AuxUtils was also supposed to handle topological
 *      ordering and other bookkeeping. This was instead handle via traversal order in the CalcUtils
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class AuxUtils {
    /*
     * variable for AuxUtils
     */

    // list of parents within a graph | Key = Child | Value = ArrayList of the child's parents | No root
    HashMap<Vertex, ArrayList<Vertex>> parents = new HashMap<>();
    // list of children within the graph | Key = Parent | Value + ArrayList of all the parents children
    HashMap<Vertex, ArrayList<Vertex>> children = new HashMap<>();
    // distinct list of all vertices in the graph
    HashSet<Vertex> allVertices = new HashSet<>();
    // the root of the graph determined by finding the vertex key in parents that has a null ArrayList
    Vertex root;

    /*
     * readEdgeListFile
     *
     * Creates a parents list, a child list, find the root , and a list of all vertices.
     */
    public void readEdgeList(String fileName) throws FileNotFoundException {
        // establish scanner with the file
        Scanner scanner = new Scanner(new File(fileName));

        // for every line in the File
        while(scanner.hasNextLine()){
            // grab the line
            String line = scanner.nextLine();

            // create a vertex for the lines parent and child
            Vertex parent = new Vertex(line.substring(0, line.indexOf(' ')));
            Vertex child = new Vertex(line.substring(line.indexOf(' ') + 1));

            // add to the HashSet of all vertices
            allVertices.add(parent); allVertices.add(child);

            // Create parents set
            // if the Vertex already exists in the parents map.
            ArrayList<Vertex> parentSet;
            if(parents.containsKey(child)){ parentSet = parents.get(child); }
            // not in the parents map
            else { parentSet = new ArrayList<>(); }
            // add the current parent and place back in HashMap
            parentSet.add(parent);
            parents.put(child, parentSet);

            // Create children set
            // if the Vertex already exists in the child map.
            ArrayList<Vertex> childrenSet;
            if(children.containsKey(parent)) { childrenSet = children.get(parent); }
            // not in the children
            else { childrenSet = new ArrayList<>(); }
            // add the current child and place back in HashMap
            childrenSet.add(child);
            children.put(parent, childrenSet);
        }

        // find the root of the graph
        for(Vertex v : allVertices){
            if (parents.get(v) == null){
                root = v;
            }
        }
    }
}
