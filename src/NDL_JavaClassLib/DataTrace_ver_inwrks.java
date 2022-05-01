/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**Edited on 27th Apr2022, BJ
 * A class for storing X, Y data along with error bars. The class is defined using java generics so that it can accept any numeric such as byte,int,long float or double 
 * as input data. This class is written as an extension to OrdXYData which stores just the data without error bars.
 * on 01st May, 2022 BJ: Added method to return the array of X values or y values
 * @author Balaji
 */

package NDL_JavaClassLib;

import static java.lang.Math.exp;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import java.util.*;
/*import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.measure.CurveFitter;
import static java.lang.Math.log10;
import static java.lang.Math.pow;
import java.util.*;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.frame.*;*/

/*class OrdXYData<X extends Number, Y extends Number> extends Object{
    
    int serialNo;
    X xDataPt;
    Y yDataPt;
    public OrdXYData(int serial, X x, Y y){
        xDataPt = x;
        yDataPt = y;
        serialNo = serial; 
    }
    public  X getX(){
        return xDataPt;
    }
    public Y getY(){
        return yDataPt;
    }
    public ArrayList<? extends Number> getXY(){
        ArrayList dataArray = new ArrayList(2);
        dataArray.add(xDataPt);
        dataArray.add(yDataPt);
        return dataArray;
    }
    public int getSerial(){
        return serialNo;
    }
}*/
class OrdXYErrData< XErr extends Number, YErr extends Number, X extends Number, Y extends Number> extends OrdXYData<X,Y>{

    /** Checks for the flag setting to determine if the error bar is standard deviation
     * Note: This flag needs to be set by the user during the data input stage.
     * @return the isSD
     */
    public boolean isIsSD() {
        return isSD;
        }

    /**Set the flag to determine if the error bar in the present in this data is standard deviation(true) or standard error(false).
     * @param isSD the isSD to set
     */
    public void setIsSD(boolean isSD) {
        this.isSD = isSD;
    }

    /**Returns the lowest value that can be stored in X data
     * 
     * @return the LXLimit
     */
    public double getLXLimit() {
        return LXLimit;
    }

    /**Sets the lowest value that can be stored in X data. Default is Double.MINVALUE
     * @param LXLimit the LXLimit to set
     */
    public void setLXLimit(double LXLimit) {
        this.LXLimit = LXLimit;
    }

    /**
     * Returns the lowest value that can be stored in Y data
     * @return the LYLimit
     */
    public double getLYLimit() {
        return LYLimit;
    }

    /**Sets the lowest value that can be stored in Y data. Default is Double.MINVALUE
     * @param LYLimit the LYLimit to set
     */
    public void setLYLimit(double LYLimit) {
        this.LYLimit = LYLimit;
    }

    /**
     * Returns the highest value that can be stored in X data. 
     * @return the HXLimit
     */
    public double getHXLimit() {
        return HXLimit;
    }

    /**
     * Sets the highest value that can be stored in X data. Default is Double.MAXVALUE
     * @param HXLimit the HXLimit to set
     */
    public void setHXLimit(double HXLimit) {
        this.HXLimit = HXLimit;
    }

    /**
     * Returns the highest value that can be stored in X data. 
     * @return the HYLimit
     */
    public double getHYLimit() {
        return HYLimit;
    }

    /**
     * Sets the highest value that can be stored in X data. Default is Double.MAXVALUE
     * @param HYLimit the HYLimit to set
     */
    public void setHYLimit(double HYLimit) {
        this.HYLimit = HYLimit;
    }
    //int serialNo;
    XErr xErrorBar;
    YErr yErrorBar;
    private boolean isSD; //SD if true and SEM if false
    int nPts;
    private double LXLimit = Double.MIN_VALUE;
    private double LYLimit = Double.MIN_VALUE;
    private double HXLimit = Double.MAX_VALUE;
    private double HYLimit = Double.MAX_VALUE;
    
    /**
     * 
     * @param serial : Serial number of the data point. Can be used to restore the order of data points to the input after sorting.
     *                 for instance one might sort on y values then and perform the desired operation on the data set and then sort on
     *                  serial number to restore to the input order. 
     * @param x      : x value it can be any of data type sub classed from Number
     * @param y      : y value  it can be any of data type sub classed from Number
     * @param xError    : value of error in x axis typically it is either standard deviation or standard error. Data type is one of the sub
     *                    classes of Number
     * @param yError  : value of error in x axis typically it is either standard deviation or standard error. Data type is one of the sub
     *                    classes of Number
     * @param SD      : Boolean for if the error bar is SD (True). False would mean the error bar is SEM
     * @param NoofPoints : Number of data points used to arrive at the SD or SE for this particular point. 
     *                     It is assumed that number of points averaged to arrive at error bar is same for X and Y.
     */
    public OrdXYErrData(int serial, X x, Y y, XErr xError, YErr yError, boolean SD, int NoofPoints){
        //super.xDataPt = (Number) x;
        //super.yDataPt = (Number) y;
        super(serial,x,y);
        /**
         * @ xErrorBar yErrorBar
         * The value of the error bar for x axis and y axis
         */
        xErrorBar = xError;                         
        yErrorBar = yError;
        /**
         * @isSD 
         * True if the error bar is standard deviation false if it is standard error of the mean.
         */
        isSD = SD;          
        /** 
         * @nPts 
         * Number of data points used to arrive at the SD or SE for this particular point. 
         * It is assumed that number of points averaged to arrive at error bar is same for X and Y.
        */
        nPts = NoofPoints;                  
    }
    public OrdXYErrData(int serial, X x, Y y){
       /* xDataPt = x;
        yDataPt = y;
        serialNo = serial; */
        super(serial,x,y);
        xErrorBar = (XErr) Integer.valueOf(-1);
        yErrorBar = (YErr) Integer.valueOf(-1);
        isSD = true;
        nPts = -1;
    }
    @Override
    public  X getX(){
        return (X) super.xDataPt;
    }
    @Override
    public Y getY(){
        return (Y) yDataPt;
    }
    @Override
    public ArrayList<? extends Number> getXY(){
        ArrayList dataArray = new ArrayList(2);
        dataArray.add(xDataPt);
        dataArray.add(yDataPt);
        return dataArray;
    }
    @Override
    public int getSerial(){
        return serialNo;
    }
    
    public ArrayList<? extends Number> getXYErr(){
        ArrayList dataArray = new ArrayList(4);
        dataArray.add(xDataPt);
        dataArray.add(yDataPt);
        dataArray.add(xErrorBar);
        dataArray.add(yErrorBar);
        return dataArray;
    }
    /**
     * 
     * @return  X errorBar value co-responding to this data point
     */
    public  XErr getXError(){
        return this.xErrorBar;
    }
    /**
     * 
     * @return Y errorBar value co-responding to this data point
     */
    public YErr getYError(){
        return this.yErrorBar;
    }
    /**
     * 
     * @return true if the error bar is SD false if SEM (This is set by the user at the time of initialization.
     */
    public boolean getifSD(){
        return isIsSD();
    }
    /**
     * 
     * @param SD call it with True to interpret the error bar at SD False for interpreting it as SEM. 
     *        This is assumed to be same for Y and X error bar.
     * Note : It is not calculated by the class or any members of this class (You can not do that as all you have is 'a' value for 
     * X, 'a' value for Y.
     * @return 
     */
    public boolean setifSD(boolean SD){
        setIsSD(SD);
        return isIsSD();
    }
    /**
     * Use this function to get the number points that is used to average and get the X and Y mean and error bar associated with it. 
     * Use the return value to calculate SEM from SD or vice versa. 
     * @return 
     */
    
    public int getNumberofPoints(){
        return nPts;
    }
    /**
     * Use this function to set the number points that is used to average and get the X and Y mean and error bar associated with it. 
     *@param npPoints : number of points over which the error bar is calculated. Provided to go from SD to SEM and vice versa. 
     */
    
    public void setNumberofPoints(int noPoints){
        nPts = noPoints;
    }
}
/**
 * This class built around arrayList to mimic a container class with x - axis and y - axis values along with 
 * Error bars. Elements of the list are objects of the class OrdXYErrData. The class can be used to perform basic operations
 * of binning, derivative and adjacent averaging (yet to be implemented as 01st May, 2022). 
 * @author balam
 */
public class DataTrace_ver_inwrks extends ArrayList<OrdXYErrData>{

    /**
     * @return the xRoundoff
     */
    public boolean isxRoundoff() {
        return xRoundoff;
    }

    /**
     * @param xRoundoff the xRoundoff to set
     */
    public void setxRoundoff(boolean xRoundoff) {
        this.xRoundoff = xRoundoff;
    }

    /**
     * @return the yRoundoff
     */
    public boolean isyRoundoff() {
        return yRoundoff;
    }

    /**
     * @param yRoundoff the yRoundoff to set
     */
    public void setyRoundoff(boolean yRoundoff) {
        this.yRoundoff = yRoundoff;
    }

    /**
     * @return the LXLimit
     */
    public double getLXLimit() {
        return LXLimit;
    }

    /**
     * @param LXLimit the LXLimit to set
     */
    public void setLXLimit(double LXLimit) {
        this.LXLimit = LXLimit;
    }

    /**
     * @return the LYLimit
     */
    public double getLYLimit() {
        return LYLimit;
    }

    /**
     * @param LYLimit the LYLimit to set
     */
    public void setLYLimit(double LYLimit) {
        this.LYLimit = LYLimit;
    }

    /**
     * @return the HXLimit
     */
    public double getHXLimit() {
        return HXLimit;
    }

    /**
     * @param HXLimit the HXLimit to set
     */
    public void setHXLimit(double HXLimit) {
        this.HXLimit = HXLimit;
    }

    /**
     * @return the HYLimit
     */
    public double getHYLimit() {
        return HYLimit;
    }

    /**
     * @param HYLimit the HYLimit to set
     */
    public void setHYLimit(double HYLimit) {
        this.HYLimit = HYLimit;
    }
    
      
    //ArrayList<OrdXYData> rawData;
    
    
    double x_Max = Double.MIN_VALUE;
    double y_Max = Double.MIN_VALUE;
    double x_Min = Double.MAX_VALUE;
    double y_Min = Double.MAX_VALUE;
    double x_Sum = 0;
    double y_Sum = 0;
    int CurrPos = 0;
    int DataLength = 0;
    int ActLength = 0;                       //Needs comment : to say what is the difference between DataLength and ActLength
                                             // Actlength - the number of datapoints that are non zero ?
                                             // Datalength - the capacity of the Data ie) the maximum number of data pts that can be held in the object
    //boolean Y_Only = false;
    private boolean xRoundoff = true;
    private boolean yRoundoff = true;
    private double LXLimit = Double.MIN_VALUE;
    private double LYLimit = Double.MIN_VALUE;
    private double HXLimit = Double.MAX_VALUE;
    private double HYLimit = Double.MAX_VALUE;
    
    
    /** Binning Data
     * 
     */
    boolean binInY = true;
    double binWnd  = 0;
    
    int SCALE = 0;       //SCALE = 0 - linear, 1 - ln, 2 - log, 3 - power of 2

    Iterator dataIterator;
    
    
    
   // ArrayList<Double[]> BinnedData;
    
   //public DataTrace( int length){
       // rawData = new ArrayList<>(length);
        /*if (length > 0){
            DataLength = length;
            xData = new double[DataLength];
            yData = new double[DataLength];
        }
        XData = new ArrayList<>();
        YData = new ArrayList<>();*/
    //}
    public void setScaleType(int stype){
        SCALE = stype;
    }
    public int getScaleType(){
        return SCALE;    //SCALE = 0 - linear, 1 - ln, 2 - log, 3 - power of 2
    }
    public DataTrace_ver_inwrks(){
        //rawData = new ArrayList<OrdXYData>();
        //dataIterator = rawData.iterator();
        
    }
   public <B extends Number>DataTrace_ver_inwrks(int datalength, B binWidth, boolean binInX){
       this.binWnd = binWidth.doubleValue();
       //rawData = new ArrayList(datalength);
       //dataIterator = rawData.iterator();
   }
   public<X extends Number,Y extends Number> void addData( X[] xData, Y[] yData){
        //this.dataIterator = rawData.iterator();
        if( xData != null && yData != null && xData.length == yData.length){          
            int idx = 0;          
            for(X x : xData){
               addData(x,yData[idx++]); // It is  more efficient to add it directly to rawdata instead of calling addData.
                                             // But this ensures modularity. If any change to way we add elements we need to 
                                             // modify one function and in one place i.e addData()              
            }
        }
    }
   
   public <X extends Number,Y extends Number, Xr extends Number, Yr extends Number> void addData(X xData,Y yData, Xr xError, Yr yError, boolean SD, int nPts){  
       DataLength++;
       OrdXYErrData dataPt = new OrdXYErrData(DataLength,xData,yData,xError,yError,SD,nPts);
                this.add(dataPt);
        
    }
   public <X extends Number, Y extends Number> void addData(X xData,Y yData){
       this.addData(xData, yData, -1, -1, true,-1);
   }
   public <X extends Number,Y extends Number, Xr extends Number, Yr extends Number> void addData(X xData,Y yData, Xr xError, Yr yError, boolean SD){
       
       this.addData(xData, yData, xError, yError, SD,0);
   }
   
 /*public ArrayList getNextXYData(){
      
     
     /*if (dataIterator.hasNext())
        return ((OrdXYData) dataIterator.next()).getXY();
     else 
         return null;*/
 //}
 
   public int getDataLength(){
       return this.size();
   }
   public<N extends Number> ArrayList getX(){
       ArrayList <N> x = new ArrayList();    
       this.forEach((Data) -> {
           x.add((N) Data.getX());
        });
       return x;
   }
   public <N extends Number> ArrayList getY(){
      ArrayList <N> y = new ArrayList();
       this.forEach((Data) -> {
           y.add((N) Data.getY());
        });
       return y;
   }
   public <N extends Number> ArrayList getXErrs(){
      ArrayList <N> Xerr = new ArrayList();
       this.forEach((Data) -> {
           Xerr.add((N) Data.getXError());
        });
       return Xerr;
   }
    public <N extends Number> ArrayList getYErrs(){
      ArrayList <N> Yerr = new ArrayList();
       this.forEach((Data) -> {
           Yerr.add((N) Data.getYError());
        });
       return Yerr;
   }
    public  ArrayList getNpts(){
      ArrayList <Integer> Pts = new ArrayList();
       this.forEach((Data) -> {
           Pts.add((Integer) Data.getNumberofPoints());
        });
       return Pts;
   }
  
 
   public <X extends Number, Y extends Number> void resetStat(){

        this.forEach((Data)->{
            double x = ((X)Data.getX()).doubleValue();
            double y = ((Y)Data.getY()).doubleValue();
            
            x_Max = (x_Max > x ) ? x_Max : x ;
            y_Max = (y_Max > y ) ? y_Max : y ;

            x_Min = (x_Min < x ) ? x_Min  : x ;
            y_Min = (y_Min < y ) ? y_Min : y ;

            x_Sum += x;
            y_Sum += y;
        });
  };
private <X extends Number, Y extends Number> void setStat(X xData,Y yData){
    
            double x = xData.doubleValue();
            double y = yData.doubleValue();
            
            x_Max = (x_Max > x ) ? x_Max : x ;
            y_Max = (y_Max > y ) ? y_Max : y ;

            x_Min = (x_Min < x ) ? x_Min  : x ;
            y_Min = (y_Min < y ) ? y_Min : y ;

            x_Sum += x;
            y_Sum += y;
  }

  
  public double getXMax(){
      return x_Max;
  }
  public double getYMax(){
      return y_Max;
  }
  public double getXMin(){
      return x_Min;
  }
  public double getYMin(){
      return y_Min;
  }
  public double getXSum(){
      return x_Sum;
  }
  public double getYSum(){
      return y_Sum;
  }
  public double getYPk(){
      return y_Max - y_Min ;
  }
  public double getXPk(){
      return x_Max - x_Min;
  }
  public void resetTrace(){
    this.clear();
  }
  /***
   * Differentiates the trace data and generates the differential of current data
   * will overwrite the current data with the differentiated data
   */
  public void differentiate(){
      differentiate(true);
  }
  /**
   * Differentiate the trace data and return the float array. 
   * if Overwrite is true then the current data will be replaced by the differentiated data
   * @param Overwrite
   * @return 
   */
  public DataTrace_ver_inwrks differentiate(boolean Overwrite){
      DataTrace_ver_inwrks difData = new DataTrace_ver_inwrks();
      //difData = null;
     double prevY = 0.0, currY ;
     double prevX = 0.0, currX ;
     for(OrdXYErrData data : this){
         currY = (double) data.getY();
         currX = (double) data.getX();
         double diffY = currY - prevY;
         double diffX = currX - prevX;
         difData.addData(diffX, diffY);               //At presnt the error bars are not propagated
         prevY = currY;
         prevX = currX;
     }
     if(Overwrite){
         this.clear();
         difData.forEach((data) -> {
             this.addData(data.getX(), data.getY());
          });
         
     }
      return difData;
  }
  
  
  
  public <X extends Number, Y extends Number, Xerr extends Number, YErr extends Number> DataTrace_ver_inwrks binData(double binWidth, boolean binInX, boolean restoreSeq){
      //SCALE = 0 - linear, 1 - ln, 2 - log, 3 - power of 2
     DataTrace_ver_inwrks binnedData = new DataTrace_ver_inwrks();
     this.sortData(binInX);
     int binNumber = 1;
     double sbinwidth = binWidth;
     double halfbinWidth = binWidth/2;
     double binStart = (double)((binInX) ? this.get(0).getX() : this.get(0).getY());
     
    /* switch(this.SCALE){
                 case 0:
                     break;
                 case 1:
                     binWidth = exp(sbinwidth*binNumber)*(exp(sbinwidth)-1); 
                     halfbinWidth = binWidth/2;
                     break;
                 case 2: 
                     binWidth = pow(10,sbinwidth*binNumber)*(pow(10,sbinwidth)-1); 
                     halfbinWidth = binWidth/2;
                     break;
                 case 3:
                     binWidth = pow(2,sbinwidth*binNumber)*(pow(2,sbinwidth)-1); 
                     halfbinWidth = binWidth/2;
                     break;
                 case 4:
                     binWidth = (binStart/binWidth);
                     System.out.print( "\t binwidths =" + binWidth);
                     halfbinWidth = binWidth/2;
                     break;
             }
     
     */
     
     
     double binEnd = binStart + binWidth;
     
     double binCtr = binStart + halfbinWidth;
     double sum = (this.get(0).getY()).doubleValue();
     
     double sumSq = sum * sum;
     
     int count = 1;
     
     for(OrdXYErrData data : this){
        
         double curX = ((double)data.getX());
         double curY = ((double)data.getY());
         if(curX <= this.getLXLimit()  || curY <= this.getLYLimit() ){
           if( isxRoundoff() || isyRoundoff()){
             if(curX <= this.getLXLimit() ) curX = 0;
             if(curY <= this.getLYLimit()) curY = 0;
           }   
           else
               continue;   
         }
            
         
         if( curX >= binStart && curX < binEnd){
             sum += curY;
             sumSq += (curY*curY);
             count++;
         }else{
             double yData = sum/count ;
             double sem  = pow((sumSq/count) - (yData*yData), 0.5)/pow(count,0.5);
             binnedData.addData(binCtr,(sum/count),0,sem,false,count);
             sum = curY;
             sumSq = sum * sum ;
             count = 1;
             switch(this.SCALE){
                 case 0:
                     break;
                 case 1:
                     binWidth = exp(sbinwidth*binNumber)*(exp(sbinwidth)-1); 
                     halfbinWidth = binWidth/2;
                     break;
                 case 2: 
                     binWidth = pow(10,sbinwidth*binNumber)*(pow(10,sbinwidth)-1); 
                     halfbinWidth = binWidth/2;
                     break;
                 case 3:
                     binWidth = pow(2,sbinwidth*binNumber)*(pow(2,sbinwidth)-1); 
                     halfbinWidth = binWidth/2;
                     break;
                 
             }
             binStart = curX;
             binCtr = binStart + halfbinWidth;
             binEnd = binStart + binWidth;
         }
     }
      
      return binnedData;
  }
  public<X extends Number, Y extends Number> DataTrace_ver_inwrks sortXYData(DataTrace_ver_inwrks XYData,boolean inX){
        
        X[] x = (X [])(XYData.getX().toArray());
        Y[] y = (Y [])(XYData.getY().toArray());
        
        DataTrace_ver_inwrks sortedData = new DataTrace_ver_inwrks();
               
        sortedData.addData(x, y);
        
        sortedData.sortData(inX);
      return sortedData;
  }
  private void sortData(boolean inX){
      compareXofXYData xCmp = new compareXofXYData();
      compareYofXYData yCmp = new compareYofXYData();
      this.sort((inX) ? xCmp : yCmp);
  }
  private void resetOrder(){
      compareSerialofXYData cmp = new compareSerialofXYData();
      this.sort(cmp);
  }
  class compareXofXYData implements Comparator<OrdXYData>{
      
        @Override
        public int compare(OrdXYData t, OrdXYData t1) {
            return Double.compare(t.getX().doubleValue(), t1.getX().doubleValue());
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
      
  }
  class compareYofXYData implements Comparator<OrdXYData>{
      @Override
      public int compare(OrdXYData t, OrdXYData t1) {
            return Double.compare(t.getY().doubleValue(), t1.getY().doubleValue());
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
      
  }
  class compareSerialofXYData implements Comparator<OrdXYData>{
      @Override
      public int compare(OrdXYData t, OrdXYData t1){
          return Integer.compare(t.getSerial(), t1.getSerial());
      }
  }

}
