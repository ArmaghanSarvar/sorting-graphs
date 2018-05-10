import adjacencyList.AdjListGraph;
import matrix.MatrixGraph;

import java.util.Scanner;

/**
 * Created by Armaghan on 12/28/2017.
 */
public class Main  {
    private Scanner scanner= new Scanner(System.in);
    private String command;
    private String fileName;
    private AdjListGraph adjListGraph;

    public static void main(String[] args) {
        Main main = new Main();
        main.getCommand();
    }

    private void getCommand(){
        System.out.println("enter the file name please");
        fileName = scanner.nextLine();
        System.out.println("enter the command please");
        command = scanner.nextLine().toLowerCase();

        if ("find n".equals(command)){
            System.out.println("List or Matrix?");
            if (scanner.nextLine().equals("list"))
                new AdjListGraph("test8.txt");
            else
                new MatrixGraph("test8.txt");
        }
        else if ("matrix".equals(command.split(" ")[1]))
            new MatrixGraph(fileName , command);
//            System.out.println("size: " + matrixGraph.getMatrix().size());
        else if ("linkedlist".equals(command.split(" ")[1]))
             new AdjListGraph(fileName, command);

    }
}