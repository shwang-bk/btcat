package studio.bluekitten.backtestingcat.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import studio.bluekitten.backtestingcat.exceptions.DifferDimensionException;
import studio.bluekitten.backtestingcat.exceptions.NotBooleanPresentationException;

public class Matrix1d {

    private BigDecimal[] decs;

    public Matrix1d(BigDecimal[] decs){
        this.decs = decs;
    }

    public Matrix1d(int[] integerArray){
        BigDecimal[] decs = new BigDecimal[integerArray.length];
        for(int i = 0; i < decs.length; i++)
            decs[i] = new BigDecimal(integerArray[i]);
        this.decs = decs;
    }

    public Matrix1d(double[] doubleArray){
        BigDecimal[] decs = new BigDecimal[doubleArray.length];
        for(int i = 0; i < decs.length; i++)
            decs[i] = new BigDecimal(doubleArray[i]);
        this.decs = decs;
    }

    public Matrix1d(double alpha, int dim){
        decs = new BigDecimal[dim];
        for(int i = 0; i < dim; i++){
            decs[i] = new BigDecimal(alpha);
        }
    }

    public Matrix1d(List<BigDecimal> decs){
        this.decs = decs.toArray(new BigDecimal[decs.size()]);
    }

    public BigDecimal get(int i){
        return decs[i];
    }

    public Matrix1d add(Matrix1d addends){
        checkDimensionMatch(addends);

        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                result[i] = decs[i].add(addends.get(i));
            }catch (NullPointerException e){
                result[i] = null;
            }
        }

        return new Matrix1d(result);
    }

    public Matrix1d subtract(Matrix1d subtrahends){
        checkDimensionMatch(subtrahends);

        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                result[i] = decs[i].subtract(subtrahends.get(i));
            }catch (NullPointerException e){
                result[i] = null;
            }
        }

        return new Matrix1d(result);
    }

    public Matrix1d multiply(Matrix1d multipliers){
        checkDimensionMatch(multipliers);

        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                result[i] = decs[i].multiply(multipliers.get(i));
            }catch (NullPointerException e){
                result[i] = null;
            }
        }

        return new Matrix1d(result);
    }

    public Matrix1d divide(Matrix1d divisors){
        checkDimensionMatch(divisors);

        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                result[i] = decs[i].divide(divisors.get(i), 4, BigDecimal.ROUND_HALF_UP);
            }catch (NullPointerException ne){
                result[i] = null;
            }catch (ArithmeticException ae){
                result[i] = BigDecimal.ZERO;
            }
        }

        return new Matrix1d(result);
    }

    public Matrix1d not() {
        checkValueBooleanPresentation(this);
        Matrix1d resultWithNull = this.subtract(new Matrix1d(1, decs.length))
                .multiply(new Matrix1d(-1, decs.length));

        // 把存在的空值改為0
        return new Matrix1d(resultWithNull.toIntegerArray());
    }

    public Matrix1d and(Matrix1d matrix) {
        checkDimensionMatch(matrix);
        checkValueBooleanPresentation(this);
        checkValueBooleanPresentation(matrix);

        Matrix1d resultWithNull = this.multiply(matrix);

        // 把存在的空值改為0
        return new Matrix1d(resultWithNull.toIntegerArray());
    }

    public Matrix1d or(Matrix1d matrix) {
        checkDimensionMatch(matrix);
        checkValueBooleanPresentation(this);
        checkValueBooleanPresentation(matrix);

        Matrix1d notA = this.not();
        Matrix1d notB = matrix.not();
        Matrix1d notAAndNotB = notA.and(notB);
        Matrix1d resultWithNull = notAAndNotB.not();

        // 把存在的空值改為0
        return new Matrix1d(resultWithNull.toIntegerArray());
    }

    public Matrix1d moreThan(double value){
        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                if(decs[i].doubleValue() > value)
                    result[i] = new BigDecimal("1");
                else
                    result[i] = new BigDecimal("0");
            }catch (NullPointerException e){
                result[i] = null;
            }
        }
        return new Matrix1d(result);
    }

    public Matrix1d moreThan(Matrix1d matrix) {
        checkDimensionMatch(matrix);

        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                if(decs[i].doubleValue() > matrix.get(i).doubleValue())
                    result[i] = new BigDecimal("1");
                else
                    result[i] = new BigDecimal("0");
            }catch (NullPointerException e){
                result[i] = null;
            }
        }
        return new Matrix1d(result);
    }

    public Matrix1d lessThan(double value) {
        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                if(decs[i].doubleValue() < value)
                    result[i] = new BigDecimal("1");
                else
                    result[i] = new BigDecimal("0");
            }catch (NullPointerException e){
                result[i] = null;
            }
        }
        return new Matrix1d(result);
    }

    public Matrix1d lessThan(Matrix1d matrix) {
        checkDimensionMatch(matrix);

        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                if(decs[i].doubleValue() < matrix.get(i).doubleValue())
                    result[i] = new BigDecimal("1");
                else
                    result[i] = new BigDecimal("0");
            }catch (NullPointerException e){
                result[i] = null;
            }
        }
        return new Matrix1d(result);
    }

    public Matrix1d equalTo(Matrix1d matrix) {
        checkDimensionMatch(matrix);

        BigDecimal[] result = new BigDecimal[decs.length];
        for(int i = 0; i < decs.length; i++){
            try {
                if(decs[i].doubleValue() == matrix.get(i).doubleValue())
                    result[i] = new BigDecimal("1");
                else
                    result[i] = new BigDecimal("0");
            }catch (NullPointerException e){
                result[i] = null;
            }
        }
        return new Matrix1d(result);
    }

    private void checkDimensionMatch(Matrix1d matrix){
        if(decs.length != matrix.size()){
            throw new DifferDimensionException("Matrix1d calculator Error: different dimension");
        }
    }

    private void checkValueBooleanPresentation(Matrix1d matrix){
        for(int i = 0; i < decs.length; i++){
            if(matrix.get(i) == null)
                continue;

            double uncheckedValue = matrix.get(i).doubleValue();
            if (uncheckedValue != 0 && uncheckedValue != 1)
                throw new NotBooleanPresentationException(
                        "Matrix1d calculator Error: the presentation of values is not BOOLEAN");

        }
    }

    public Matrix1d shift(int offset){
        BigDecimal[] result = new BigDecimal[decs.length];

        for(int i = 0; i < decs.length; i++){
            if(i < offset || i >= (decs.length + offset))
                result[i] = null;
            else
                result[i] = decs[i - offset];
        }

        return new Matrix1d(result);
    }

    public Matrix1d simpleRegressionLine(){
        Matrix1d ONE = new Matrix1d(1, size());
        Matrix1d x = ONE.cumsum().subtract(ONE);

        BigDecimal xBar = x.mean();
        Matrix1d xDiff = x.subtract(new Matrix1d(xBar.doubleValue(), size()));

        BigDecimal yBar = this.mean();
        Matrix1d yDiff = this.subtract(new Matrix1d(yBar.doubleValue(), size()));

        BigDecimal beta = xDiff.multiply(yDiff).sum()
                .divide(xDiff.multiply(xDiff).sum(), 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal alpha = yBar.subtract(beta.multiply(xBar));

        // y = a + bx
        return x.multiply(new Matrix1d(beta.doubleValue(), size()))
                .add(new Matrix1d(alpha.doubleValue(), size()));

    }

    public Matrix1d cumsum(){
        BigDecimal current = BigDecimal.ZERO;
        BigDecimal[] result = new BigDecimal[decs.length];

        for(int i = 0; i < decs.length; i++){
            if(decs[i] != null){
                current = current.add(decs[i]);
                result[i] = current;
            }
        }
        return new Matrix1d(result);
    }

    public Matrix1d sqrt(){
        int idx = 0;
        double[] result = new double[decs.length];
        for(BigDecimal decimal : decs)
            result[idx++] = Math.sqrt(decimal.doubleValue());
        return new Matrix1d(result);
    }

    public Matrix1d subMatrix(int from, int to){
        List<BigDecimal> newDecs = new ArrayList();
        for(int i = from; i <= to; i++)
            newDecs.add(decs[i]);

        return new Matrix1d(newDecs.toArray(new BigDecimal[newDecs.size()]));
    }

    public BigDecimal sum(){
        BigDecimal total = BigDecimal.ZERO;
        for(BigDecimal dec : decs)
            if(dec != null)
                total = total.add(dec);

        return total;
    }

    public BigDecimal mean(){
        BigDecimal total = sum();
        total = total.divide(new BigDecimal(decs.length), 4, RoundingMode.HALF_UP);

        return total;
    }

    public BigDecimal std(){
        Matrix1d meanMatrix = new Matrix1d(this.mean().doubleValue(), size());
        Matrix1d diffMatrix = this.subtract(meanMatrix);
        Matrix1d squareDiff = diffMatrix.multiply(diffMatrix);
        BigDecimal avgOfSquareDiff = squareDiff.sum()
                .divide(new BigDecimal(size()), 5, RoundingMode.HALF_UP);
        return new BigDecimal(Math.sqrt(avgOfSquareDiff.doubleValue()));
    }

    public BigDecimal max(){
        double max = Double.MIN_VALUE;
        for(BigDecimal bigDecimal : decs){
            if(bigDecimal.doubleValue() > max)
                max = bigDecimal.doubleValue();
        }
        return new BigDecimal(max);
    }

    public BigDecimal min(){
        double min = Double.MAX_VALUE;
        for(BigDecimal bigDecimal : decs){
            if(bigDecimal.doubleValue() < min)
                min = bigDecimal.doubleValue();
        }
        return new BigDecimal(min);
    }

    public int size(){
        return decs.length;
    }

    public int nonZeroSize(){
        int count = 0;
        for(BigDecimal d : decs)
            if(d != null && d.doubleValue() != 0)
                count++;
        return count;
    }

    public BigDecimal[] toBigDecimalArray(){
        return decs;
    }

    public Matrix1d[] rolling(int offset){
        Matrix1d[] rollingArray = new Matrix1d[decs.length];
        for(int i = 0; i < decs.length; i++){
            if(i < offset - 1)
                rollingArray[i] = subMatrix(0, i);
            else
                rollingArray[i] = subMatrix(i - offset + 1, i);
        }
        return rollingArray;
    }

    public double[] toDoubleArray(){
        double[] arr = new double[decs.length];
        for(int i = 0; i < arr.length; i++){
            if(decs[i] == null)
                arr[i] = Double.NaN;
            else
                arr[i] = decs[i].doubleValue();
        }
        return arr;
    }

    public float[] toFloatArray(){
        float[] arr = new float[decs.length];
        for(int i = 0; i < arr.length; i++){
            if(decs[i] == null)
                arr[i] = Float.NaN;
            else
                arr[i] = decs[i].floatValue();
        }
        return arr;
    }

    public int[] toIntegerArray(){
        int[] arr = new int[decs.length];
        for(int i = 0; i < arr.length; i++){
            if(decs[i] == null)
                arr[i] = 0;
            else
                arr[i] = decs[i].intValue();
        }
        return arr;
    }

    @Override
    public String toString(){
        String str = "";
        boolean first = true;
        for(BigDecimal dec : decs){
            if(dec != null)
                dec = dec.setScale(2, BigDecimal.ROUND_HALF_UP);

            if(first){
                first = false;
                str += dec;
            }
            else
                str +=  "," + dec;
        }
        return str;
    }
}
