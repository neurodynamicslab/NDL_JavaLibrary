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

    /**
     * @param nComp the nComp to set
     */
    private void setnComp(int nComp) {
        this.nComp = nComp;
    }

    /**
     * @return the nComp
     */
    public int getnComp() {
        return nComp;
    }

    /**
     * @return the space
     */
    public ArrayList<OrdXYData> getSpace() {
        return space;
    }

    /**
     * @return the vectors
     */
    public ArrayList<JVector> getVectors() {
        return vectors;
    }

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
     * @return the xMax
     */
    public int getxMax() {
        return xMax;
    }

    /**
     * @param xMax the xMax to set
     */
    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    /**
     * @return the yMax
     */
    public int getyMax() {
        return yMax;
    }

    /**
     * @param yMax the yMax to set
     */
    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

    /**
     * @return the resMismatch
     */
    public boolean isResMismatch() {
        return resMismatch;
    }

    /**
     * @param resMismatch the resMismatch to set
     */
    public void setResMismatch(boolean resMismatch) {
        this.resMismatch = resMismatch;
    }
    
    private ArrayList<OrdXYData> space;
    private ArrayList<JVector> vectors;
    private int xRes, yRes,xMax,yMax;
    private boolean resMismatch;
    private int nComp; //Helps to keep track of dimensionality of the vectors space (i.e all vectors have same number of components 
   // private double [] xcompPixelArray, ycompPixelArray;
    
 public double[] getCompArray(int Idx){
    double[] pixels;
    if( getSpace().isEmpty() || getVectors().isEmpty()){
        javax.swing.JOptionPane.showMessageDialog(null, "There are no vectors to retrive");
        return null;
    }
    if( getSpace().size() != getVectors().size()){
        javax.swing.JOptionPane.showMessageDialog(null, "Vector count and pixel mismatch");
        return null;
    }
    if( getxRes() == 0 || getyRes() == 0){
        javax.swing.JOptionPane.showMessageDialog
                    (null, "Please assign resolution of the image/array first\n");
        return null;
    }           
    pixels = new double[getxRes() * getyRes()];
    
//    int pixelCount = 0, arrayIdx;
//        //System.out.print("xRes = "+getxRes()+"\n");
//    for ( OrdXYData curPixel : getSpace()){
//        arrayIdx = (int) (Math.round((double)curPixel.getY()) + Math.round((double)curPixel.getX() * getyRes()));
//        //System.out.print(""+curPixel.getX()+"\t"+curPixel.getY()+"\t"+arrayIdx+"\n");
//        if(arrayIdx < (getxRes() * getyRes())){
//            JVector vect = getVectors().get(pixelCount);
//            pixels[arrayIdx] = (double)vect.getComponent(Idx);
//        }else{
//            javax.swing.JOptionPane.showMessageDialog
//                    (null, "Array index " +arrayIdx +" does not match the resolution of image :" + getxRes() + getyRes() +"\n");
//        }
//        pixelCount++;
//    }

    double[][] tempArray = new double[getxRes()][getyRes()];
    int currX, currY;
    int dataIdx = 0;
    for(OrdXYData curPixel : getSpace()){
        currX = (int)Math.round((double)curPixel.getX());
        currY = (int)Math.round((double)curPixel.getY());
        tempArray[currX][currY] = (double)getVectors().get(dataIdx).getComponent(Idx);
        dataIdx++;
    }
    int xIdx = 0, yIdx = 0;
    for(double[] Y : tempArray){
        for(double Val : Y){
            pixels[xIdx + yIdx*getxRes()] += Val;
            xIdx++;
        }
        yIdx ++;
        xIdx = 0;
    }
    return pixels;
    }
 public JVectorSpace (int xRes, int yRes){
     this.xRes = xRes;
     this.yRes = yRes;
     this.xMax = this.yMax = 0;
     resMismatch = false;
     space = new ArrayList<>();
     vectors = new ArrayList<>();
 } 
 public JVectorSpace(int xRes, int yRes, boolean resAuto, OrdXYData[] spaceArray,JVector[] vectorArray ){
     this.xRes = xRes;
     this.yRes = yRes;
     this.xMax = this.yMax = 0;
     resMismatch = false;
     space = new ArrayList<>();
     vectors = new ArrayList<>();
     
     if(spaceArray.length != vectorArray.length || spaceArray.length == 0){
         space = null;
         vectors = null;
     }else{
         space = new ArrayList<> ();
         vectors = new ArrayList<> ();
         int Idx = 0;
         for(OrdXYData coord : spaceArray){
             addVector(coord,vectorArray[Idx],resAuto);
             Idx++;
         }
     }
 }
 private void addVector(OrdXYData coordinates, JVector vector, boolean resAuto){
     
     int currX = (int) Math.round((double)coordinates.getX()); 
     int currY = (int) Math.round((double)coordinates.getY());
     int currComp = vector.getNComponents();
        if(this.vectors.isEmpty())
            this.setnComp(vector.getNComponents());
        if(currComp != getnComp()){
            javax.swing.JOptionPane.showMessageDialog(null, "Found vector component mismatch");
            return;
        }
                
        setxMax(currX > getxMax() ? currX : getxMax());
        setyMax(currY > getyMax() ? currY : getyMax());
        setResMismatch(getxRes() > getxMax() || getyRes() > getyMax());
     
     if (resAuto && isResMismatch()){
            setxRes(currX >= getxRes() ? currX : getxRes());    
            setyRes(currY >= getyRes() ? currY : getyRes());  
            setResMismatch(false);
     }
     
        getSpace().add(coordinates);
        getVectors().add(vector);
 }
 public void fillSpace(ArrayList<OrdXYData> coordLst,ArrayList<JVector>vectorLst,boolean resAuto){
     
     if(coordLst.isEmpty() || (vectorLst.size() != coordLst.size())){
         space = null;
         vectors = null;
     }else{
         int Idx = 0;
         for(OrdXYData coord : coordLst){
             addVector(coord,vectorLst.get(Idx),resAuto);
             Idx++;
         }
     }
     
 }
 public void fillSpace(OrdXYData[] spaceArray,JVector[] vectorArray,boolean resAuto){
     
     if(spaceArray.length != vectorArray.length || spaceArray.length == 0){
         space = null;
         vectors = null;
     }else{
         int Idx = 0;
         for(OrdXYData coord : spaceArray){
             addVector(coord,vectorArray[Idx],resAuto);
             Idx++;
         }
     }
 
 }
 
 public JVectorSpace(int xRes, int yRes,boolean resAuto, ArrayList<OrdXYData> coordLst,ArrayList<JVector> vectorLst ){
   
     this.xRes = xRes;
     this.yRes = yRes;
     this.xMax = this.yMax = 0;
     resMismatch = false;
     space = new ArrayList<>();
     vectors = new ArrayList<>();
     
     if(coordLst.isEmpty() || (vectorLst.size() != coordLst.size())){
         space = null;
         vectors = null;
     }else{
         int Idx = 0;
         for(OrdXYData coord : coordLst){
             addVector(coord,vectorLst.get(Idx),resAuto);
             Idx++;
         }
     }
 }
 public JVectorSpace scaleVectors(Number[][] scalingMat){
     
     
     if(scalingMat[0].length != yRes && scalingMat.length != xRes){
         System.out.println("The dimension of the scaling Matrix in (x X y)/(width X height) "+ scalingMat.length + " X " 
                                            + scalingMat[0].length + "is not same as vector space" + this.xRes + " X " + this.yRes);
         return null;
     }
     //if(this.isResMismatch()) Do a reset of res or inform the user to set it
     
     JVectorSpace scaledSpace = new JVectorSpace(this.xRes,this.yRes);
     int xIdx, yIdx;
     double scale = 1;
     int Idx = 0;
     
     for(var XYCord : space){
         xIdx = (int) Math.round((Double)XYCord.getX());
         yIdx = (int) Math.round((Double)XYCord.getY());
         try{
         scale = (Double)scalingMat[xIdx][yIdx]; 
         }
         catch(Exception e){
             System.out.println(e.getMessage()+ " Resolution mismatch !");
         }
         scaledSpace.addVector(XYCord,vectors.get(Idx++).getScaledVector(scale), resMismatch);
     }
     return scaledSpace;
 }
 public JVectorSpace scaleVectors(double scale){
     
     JVectorSpace scaledSpace = new JVectorSpace(this.xRes,this.yRes);
     
     int Idx = 0;
     
     for(var XYCord : space)         
         scaledSpace.addVector(XYCord,vectors.get(Idx++).getScaledVector(scale), resMismatch);
     
     return scaledSpace;
 }
}
