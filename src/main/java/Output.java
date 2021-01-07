public class Output {

    private double time;
    private double straightDistance;
    private double calculatedDistance;
    private double avgVelocity;
    private String path;
    private boolean isGood;


    public Output(double time, double straightDistance, double calculatedDistance, String path) {
        this.time = round(time);
        this.straightDistance = round(straightDistance);
        this.calculatedDistance = round(calculatedDistance);
        this.avgVelocity = round(calculatedDistance / time);
        this.path = path;
        this.isGood = true;
    }

    public Output() {
        this.isGood = false;
    }

    public String getTime() {
        return String.valueOf(time);
    }

    public String getStraightDistance() {
        return String.valueOf(straightDistance);
    }

    public String getCalculatedDistance() {
        return String.valueOf(calculatedDistance);
    }

    public String getAvgVelocity() {
        return String.valueOf(avgVelocity);
    }

    public String getPath() {
        return path;
    }

    public boolean isGood() {
        return isGood;
    }

    public double round(double number) {
        return Math.round(number * 100.0) / 100.0;
    }
}
