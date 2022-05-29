package NDL_JavaClassLib;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author balam
 */
public class JVectorSpace {
    
    private ArrayList<OrdXYData> space;
    private ArrayList<JVector> vectors;
    
    private int xRes, yRes,xMax,yMax;
    private boolean resMismatch;
    
   // private double [] xcompPixelArray, ycompPixelArray;
    public double[] getCompArray(int Idx){
    double[] pixels;
    if(space.isEmpty() || vectors.isEmpty()){
        javax.swing.JOptionPane.showMessageDialog(null, "There are no vectors to retrive");
        return null;
    }
    if(space.size() != vectors.size()){
        javax.swing.JOptionPane.showMessageDialog(null, "Vector count and pixel mismatch");
        return null;
    }
    if(xRes == 0 || yRes == 0){
        javax.swing.JOptionPane.showMessageDialog
                    (null, "Please assign resolution of the image/array first\n");
        return null;
    }           
    pixels = new double[xRes * yRes];
    
    int pixelCount = 0, arrayIdx;
        
    for ( OrdXYData curPixel : space){
        arrayIdx = (int) Math.round((double)curPixel.getX() + (double)curPixel.getY() * xRes);
        if(arrayIdx < xRes * yRes){
            JVector vect = vectors.get(pixelCount);
            pixels[arrayIdx] = (double)vect.getComponent(Idx);
        }else{
            javax.swing.JOptionPane.showMessageDialog
                    (null, "Array index " +arrayIdx +" does not match the resolution of image :" + xRes + yRes +"\n");
        }
        pixelCount++;
    }
    return pixels;
    }
 JVectorSpace (int xRes, int yRes){
     this.xRes = xRes;
     this.yRes = yRes;
     this.xMax = this.yMax = 0;
     resMismatch = false;
     space = null;
     vectors = null;
 } 
 JVectorSpace(int xRes, int yRes, boolean resAuto, OrdXYData[] space,JVector[] vectors ){
     this.xRes = xRes;
     this.yRes = yRes;
     this.xMax = this.yMax = 0;
     resMismatch = false;
     
     if(space.length != vectors.length || space.length == 0){
         space = null;
         vectors = null;
     }else{
         int Idx = 0;
         for(OrdXYData coord : space){
             addVector(coord,vectors[Idx],resAuto);
             Idx++;
         }
     }
 }
 public void addVector(OrdXYData coordinates, JVector vector, boolean resAuto){
     
     int currX = Math.round((float)coordinates.getX()); 
     int currY = Math.round((float)coordinates.getY());
     
     xMax = currX > xMax ? currX : xMax;
     yMax = currY > yMax ? currY : yMax;
     resMismatch = xRes > xMax || yRes > yMax;
     
     if (resAuto && resMismatch){
        xRes =   currX >= xRes ? currX : xRes;    
        yRes =   currY >= yRes ? currY : yRes;  
        resMismatch = false;
     }
     
     space.add(coordinates);
     vectors.add(vector);
 }
    
}
