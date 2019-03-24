package NDL_JavaClassLib;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Balaji
 */
public class BinnerWorker {

    /**
     * @return the binWnd
     */
    public double getBinWnd() {
        return binWnd;
    }

    /**
     * @param binWnd the binWnd to set
     */
    public void setBinWnd(double binWnd) {
        this.binWnd = binWnd;
    }

    /**
     * @return the binStart
     */
    public double getBinStart() {
        return binStart;
    }

    /**
     * @param binStart the binStart to set
     */
    public void setBinStart(double binStart) {
        this.binStart = binStart;
    }

    /**
     * @return the binEnd
     */
    public double getBinEnd() {
        return binEnd;
    }

    /**
     * @param binEnd the binEnd to set
     */
    public void setBinEnd(double binEnd) {
        this.binEnd = binEnd;
    }
    
    TraceData rawData;
    TraceData binData;
    
    private double binWnd = 0;
    
    private double binStart = 0;
    private double binEnd = 0; 
    
    
    
    
    
}
