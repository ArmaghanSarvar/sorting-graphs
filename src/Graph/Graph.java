package Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Armaghan on 12/28/2017.
 */

public class Graph {

    public int numOfVertices;
    protected String fileName;
    protected String sortType;
    protected void fillGraph(){};
    protected int calculateZij(int v1 , int v2){return 0;}
    protected int countDegree(int v){return 0;}
    protected boolean areConnected(int v1 , int v2){return false;}
    protected void setScores(){}
    protected void process(){}
    protected void sort(String sortType){}
    protected void writeToFile(){}

    protected int readNumOfVertices() {
        File file = new File(fileName);
        int num = 0;
        int[] ve = new int[2];
        try {
            Scanner filereader = new Scanner(file);
            do {
                String s = filereader.nextLine();
                ve[0] = Integer.parseInt(s.split(",")[0]);
                ve[1] = Integer.parseInt(s.split(",")[1]);
                if (ve[0] > ve[1] && ve[0] > num)
                    num = ve[0];
                else if(ve[1] > ve[0] && ve[1] > num)
                    num = ve[1];
            } while (filereader.hasNext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return num;
    }
}