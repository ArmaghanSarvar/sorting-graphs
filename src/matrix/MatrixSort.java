package matrix;

import java.util.Vector;

/**
 * Created by Armaghan on 1/1/2018.
 */
public class MatrixSort {
    private MatrixGraph matrixGraph;
    private int nOfOptimum;

    public MatrixSort(MatrixGraph matrixGraph) {
        this.matrixGraph = matrixGraph;
    }

    public void bubbleSort(){
        for (int i = 0; i < matrixGraph.getMatrix().size() - 1 ; i++) {
            for (int j = 0; j < matrixGraph.getMatrix().size() - i - 1 ; j++) {
                if (matrixGraph.getMatrix().get(j).getWeight() > matrixGraph.getMatrix().get(j + 1).getWeight()) {
                    swap(j , j + 1);
                }
            }
        }
    }

    public void insertionSort(){   // O(n^2)
        int sortedS = 0;
        for(int i = 1 ; i < matrixGraph.getMatrix().size() ; i++){
            int j = i;
            do{
                if(matrixGraph.getMatrix().get(j).getWeight() < matrixGraph.getMatrix().get(j-1).getWeight()){ //we should swap
                    SMValue small = matrixGraph.getMatrix().get(j);
                    SMValue large = matrixGraph.getMatrix().get(j-1);
                    matrixGraph.getMatrix().set(j - 1 , small);
                    matrixGraph.getMatrix().set(j, large);
                }
                j--;
            }while(j > sortedS);
        }
    }

    public void quickSort(int low, int high) {
        int i = low, j = high;
        double pivot = matrixGraph.getMatrix().get(low +(high-low)/2).getWeight();
        while (i <= j) {
            while (matrixGraph.getMatrix().get(i).getWeight() < pivot) {
                i++;
            }
            while (matrixGraph.getMatrix().get(j).getWeight() > pivot) {
                j--;
            }
            if (i <= j) {
                swap(i,j);
                i++;
                j--;
            }
        }
        if (low < j)
            quickSort(low, j);
        if (i < high)
            quickSort(i, high);
    }

    public void dMergeSort(){
        mergeSort(matrixGraph.getMatrix());
    }

    public void mergeSort(Vector<SMValue> matrix){
        if (matrix.size() == 1)
            return;
        int midIndex= (matrix.size() + 1) / 2;
        Vector<SMValue> listFirstHalf = new Vector<>();
        Vector<SMValue> listSecondHalf = new Vector<>();

        for (int i = 0; i < midIndex; i++) {
            listFirstHalf.addElement(matrix.get(i));
        }
        for (int i = midIndex ; i < matrix.size(); i++) {
            listSecondHalf.addElement(matrix.get(i));
        }
        mergeSort(listFirstHalf);
        mergeSort(listSecondHalf);
        merge(listFirstHalf , listSecondHalf, matrix);
    }

    private void merge(Vector<SMValue> left , Vector<SMValue> right, Vector<SMValue> A){
        int lengthL = left.size();
        int lengthR = right.size();
        int i = 0, j = 0, k = 0;//i : left, j : right
        while (i < lengthL && j < lengthR) {
            if (left.get(i).getWeight() <= right.get(j).getWeight()) {
                A.set(k, left.get(i));
                i++;
                k++;
            }
            else {
                A.set(k, right.get(j));
                j++;
                k++;
            }
        }
        while (i < lengthL) {
            A.set(k, left.get(i));
            i++;
            k++;
        }
        while (j < lengthR) {
            A.set(k, right.get(j));
            j++;
            k++;
        }
    }

    private void swap(int index1 , int index2){
        SMValue temp = matrixGraph.getMatrix().get(index1);
        matrixGraph.getMatrix().set(index1 , matrixGraph.getMatrix().get(index2));
        matrixGraph.getMatrix().set(index2 , temp);
    }

    public void optimumBubble(Vector<SMValue> toBeS, int low, int high) {
        if (high - low > nOfOptimum) {
            int i = low, j = high;
            SMValue pivot = toBeS.get(low + (high - low) / 2);
            while (i <= j) {
                while (toBeS.get(i).getWeight() < pivot.getWeight()) {
                    i++;
                }
                while (toBeS.get(j).getWeight() > pivot.getWeight()) {
                    j--;
                }
                if (i <= j) {
                    swap(i, j);
                    i++;
                    j--;
                }
            }
            if (low < j)
                optimumBubble(toBeS, low, j);
            if (i < high)
                optimumBubble(toBeS, i, high);
        } else {
            for (int i = low; i < high; i++) {
                boolean flag = false;
                for (int j = low; j < high; j++) {
                    if (toBeS.get(j).getWeight() > toBeS.get(j + 1).getWeight()) {
                        swap(j, j + 1);
                        flag = true;
                    }
                }
                if (!flag) return;
            }
        }
    }

    public void optimumInsertion(Vector<SMValue> toBeSorted, int low, int high) {
        if (high - low > nOfOptimum) {
            int i = low, j = high;
            SMValue pivot = toBeSorted.get(low + (high - low) / 2);
            while (i <= j) {
                while (toBeSorted.get(j).getWeight() > pivot.getWeight()) {
                    j--;
                }
                while (toBeSorted.get(i).getWeight() < pivot.getWeight()) {
                    i++;
                }
                if (i <= j) {
                    swap(i, j);
                    j--;
                    i++;
                }
            }
            if (low < j)
                optimumInsertion(toBeSorted, low, j);
            if (i < high)
                optimumInsertion(toBeSorted, i, high);
        } else {
            SMValue key;
            int j;
            for (int i = low + 1; i <= high; i++) {
                j = i - 1;
                key = toBeSorted.get(i);
                while (j >= low && toBeSorted.get(j).getWeight() > key.getWeight()) {
                    toBeSorted.set(j + 1, toBeSorted.get(j));
                    j--;
                }
                toBeSorted.set(j + 1, key);
            }
        }
    }

    public void setnOfOptimum(int nOfOptimum) {
        this.nOfOptimum = nOfOptimum;
    }

}