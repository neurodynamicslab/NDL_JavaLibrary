/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NDL_JavaClassLib;
/**
 * Class to hold, initialize and create heat map pixel array from  time series XY data. 
 * @author balam
 */
public class JHeatMapArray extends Object{

    /**
     * @return the xRes
     */
    public int getxRes() {
        return xRes;
    }

    /**
     * @param xRes the xRes to set
     */
    public void setxRes(int xRes) {
        this.xRes = xRes;
    }

    /**
     * @return the yRes
     */
    public int getyRes() {
        return yRes;
    }

    /**
     * @param yRes the yRes to set
     */
    public void setyRes(int yRes) {
        this.yRes = yRes;
    }

    /**
     * @return the timeSeries
     */
    public DataTrace_ver_3 getTimeSeries() {
        return timeSeries;
    }

    /**
     * @param timeSeries the timeSeries to set
     */
    public void setTimeSeries(DataTrace_ver_3 timeSeries) {
        this.timeSeries = timeSeries;
    }





    /**
     * @return the pixelArray
     */
    public double[][] getPixelArray() {
        return pixelArray;
    }

    /**
     * @param pixelArray the pixelArray to set
     */
    public void setPixelArray(double[][] pixelArray) {
        this.pixelArray = pixelArray;
    }
    
    private int xRes = 0;
    private int yRes = 0;
    private double [][] pixelArray;
    private DataTrace_ver_3 timeSeries;
    
    public void JHeatMapArray(int xRes, int yRes){
        this.setxRes(xRes);
        this.setyRes(yRes);
        setTimeSeries(new DataTrace_ver_3());
    }
    public void convertTimeSeriestoArray(int xRes,int yRes){
        
        pixelArray = new double[xRes][yRes];
        
        
        
    }
    public void convertTimeSeriestoArray(){
        this.timeSeries.resetStat();
        this.convertTimeSeriestoArray( Math.round((float)(timeSeries.getXMax()-timeSeries.getXMin()))
                                        ,Math.round((float)(timeSeries.getYMax()-timeSeries.getYMin())));
    }
}
