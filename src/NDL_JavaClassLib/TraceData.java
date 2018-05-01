/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Balaji
 */
package NDL_JavaClassLib;

import java.awt.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;
/*import ij.*;
import ij.process.*;
import ij.gui.*;
import ij.measure.CurveFitter;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import ij.plugin.frame.*;*/
import java.awt.datatransfer.*;
import javax.swing.*;


public class TraceData extends Object{
    double[] xData = null;
    double[] yData = null;
    double x_Max = Double.MIN_VALUE;
    double y_Max =Double.MIN_VALUE;
    double x_Min = Double.MAX_VALUE;
    double y_Min = Double.MAX_VALUE;
    double x_Sum = 0;
    double y_Sum = 0;
    int CurrPos = 0;
    int DataLength = 0;
    int ActLength =0;                       //Needs comment : to say what is the difference between DataLength and ActLength
                                            // Actlength - the number of datapoints that are non zero ?
                                            // Datalength - the capacity of the Data ie) the maximum number of data pts that can be held in the object
    //boolean Y_Only = false;
   public TraceData( int length){
        if (length > 0){
            DataLength = length;
            xData = new double[DataLength];
            yData = new double[DataLength];
        }
    }
   public TraceData( double[] x, double[] y){
        if( x != null && y != null){
            xData = (double[])x.clone();
            yData = (double[])y.clone();
            DataLength = Math.min(xData.length,yData.length);
        }
    }
   public boolean addData(double x, double y){
        if (CurrPos >= DataLength){
            //showMessage("OOPS! I am full you can not add anymore to me");
            return false;
        }

        xData[CurrPos] = x;
        yData[CurrPos] = y;
        CurrPos++;
        ActLength =  CurrPos > ActLength ? CurrPos : ActLength;

        /* Update the stat parameters: This is the only entry point of the data */

        setStat( CurrPos-1);

        return true;
    }
   public double getX(int pos){
       if(pos < DataLength)
           return xData[pos];
       return xData[DataLength];
   }
   public double getY(int pos){
      if(pos < DataLength) return yData[pos];
      return yData[DataLength];
   }
   public double[] getXY(int pos){
       double[] XY = new double[2];
       if (pos < DataLength){
            XY[1] = xData[pos];
            XY[2] = yData[pos];
       }
       else{
            XY[1] = xData[DataLength];
            XY[2] = yData[DataLength];
       }
       return XY;
   }
   public boolean setPosition(int pos){
       if(pos < DataLength){
           CurrPos = pos;
           return true;
       }
     return false;
   }
   public int getPosition(){
       return CurrPos;
   }
   public int getDataLength(){
       return DataLength;
   }
   public double[] getX(){
       return (double [])xData.clone();
   }
   public double[] getY(){
       return (double [])yData.clone();
   }
   public double[] getX(boolean trimmed){
       return Arrays.copyOf(xData, ActLength);
   }
   public double[] getY(boolean trimmed){
       return Arrays.copyOf(yData, ActLength);
   }
   public boolean setLength(int length){
       if (DataLength != 0)
           return false;            // The object is holding a data array. In order to reset one needs to use the override method (just an extra protection)
        if (length > 0){
            DataLength = length;
            xData = new double[DataLength];
            yData = new double[DataLength];
            CurrPos = ActLength = 0;
            x_Min = y_Min = Double.MAX_VALUE;
            x_Max= y_Max = x_Sum = y_Sum = 0;
            return true;
        }
       return false;
   }
   public void OverrideLength(int length){
       if (length == 0){
           xData = null;
           yData = null;
           x_Min = y_Min = Double.MAX_VALUE;
            x_Max= y_Max = x_Sum = y_Sum = 0;
           return;
       }
       DataLength = length;
       xData = new double[DataLength];
       yData = new double[DataLength];
       CurrPos = ActLength = 0;
       x_Min = y_Min = Double.MAX_VALUE;
       x_Max= y_Max = x_Sum = y_Sum = 0;
       return;
   }
  void  setStat(double x, double y){
       x_Max = (x_Max > x ) ? x_Max : x ;
        y_Max = (y_Max > y ) ? y_Max : y ;

        x_Min = (x_Min < x ) ? x_Min  : x ;
        y_Min = (y_Min < y ) ? y_Min : y ;

        x_Sum += x;
        y_Sum += y;
   }
  void setStat(int pos){

        double x = getX(pos);
        double y = getY(pos);

        x_Max = (x_Max > x ) ? x_Max : x ;
        y_Max = (y_Max > y ) ? y_Max : y ;

        x_Min = (x_Min < x ) ? x_Min  : x ;
        y_Min = (y_Min < y ) ? y_Min : y ;

        x_Sum += x;
        y_Sum += y;
  }

  void setStat(boolean all){
      if(all){
          double x =0;
          double y = 0;
          for(int i = 0 ; i < ActLength ; i++){
            x = getX(i);
            y = getY(i) ;
            
            x_Max = (x_Max > x ) ? x_Max : x ;
            y_Max = (y_Max > y ) ? y_Max : y ;

            x_Min = (x_Min < x ) ? x_Min  : x ;
            y_Min = (y_Min < y ) ? y_Min : y ;

            x_Sum += x;
            y_Sum += y;
          }
      }else{
          setStat(this.getPosition());
      }
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

}
