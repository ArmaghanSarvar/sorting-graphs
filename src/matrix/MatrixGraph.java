package matrix;

import Graph.Graph;

import java.io.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Armaghan on 12/28/2017.
 */
public class MatrixGraph extends Graph {
    private Vector<SMValue> matrix;   // I'm using a sparse matrix
    private MatrixSort matrixSort;
    private boolean[] visited;
    private int[] degrees;
    private Scanner scanner= new Scanner(System.in);
    private String input;

    public MatrixGraph(String fileName, String sortType) {
        this.fileName = fileName;
        this.sortType = sortType;
        numOfVertices = readNumOfVertices();
        degrees = new int[numOfVertices+1];
        visited = new boolean[numOfVertices + 1];
        System.out.println("num of vertices " + numOfVertices);
        matrix = new Vector<>();
        matrixSort = new MatrixSort(this);
        fillGraph();
        countDegree();
        process();
    }

    public MatrixGraph(String fileName){
        this.fileName = fileName;
        numOfVertices = readNumOfVertices();
        matrix = new Vector<>();
        matrixSort = new MatrixSort(this);
        fillGraph();
        findIdealN();
    }

    private void findIdealN(){
        System.out.println("enter optimum type");
        input = scanner.nextLine().toLowerCase();
        if (input.equals("insertion")){
            for (int i = 2; i <= 30; i++) {
                long start = System.currentTimeMillis();
                matrixSort.setnOfOptimum(i);
                matrixSort.optimumInsertion(matrix , 0 , matrix.size()-1);
                long end = System.currentTimeMillis();
                System.out.println("for N = "+ i + " sort takes "+ (float)(end-start)/(float)1000 +" seconds");
            }
        }
        else if (input.equals("bubble")){
            for (int i = 2; i <= 30; i++) {
                long start = System.currentTimeMillis();
                matrixSort.setnOfOptimum(i);
                matrixSort.optimumBubble(matrix , 0 , matrix.size() - 1);
                long end = System.currentTimeMillis();
                System.out.println("for N = "+ i + "sort takes "+ (float)(end-start)/(float)1000 +" seconds");
            }
        }
    }

    @Override
    protected void fillGraph() {
        File file = new File(fileName);
        long start = System.currentTimeMillis();
        int[] ve = new int[2];
        try {
            Scanner filereader = new Scanner(file);
            do {
                String s = filereader.nextLine();
                ve[0] = Integer.parseInt(s.split(",")[0]);
                ve[1] = Integer.parseInt(s.split(",")[1]);
                if (ve[0] < ve[1])      // so that we won't have repeated vertices
                    matrix.add(new SMValue(ve[0], ve[1]));
            } while (filereader.hasNext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("time taken to read the file and fillTheGraph = "+ (float)(end-start)/(float)1000 +" seconds");
    }

    @Override
    protected void process() {
        int c = 1;
        long start = System.currentTimeMillis();
        while (isConnectedGraph()) {
            long start1 = System.currentTimeMillis();
            System.out.println("iteration" + c);
            setScores();
            sort(sortType);
            omit();
            c++;
            long end = System.currentTimeMillis();
            System.out.println("time taken to process 5 steps = "+ (float)(end-start1)/(float)1000 +" seconds");
        }
        writeToFile();
        long end = System.currentTimeMillis();
        System.out.println("time taken to process the main algorithm = "+ (float)(end-start)/(float)1000 +" seconds");
    }

    private void countDegree(){
        for (int i = 0; i < matrix.size(); i++) {
            degrees[matrix.get(i).getRow()]++;
            degrees[matrix.get(i).getColumn()]++;
        }
    }

    private void omit(){
        degrees[matrix.get(0).getRow()]--;
        degrees[matrix.get(0).getColumn()]--;
        matrix.remove(0);
    }

    protected void setScores(){
        long start = System.currentTimeMillis();
        for (int i = 0; i < matrix.size(); i++) {
            matrix.get(i).setWeight(calEdgeWeight(matrix.get(i).getRow(), matrix.get(i).getColumn()));
        }
        long end = System.currentTimeMillis();
        System.out.println("time taken to setScores = "+ (float)(end-start)/(float)1000 +" seconds");
    }

    private void dfs(int v){
        Stack<Integer> s = new Stack<>();
        s.push(v);
        while (!s.isEmpty()) {
            v = s.pop();
            if (!visited[v])
                visited[v] = true;
            for (int i = 0; i < matrix.size(); i++) {
                if (!visited[matrix.get(i).getColumn()] && areConnected(v , matrix.get(i).getColumn()))
                    s.push(matrix.get(i).getColumn());
                else if (!visited[matrix.get(i).getRow()] && areConnected(v, matrix.get(i).getRow()))
                    s.push(matrix.get(i).getRow());
            }
        }
    }

    @Override
    protected void writeToFile() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("result.txt"));

            for (int v = 1; v < visited.length; v++) {
                if (visited[v])
                    bw.write(String.format("#%d : A", v));
                else
                    bw.write(String.format("#%d : B", v));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isConnectedGraph(){
        for (int i = 1; i < visited.length; i++) {
            visited[i] = false;
        }
        dfs(1);
        for (int v = 1; v < visited.length ; v++) {
            if (!visited[v]){
                return false;
            }
        }
        return true;
    }

    @Override
    protected void sort(String sortType) {
        long start = System.currentTimeMillis();

        if (sortType.toLowerCase().contains("quick"))
            matrixSort.quickSort(0, matrix.size() - 1);

        else if (sortType.toLowerCase().contains("merge"))
            matrixSort.dMergeSort();

        else if (sortType.toLowerCase().contains("optimum insertion")) {
            matrixSort.setnOfOptimum(Integer.parseInt(sortType.split(" ")[4]));
            matrixSort.optimumInsertion(matrix , 0 , matrix.size() - 1);
        }
        else if (sortType.toLowerCase().contains("optimum bubble")) {
            matrixSort.setnOfOptimum(Integer.parseInt(sortType.split(" ")[4]));
            matrixSort.optimumBubble(matrix , 0 , matrix.size() - 1);
        }

        else if (sortType.toLowerCase().contains("insertion"))
            matrixSort.insertionSort();

        else if (sortType.toLowerCase().contains("bubble"))
            matrixSort.bubbleSort();
        long end = System.currentTimeMillis();
//        test();
        System.out.println("time taken to sort = "+ (float)(end-start)/(float)1000 +" seconds");
    }

    private void test(){
        for (int i=0; i< matrix.size() ; i++) {
            System.out.println(matrix.get(i).getRow() + " ->" + matrix.get(i).getColumn());
            System.out.println(matrix.get(i).getWeight());
        }
        System.exit(0);
    }

    @Override
    protected int calculateZij(int v1, int v2) {
        int numOfTriangles = 0;
        for (SMValue aMatrix : matrix) {
            if ((aMatrix.getRow() == v1 && isForTheOther(v2, aMatrix.getColumn())) ||
                    (aMatrix.getColumn() == v1 && isForTheOther(v2, aMatrix.getRow()))) {
                numOfTriangles++;
            }
        }
        return numOfTriangles;
    }

    private boolean isForTheOther(int v2, int toBeSearched) {
        for (SMValue aMatrix : matrix) {
            if ((aMatrix.getRow() == v2 && aMatrix.getColumn() == toBeSearched) ||
                    (aMatrix.getRow() == toBeSearched && aMatrix.getColumn() == v2)) {
//                System.out.println("row is"+ aMatrix.getRow() + "col is"+ aMatrix.getColumn());
                return true;
            }
        }
        return false;
    }

    protected boolean areConnected(int v1, int v2) {
        for (SMValue aMatrix : matrix) {
            if ((aMatrix.getRow() == v1 && aMatrix.getColumn() == v2) ||
                    (aMatrix.getRow() == v2 && aMatrix.getColumn() == v1))
                return true;
        }
        return false;
    }

    public float calEdgeWeight(int v1, int v2) {
        float cIJ;
        int min = Math.min(degrees[v1] - 1, degrees[v2] - 1);
        if (min == 0) {
            cIJ = 10000;
        } else {
            cIJ = (float) (calculateZij(v1, v2) + 1) / (float) min;
        }
        return cIJ;
    }

    public Vector<SMValue> getMatrix() {
        return matrix;
    }
}
