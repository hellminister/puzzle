package puzzlegame.puzzle.factors;

public final class Factor implements Comparable<Factor>{
    
    
    private final double ratio;
    private final int x;
    private final int y;

    public Factor(int x, int y) {
        this.x = x;
        this.y = y;
        this.ratio = (double)this.x / this.y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public double getRatio() {
        return ratio;
    }

    /**
     * Permits ordering based on ratio, first factor, second factor in this order of priority
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Factor o) {
        int i = Double.compare(this.ratio, o.ratio);
        if (i == 0){
            i = Integer.compare(this.x, o.x);
            if (i == 0){
                i = Integer.compare(this.y, o.y);
            }
        }

        return i;
    }

    @Override
    public String toString() {
        return "Factor{" +
                "ratio=" + ratio +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
