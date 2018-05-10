package adjacencyList;


import matrix.SMValue;

import java.util.Vector;


public class ListSort {
    private AdjListGraph listGraph;
    private int nOfOptimum;

    public ListSort(AdjListGraph listGraph) {
        this.listGraph = listGraph;
    }

    public void bubbleSort(){
        for (int i = 0; i < listGraph.getWeightList().size() - 1 ; i++) {
            for (int j = 0; j <  listGraph.getWeightList().size() - i - 1 ; j++) {
                if ( listGraph.getWeightList().get(j).getWeight() >  listGraph.getWeightList().get(j + 1).getWeight()) {
                    swap(j , j + 1);
                }
            }
        }
    }

    public void insertionSort(){   // O(n^2)
        int sortedS = 0;
        for(int i = 1 ; i < listGraph.getWeightList().size() ; i++){
            int j = i;
            do{
                if(listGraph.getWeightList().get(j).getWeight() < listGraph.getWeightList().get(j-1).getWeight()){ //we should swap
                    SMValue small = listGraph.getWeightList().get(j);
                    SMValue large = listGraph.getWeightList().get(j-1);
                    listGraph.getWeightList().set(j - 1 , small);
                    listGraph.getWeightList().set(j, large);
                }
                j--;
            }while(j > sortedS);
        }
    }

    public void quickSort(int low, int high) {
        int i = low, j = high;
        double pivot = listGraph.getWeightList().get(low +(high-low)/2).getWeight();
        while (i <= j) {
            while (listGraph.getWeightList().get(i).getWeight() < pivot) {
                i++;
            }
            while (listGraph.getWeightList().get(j).getWeight() > pivot) {
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

    private void insertSort(Vector<SMValue> inArray , int l , int u){
        int in, out;
        for (out = l + 1; out <= u ; out++) {
            SMValue temp = inArray.get(out);
            in = out;
            while (in > l && inArray.get(in -1).getWeight() >= temp.getWeight()) {
                inArray.set(in ,inArray.get(in - 1) );
                --in;
            }
            inArray.set(in ,temp);
        }
    }


    public void dMergeSort(){
        mergeSort(listGraph.getWeightList());
    }

    public void mergeSort(Vector<SMValue> weightL){
        if (weightL.size() == 1)
            return;
        Vector<SMValue> listFirstHalf = new Vector<>();
        Vector<SMValue> listSecondHalf = new Vector<>();
        int midIndex= (weightL.size() + 1) / 2;

        for (int i = 0; i < midIndex; i++) {
            listFirstHalf.addElement(weightL.get(i));
        }
        for (int i = midIndex ; i < weightL.size(); i++) {
            listSecondHalf.addElement(weightL.get(i));
        }
        mergeSort(listFirstHalf);
        mergeSort(listSecondHalf);
        merge(listFirstHalf , listSecondHalf, weightL);
    }

    private void merge(Vector<SMValue> left , Vector<SMValue> right, Vector<SMValue> A){
        int lengthR = right.size();
        int lengthL = left.size();

        int i = 0,k = 0, j = 0;//i : left, j : right
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

    public void optimumBubble(Vector<SMValue> toBeSorted, int low, int high) {
        if (high - low > nOfOptimum) {
            int i = low, j = high;
            SMValue pivot = toBeSorted.get(low + (high - low) / 2);
            while (i <= j) {
                while (toBeSorted.get(i).getWeight() < pivot.getWeight()) {
                    i++;
                }
                while (toBeSorted.get(j).getWeight() > pivot.getWeight()) {
                    j--;
                }
                if (i <= j) {
                    swap(i, j);
                    j--;
                    i++;
                }
            }
            if (low < j)
                optimumBubble(toBeSorted, low, j);
            if (i < high)
                optimumBubble(toBeSorted, i, high);
        } else {
            for (int i = low; i < high; i++) {
                boolean f = false;
                for (int j = low; j < high; j++) {
                    if (toBeSorted.get(j).getWeight() > toBeSorted.get(j + 1).getWeight()) {
                        f = true;
                        swap(j, j + 1);
                    }
                }
                if (!f) return;
            }
        }
    }

    public void optimumInsertion(Vector<SMValue> toBeSorted, int low, int high) {
        if (high - low > nOfOptimum) {
            int i = low, j = high;
            SMValue pivot = toBeSorted.get(low + (high - low) / 2);
            while (i <= j) {
                while (toBeSorted.get(i).getWeight() < pivot.getWeight()) {
                    i++;
                }
                while (toBeSorted.get(j).getWeight() > pivot.getWeight()) {
                    j--;
                }
                if (i <= j) {
                    swap(i, j);
                    i++;
                    j--;
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
                key = toBeSorted.get(i);
                j = i - 1;
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

    private void swap(int index1 , int index2){
        SMValue temp = listGraph.getWeightList().get(index1);
        listGraph.getWeightList().set(index1 , listGraph.getWeightList().get(index2));
        listGraph.getWeightList().set(index2 , temp);
    }

}