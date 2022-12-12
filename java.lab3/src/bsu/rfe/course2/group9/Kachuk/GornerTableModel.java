package bsu.rfe.course2.group9.Kachuk;

import javax.swing.table.AbstractTableModel;
import java.math.BigInteger;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    public Double getFrom() { return from; }
    public Double getTo() { return to; }
    public Double getStep() { return step; }

    public int getColumnCount() { return 3; }

    public int getRowCount() {
        return Double.valueOf(Math.ceil((to-from)/step)).intValue()+1;
    }

    public Object getValueAt(int row, int col) {
        double x = from + step * row;
        Double result = 0.0;
        switch (col) {
            case 0:
                return x;
            case 1: {
                for (int i = coefficients.length - 1; i >= 0; i--) {
                    result += coefficients[i] * Math.pow(x, coefficients.length - 1 - i);
                }
                return result;
            }
            case 2: {
                for (int i = coefficients.length - 1; i >= 0; i--) {
                    result += coefficients[i] * Math.pow(x, coefficients.length - 1 - i);
                }
                String str = String.valueOf(result);
                int counter = 0;
                int numOfDigits = 0;
                for(int i = 0; i < str.length() - 1; i++){
                    if(numOfDigits > 0){
                        numOfDigits++;
                    }
                    if(str.charAt(i) == '.'){
                        numOfDigits++;
                        continue;
                    }
                    if(numOfDigits == 4){
                        break;
                    }
                    Character c = str.charAt(i);
                    if(c.equals(str.charAt(i + 1))){
                        counter++;
                    }
                }
                if(counter >= 2){
                    return Boolean.valueOf(true);
                }
                return Boolean.valueOf(false);
            }
        }

        return result;
    }
    public String getColumnName(int col) {
        switch (col) {
            case 0: return "Value of X";
            case 1: return "Forward value";
            case 2: return "If two pairs?";
        }
        return "";
    }

    public Class<?> getColumnClass(int col) {
        if(col == 2){
            return Boolean.class;
        }
        return Double.class;
    }
}