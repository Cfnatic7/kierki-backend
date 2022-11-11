package data;


public class PointWrapper {

    private int firstPlayerPoints;

    private int secondPlayerPoints;

    public PointWrapper(int firstPlayerPoints, int secondPlayerPoints) {
        this.firstPlayerPoints = firstPlayerPoints;
        this.secondPlayerPoints = secondPlayerPoints;
    }

    public void add(PointWrapper pointWrapper) {
        this.firstPlayerPoints += pointWrapper.firstPlayerPoints;
        this.secondPlayerPoints += pointWrapper.secondPlayerPoints;
    }

    public int getFirstPlayerPoint() {
        return firstPlayerPoints;
    }

    public void setFirstPlayerPoint(int firstPlayerPoint) {
        this.firstPlayerPoints = firstPlayerPoint;
    }

    public int getSecondPlayerPoints() {
        return secondPlayerPoints;
    }

    public void setSecondPlayerPoints(int secondPlayerPoints) {
        this.secondPlayerPoints = secondPlayerPoints;
    }
}
