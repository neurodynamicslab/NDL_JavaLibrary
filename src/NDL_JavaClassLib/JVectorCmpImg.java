/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NDL_JavaClassLib;
/**
 *
 * @author balam
 */


import ij.ImagePlus;
import ij.ImageStack;
import ij.io.FileSaver;
import ij.process.*;
import java.io.File;

public class JVectorCmpImg {
    
    private ImageProcessor[]  heatMapImg;
    //private JVectorSpace vectors;
    
    int xRes, yRes;
    
    public JVectorCmpImg(int xRes, int yRes, int nComps ){
        
        heatMapImg = new FloatProcessor[nComps];
        this.xRes = xRes;
        this.yRes = yRes;
        
       // vectors = null;
        
    }
    public JVectorCmpImg(JVectorSpace vectors){
        setVectors(vectors);
    }

    private void setVectors(JVectorSpace vectors1) {
        //this.vectors = vectors1;
        int cmp = vectors1.getnComp();
        this.xRes = vectors1.getxMax();
        this.yRes = vectors1.getyMax();  
        for (int compCount = 0; compCount < cmp; compCount++) {
            heatMapImg[compCount] = new FloatProcessor(xRes, yRes, vectors1.getCompArray(compCount));
        }
    }
    public void saveImages(String folderPath,String prefix){
       
       ImagePlus [] images = getImages();
       FileSaver fs;
       int nCmps = images.length;
       int nDigits = (int) Math.log10(nCmps);
       int curNumber = 0;
       for(ImagePlus imp : images){
          fs = new FileSaver(imp); 
          fs.saveAsTiff(folderPath+File.separator+prefix+" Comp");
       }  
    }
    public void saveStack(String filename){
        FileSaver fs;
        fs = new FileSaver(new ImagePlus("",getImageStack()));
        fs.saveAsRawStack(filename);
    }
    public ImageStack getImageStack(){
        ImageStack stk;
        stk = new ImageStack(xRes,yRes,heatMapImg.length);
        for(ImageProcessor ip : heatMapImg)
            stk.addSlice(ip);
        return stk;
    }
    public void addVectors(JVectorSpace vectors){
        setVectors(vectors);
    }
    public ImagePlus [] getImages(){
        ImagePlus [] Images = new ImagePlus[heatMapImg.length];
        int count =0;
        for(ImageProcessor ip : heatMapImg){
            Images[count] = new ImagePlus("Cmp"+count,ip);
        }
        return Images;
    }
    
}
