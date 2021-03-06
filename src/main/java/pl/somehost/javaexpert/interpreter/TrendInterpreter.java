package pl.somehost.javaexpert.interpreter;

import pl.somehost.javaexpert.Commander;


public class TrendInterpreter {

    private int resistanceSize;
    private int supportSize;


    public TrendInterpreter(int resistanceSize, int supportSize) {
        this.supportSize = supportSize;
        this.resistanceSize = resistanceSize;
    }


    public String getTrend() {
        if (resistanceSize > supportSize) {
            return Commander.showDownTrend();
        }

        if (resistanceSize < supportSize) {
            return Commander.showDownTrend();
        }
        return Commander.noTrend();
    }
}
