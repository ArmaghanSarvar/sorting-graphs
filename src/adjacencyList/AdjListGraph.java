package adjacencyList;

import Graph.Graph;
import matrix.SMValue;

import java.io.*;
import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

/**
 * Created by Armaghan on 12/28/2017.
 */

public class AdjListGraph extends Graph{
    private AdjListNode[] adjList;
    private Vector<SMValue> weightList;
    private ListSort listSort;
    private boolean[] visited;
    private String command;
    private Scanner scanner= new Scanner(System.in);

    public AdjListGraph(String fileName, String sortType) {   // each node of array has degree
        this.fileName = fileName;
        this.sortType = sortType;
        numOfVertices = readNumOfVertices();
        System.out.println("num of vertices is"+ numOfVertices);
        weightList = new Vector<>();
        visited = new boolean[numOfVertices + 1];
        adjList = new AdjListNode[numOfVertices + 1];
        listSort = new ListSort(this);
        fillGraph();
        process();
    }

    public AdjListGraph(String fileName){
        this.fileName = fileName;
        numOfVertices = readNumOfVertices();
        adjList = new AdjListNode[numOfVertices + 1];
        weightList = new Vector<>();
        listSort = new ListSort(this);
        fillGraph();
        findIdealN();
    }

    private void findIdealN(){
        System.out.println("enter optimum type");
        command = scanner.nextLine().toLowerCase();
        if (command.equals("insertion")){
            for (int i = 2; i <= 30; i++) {
                long start = System.currentTimeMillis();
                listSort.setnOfOptimum(i);
                listSort.optimumInsertion(weightList , 0 , weightList.size()-1);
                long end = System.currentTimeMillis();
                System.out.println("for N = "+ i + " sort takes "+ (float)(end-start)/(float)1000 +" seconds");
            }
        }
        else if (command.equals("bubble")){
            for (int i = 2; i <= 30; i++) {
                long start = System.currentTimeMillis();
                listSort.setnOfOptimum(i);
                listSort.optimumBubble(weightList , 0 , weightList.size() - 1);
                long end = System.currentTimeMillis();
                System.out.println("for N = "+ i + "sort takes "+ (float)(end-start)/(float)1000 +" seconds");
            }
        }

    }

    @Override
    protected void fillGraph() {
        File file = new File(fileName);
        int[] ve = new int[2];
        try {
            Scanner filereader = new Scanner(file);
            do {
                String s= filereader.nextLine();
                ve[0] = Integer.parseInt(s.split(",")[0]);
                ve[1] = Integer.parseInt(s.split(",")[1]);
                if (ve[0] < ve[1])      // so that we won't have repeated vertices
                    weightList.add(new SMValue(ve[0], ve[1]));
                AdjListNode newNode = new AdjListNode(ve[1]);
                if (adjList[ve[0]] == null) {
                    adjList[ve[0]] = newNode;
                }
                else {
                    newNode.next = adjList[ve[0]];
                    adjList[ve[0]]= newNode;
                }
            }while (filereader.hasNext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void process() {
        int c = 1;
        long start = System.currentTimeMillis();

        while (isConnectedGraph() ) {
            long start1 = System.currentTimeMillis();
            System.out.println("iteration " + c);
            setScores();
            sort(sortType);
            omit(weightList.get(0).getRow(), weightList.get(0).getColumn());
            omit(weightList.get(0).getColumn(), weightList.get(0).getRow());
            c++;
           long end = System.currentTimeMillis();
           System.out.println("time taken to process 5 steps = "+ (float)(end-start1)/(float)1000 +" seconds");
        }
        long end = System.currentTimeMillis();
        System.out.println("time taken to process the main algorithm = "+ (float)(end-start)/(float)1000 +" seconds");
        System.out.println("end");
        writeToFile();
    }

    @Override
    protected void setScores() {
        SMValue smValue = null;
        AdjListNode node;
        weightList = new Vector<>();
        for (int i = 1; i < adjList.length; i++) {
            node = adjList[i];
            while (node != null) {
                if (i < node.vertexID) {
                    smValue = new SMValue(i, node.vertexID, calEdgeWeight(i, node.vertexID));
                    weightList.add(smValue);
                }
                node = node.next;
            }
        }
    }

    @Override
    protected void sort(String sortType) {
        long start = System.currentTimeMillis();

        if (sortType.toLowerCase().contains("quick"))
            listSort.quickSort(0 , weightList.size() - 1);

        else if (sortType.toLowerCase().contains("optimum insertion")) {
            listSort.setnOfOptimum(Integer.parseInt(sortType.split(" ")[4]));
            listSort.optimumInsertion(weightList , 0 , weightList.size() - 1);
        }

        else if (sortType.toLowerCase().contains("optimum bubble")) {
            listSort.setnOfOptimum(Integer.parseInt(sortType.split(" ")[4]));
            listSort.optimumBubble(weightList, 0 , weightList.size() - 1);
        }

        else if (sortType.toLowerCase().contains("merge")) {
            listSort.dMergeSort();
        }

        else if (sortType.toLowerCase().contains("insertion"))
            listSort.insertionSort();

        else if (sortType.toLowerCase().contains("bubble"))
            listSort.bubbleSort();

        long end = System.currentTimeMillis();
//        test();
        System.out.println("time taken to sort = "+ (float)(end-start)/(float)1000 +" seconds");
    }

    public void test() {
                for (int i = 0; i < weightList.size() ; i++) {
                    System.out.printf("(%d, %d) : %f\n", weightList.get(i).getRow(),
                            weightList.get(i).getColumn(),
                            weightList.get(i).getWeight());
            }
    }

    private void omit(int i, int j){
        AdjListNode node = adjList[i];
        AdjListNode q = node;
        while (node != null) {
            if (node.vertexID == j) {
                if (node == adjList[i])
                    adjList[i] = node.next;
                else
                    q.next= node.next;
            }
            q = node;
            node = node.next;
        }
    }

    private void dfs(int v){
        Stack<Integer> s = new Stack<>();
        AdjListNode p;
        s.push(v);
        while (!s.isEmpty()) {
            v = s.pop();
            if (!visited[v])
                visited[v] = true;
                p = adjList[v];
            while (p != null) {
                v = p.vertexID;
                if (!visited[v])
                    s.push(v);
                p = p.next;
            }
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
    protected int calculateZij(int v1, int v2) {
        int numOfTriangles = 0;

        AdjListNode temp = adjList[v1];

        while (temp != null) {
            if (inVertexsub(v2 , temp.vertexID))
                numOfTriangles ++;
            temp = temp.next;
        }
        return numOfTriangles;
    }

    protected boolean areConnected(int v1 , int v2){
        AdjListNode temp = adjList[v1];
        while (temp != null){
            if (temp.vertexID == v2){
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    private boolean inVertexsub(int v2, int toBeSearched){
        AdjListNode temp = adjList[v2];
        while (temp != null){
            if (temp.vertexID == toBeSearched)
                return true;
            temp = temp.next;
        }
        return false;
    }

    @Override
    protected int countDegree(int v) {
        int num = 0;
        AdjListNode temp = adjList[v];
        while (temp != null){
            num++;
            temp = temp.next;
        }
        return num;
    }

    public float calEdgeWeight(int v1, int v2) {
        float cIJ ;
        int min = Math.min(countDegree(v1) - 1 , countDegree(v2) - 1);
        if (min == 0){
            cIJ = 10000;
        }
        else {
            cIJ = (float)(calculateZij(v1, v2) + 1) / (float)min;
        }
        return cIJ;
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

    public Vector<SMValue> getWeightList() {
        return weightList;
    }
}