package matrix;

/**
 * Created by Armaghan on 12/29/2017.
 */
public class SMValue {
    private int row, column;
    private float weight;

    public SMValue(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public SMValue(int row, int column, float weight) {
        this.row = row;
        this.column = column;
        this.weight = weight;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }
}