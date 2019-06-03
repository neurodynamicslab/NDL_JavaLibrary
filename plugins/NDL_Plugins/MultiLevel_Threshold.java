/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package NDL_Plugins;
import ij.*;
import ij.measure.ResultsTable;
import ij.plugin.ImageCalculator;
import ij.plugin.PlugIn;
import ij.process.*;
//import ij.gui.*;
//import java.awt.*;
import java.io.File;
import javax.swing.JFileChooser;

/**
 * ImageJ Plugin for selecting multiple levels of thresholding in an image. Modified from the code available 
 * at https://imagej.net/plugins/download/Multi_OtsuThreshold.java. The available code does not handle stacks. 
 * Algorithm: PS.Liao, TS.Chen, and PC. Chung,
 * Journal of Information Science and Engineering, vol 17, 713-727 (2001)
 * Coding of Multi_OtsuThreshold.java is by Yasunari Tosa (ytosa@att.net)        Date     : Feb. 19th, 2005
 * @author Balaji (MultiLevel_Threshold.java)
 * The functions     have been taken from the Multi_OtsuThreshold PlugIn
 */
public class MultiLevel_Threshold implements PlugIn{

    ImagePlus imp;
    ImagePlus [] result;
    
    static String filePath = "";
    File outDir;
    File [] imageFiles;
    
    int tLevels;
    static final int NGRAY=256;
    
    float [] hist   = new float[256];
    float [][] p    = new float [256][256];
    float [][] s    = new float [256][256];
    float [][] h    = new float [256][256];
    int [] thres;
    private ImageStack[] resStk;
    @Override
    public void run(String str) {
       //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       
       
       MultiLevelThresholdGUI gui = new MultiLevelThresholdGUI(IJ.getInstance(), true);
       gui.setVisible(true);
       tLevels = gui.getnLevels();
       
       result = new ImagePlus[tLevels];
       thres  = new int[tLevels];
       
       JFileChooser fc = new JFileChooser();
       fc.setCurrentDirectory(new File(filePath));
       fc.setMultiSelectionEnabled(true);
       int result = fc.showOpenDialog(null);
       
       if(result == JFileChooser.APPROVE_OPTION){
           imageFiles  = fc.getSelectedFiles();
           filePath = fc.getCurrentDirectory().getAbsolutePath();
           
       }else{
           return;
       }
       
       for(File dataFile : imageFiles){             //Iterate through the imagefile array
           if(dataFile.exists())
               imp = new ImagePlus(dataFile.getAbsolutePath());
           else
               break;
           
           if(imp != null){             
              
              ImageStack stk =  imp.getStack();
              ImagePlus curSlice;
              ImageProcessor curIp;
              int width = imp.getWidth();
              int height = imp.getHeight();
                            
              int thCount = 1;
              int lowWnd = 0;       //min on gray scale
              ResultsTable rt = new ResultsTable();
              resStk = new ImageStack[tLevels];
              ImagePlus [] masks; 
              
              for(int i = 0 ; i < tLevels ; i++){
                  resStk[i] = imp.createEmptyStack();
                  this.result[i] = imp.duplicate();
                  //this.result[i].setStack(resStk[i]);
                 // this.result[i].show();
              }
              
              
              for(int sliceNo = 1 ; sliceNo <= stk.getSize() ; sliceNo++){
                  
                  //masks = new ImagePlus[tLevels];
                  curIp = stk.getProcessor(sliceNo).duplicate().convertToByteProcessor(true);
                  
                  this.buildHistogram(this.hist,(byte[])curIp.getPixels(),width,height);
                  this.buildLookupTables(p, s, h, hist);
                  this.findMaxSigma(tLevels, h, thres);
                  rt.incrementCounter();
                  rt.addValue("FName",dataFile.getName());
                  rt.addValue("Slice#",sliceNo);
                  
                  for(int th : thres){
                      rt.addValue("Thres#"+ thCount, th);
                      ImageProcessor resIP = curIp.duplicate();
                      resIP.setThreshold(lowWnd, th, ImageProcessor.NO_LUT_UPDATE);
                     // masks[thCount-1] = new ImagePlus(dataFile.getName(),resIP.createMask());                    
                      resStk[thCount-1].addSlice(resIP.createMask());
                      thCount++;
                  }
                  
                  thCount = 1; 
                  
              }
              
           }
         int Count = 0;
         for(ImagePlus im : this.result){
             im.setStack(dataFile.getName()+" Thres#"+Count,resStk[Count]);
             im.show();
             
             String fileName  = dataFile.getName();
             fileName = fileName.substring(0,fileName.lastIndexOf(".")) + "_Thres#"+Count;
             outDir = new File (dataFile.getParent() +File.pathSeparator+ "Res_Of_"+tLevels+"Seg");
             if(outDir.mkdir())
                IJ.saveAsTiff(im,outDir+File.pathSeparator+fileName); 
             else{
                IJ.saveAsTiff(im,dataFile.getParent()+File.pathSeparator+fileName);
             }
             Count++;
         }  
           
       }
       
    }
  public void buildHistogram(float [] h, byte [] pixels, int width, int height){
      
    // note Java byte is signed. in order to make it 0 to 255 you have to
    // do int pix = 0xff & pixels[i];
    for (int i=0; i < width*height; ++i)
      h[(int) (pixels[i]&0xff)]++;
   // note the probability of grey i is h[i]/(width*height)
    float [] bin = new float[NGRAY];
    float hmax = 0.f;
    for (int i=0; i < NGRAY; ++i)
    {
      bin[i] = (float) i;
      h[i] /= ((float) (width*height));
      if (hmax < h[i])
	hmax = h[i];
    }
   // PlotWindow histogram = new PlotWindow("Histogram", "grey", "hist", bin, h);
   // histogram.setLimits(0.f, (float) NGRAY, 0.f, hmax);
   //  histogram.draw();
  }

  public void buildLookupTables(float [][] P, float [][] S, float [][] H, float [] h)
  {
    // initialize
    for (int j=0; j < NGRAY; j++)
      for (int i=0; i < NGRAY; ++i)
      {
	P[i][j] = 0.f;
	S[i][j] = 0.f;
	H[i][j] = 0.f;
      }
    // diagonal 
    for (int i=1; i < NGRAY; ++i)
    {
      P[i][i] = h[i];
      S[i][i] = ((float) i)*h[i];
    }
    // calculate first row (row 0 is all zero)
    for (int i=1; i < NGRAY-1; ++i)
    {
      P[1][i+1] = P[1][i] + h[i+1];
      S[1][i+1] = S[1][i] + ((float) (i+1))*h[i+1];
    }
    // using row 1 to calculate others
    for (int i=2; i < NGRAY; i++)
      for (int j=i+1; j < NGRAY; j++)
      {
	P[i][j] = P[1][j] - P[1][i-1];
	S[i][j] = S[1][j] - S[1][i-1];
      }
    // now calculate H[i][j]
    for (int i=1; i < NGRAY; ++i)
      for (int j=i+1; j < NGRAY; j++)
      {
	if (P[i][j] != 0)
	  H[i][j] = (S[i][j]*S[i][j])/P[i][j];
	else
	  H[i][j] = 0.f;
      }

  }

  public float findMaxSigma(int mlevel, float [][] H, int [] t)
  {
    t[0] = 0;
    float maxSig= 0.f;
    switch(mlevel)
    {
    case 2:
      for (int i= 1; i < NGRAY-mlevel; i++) // t1
      {
	float Sq = H[1][i] + H[i+1][255];
	if (maxSig < Sq)
	{
	  t[1] = i;
	  maxSig = Sq;
	}
      } 
      break;
    case 3:
      for (int i= 1; i < NGRAY-mlevel; i++) // t1
	for (int j = i+1; j < NGRAY-mlevel +1; j++) // t2
	{
	  float Sq = H[1][i] + H[i+1][j] + H[j+1][255];
	  if (maxSig < Sq)
	  {
	    t[1] = i;
	    t[2] = j;
	    maxSig = Sq;
	  }
	} 
      break;
    case 4:
      for (int i= 1; i < NGRAY-mlevel; i++) // t1
	for (int j = i+1; j < NGRAY-mlevel +1; j++) // t2
	  for (int k = j+1; k < NGRAY-mlevel + 2; k++) // t3
	  {
	    float Sq = H[1][i] + H[i+1][j] + H[j+1][k] + H[k+1][255];
	    if (maxSig < Sq)
	    {
	      t[1] = i;
	      t[2] = j;
	      t[3] = k;
	      maxSig = Sq;
	    }
	  } 
      break;
    case 5:
      for (int i= 1; i < NGRAY-mlevel; i++) // t1
	for (int j = i+1; j < NGRAY-mlevel +1; j++) // t2
	  for (int k = j+1; k < NGRAY-mlevel + 2; k++) // t3
	    for (int m = k+1; m < NGRAY-mlevel + 3; m++) // t4
	  {
	    float Sq = H[1][i] + H[i+1][j] + H[j+1][k] + H[k+1][m] + H[m+1][255];
	    if (maxSig < Sq)
	    {
	      t[1] = i;
	      t[2] = j;
	      t[3] = k;
	      t[4] = m;
	      maxSig = Sq;
	    }
	  } 
      break;
    }
    return maxSig; 
  }

}
