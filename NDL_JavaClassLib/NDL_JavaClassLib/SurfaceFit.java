
import Jama.Matrix;



/**
 *
 * @author balam
 */
public class SurfaceFit {

    /**
     * @return the gFit
     */
    public double[][] getgFit() {
        return gFit;
    }

    /**
     * @param gFit the gFit to set
     */
    private void setgFit(double[][] gFit) {
        this.gFit = gFit;
    }
    public SurfaceFit(){
        setPolyOrderX(5);
        setPolyOrderY(5);
    }
    public SurfaceFit(int PolyX, int PolyY){
        this.setPolyOrderX(PolyOrderX);
        this.setPolyOrderY(PolyOrderY);
    }
private double [][] gFit ;
    /**
     * @return the PolyOrderY
     */
    public int getPolyOrderY() {
        return PolyOrderY;
    }

    /**
     * @param PolyOrderY the PolyOrderY to set
     */
    public void setPolyOrderY(int PolyOrderY) {
        this.PolyOrderY = PolyOrderY;
    }

    /**
     * @return the PolyOrderX
     */
    public int getPolyOrderX() {
        return PolyOrderX;
    }

    /**
     * @param PolyOrderX the PolyOrderX to set
     */
    public void setPolyOrderX(int PolyOrderX) {
        this.PolyOrderX = PolyOrderX;
    }
    private int PolyOrderX;
    private int PolyOrderY;
    
public  double[][] FitSurface( double[][] TheImage )
{
    int Nrows = TheImage.length;
    int Ncols = TheImage[0].length;
    int Npixels = Nrows*Ncols;
    int n=0;
    int r, c, cnt, i, j, k, MatVal, nCol;
    int Dim1, Dim2;
    int PO_2xp1 = Math.max((2 * getPolyOrderX() + 1), (2 * getPolyOrderY() + 1));
    int MatSize = (getPolyOrderX()+1)*(getPolyOrderY()+1);

    // Create the x, y, and z arrays from which the image to be fitted
    double []X = new double[Npixels];
    double []Y = new double[Npixels];
    double []Z = new double[Npixels];
    cnt = 0;
    for(r=0; r<Nrows; r++) {
        for(c=0; c<Ncols; c++) {
            X[cnt] = c;
            Y[cnt] = r;
            Z[cnt] = TheImage[r][c];
            cnt++;
        }
    }

    // Notation:
    //  1)  The matrix [XY] is made up of sums (over all the pixels) of the
    //      row & column indices raised to various powers.  For example,
    //      sum( y^3 * x^2 ).  It turns out, within [XY] there are 
    //      patterns to the powers and these patterns are computed
    //      in the matrices [nnx] and [nny].
    //  2)  [Sxyz] represents all of the possible sums that will be used to
    //      create [XY] and [Z].  We compute all of these sums even though 
    //      some of them might not be utilized... it's just easier.
	double [][]XY_mat = new double[MatSize][MatSize];
    int [][]nnx = new int[MatSize][MatSize];
    int [][]nny = new int[MatSize][MatSize];
    int []aRow = new int[MatSize];

    // Create all the possible sums, Sxyz[][][]
    //IJ.showProgress(1,6);
    //IJ.showStatus("Preparing sums matrix");
    double[][][] Sxyz = new double[PO_2xp1][PO_2xp1][2];
    double x, y, z;
    double powx, powy, powz;
    int nx, ny, nz;
    // Initialize all of the sums to zero
    for(nx=0; nx<PO_2xp1; nx++) {
        for(ny=0; ny<PO_2xp1; ny++) {
            for(nz=0; nz<2; nz++) {
                Sxyz[nx][ny][nz] = 0.0;
            }
        }
    }
    // Produce the sums
    for( i=0; i<Npixels; i++) {
        x = X[i]; y = Y[i]; z = Z[i];
        for(nx=0; nx<PO_2xp1; nx++) {
            powx = java.lang.Math.pow(x,(double)nx);
            for(ny=0; ny<PO_2xp1; ny++) {
                powy = java.lang.Math.pow(y,(double)ny);
                for(nz=0; nz<2; nz++) {
                    powz = java.lang.Math.pow(z,(double)nz);
                    Sxyz[nx][ny][nz] += powx * powy * powz;
                }
            }
        }
    }

    // Create the patterns of "powers" for the X (horizontal) pixel indices
    //IJ.showProgress(2,6);
    int iStart = 2 * getPolyOrderX();
    Dim1 = 0;
    while(Dim1<MatSize) {
        for(i=0; i<(getPolyOrderY()+1); i++) {
            // A row of nnx[][] consists of an integer that starts with a value iStart and
            //  1) is repeated (PolyOrderX+1) times
            //  2) decremented by 1
            //  3) Repeat steps 1 and 2 for a total of (PolyOrderY+1) times
            nCol = 0;
            for(j=0; j<(getPolyOrderX()+1); j++ ) {
                for(k=0; k<(getPolyOrderY()+1); k++) {
                    aRow[nCol] = iStart - j;
                    nCol++;
                }
            }
            // Place this row into the nnx matrix
            for(Dim2=0; Dim2<MatSize; Dim2++ ) {
                nnx[Dim1][Dim2] = aRow[Dim2];
            }
            Dim1++;
        }
        iStart--;
    }
    
    // Create the patterns of "powers" for the Y (vertical) pixel indices
    //IJ.showProgress(3,6);
    Dim1 = 0;
    while(Dim1<MatSize) {
        iStart = 2 * getPolyOrderY();
        for(i=0; i<(getPolyOrderY()+1); i++) {
            // A row of nny[][] consists of an integer that starts with a value iStart and
            //  1) place in matrix
            //  2) decremented by 1
            //  3) 1 thru 2 are repeated for a total of (PolyOrderX+1) times
            //  4) 1 thru 3 are repeat a total of (PolyOrderY+1) times
            nCol = 0;
            for(j=0; j<(getPolyOrderX()+1); j++ ) {
                for(k=0; k<(getPolyOrderY()+1); k++) {
                    aRow[nCol] = iStart - k;
                    nCol++;
                }
            }
            // Place this row into the nnx matrix
            for(Dim2=0; Dim2<MatSize; Dim2++ ) {
                nny[Dim1][Dim2] = aRow[Dim2];
            }
            Dim1++;
            iStart--;
        }
    }

    // Put together the [XY] matrix
	for(r=0; r<MatSize; r++) {
		for(c=0; c<MatSize; c++) {
			nx = nnx[r][c];
			ny = nny[r][c];
			XY_mat[r][c] = Sxyz[nx][ny][0];
		}
	}

    // Put together the [Z] vector
    //IJ.showProgress(4,6);
	double[] Z_mat = new double[MatSize];
    c = 0;
    for(i=getPolyOrderX(); i>=0; i--) {
		for(j=getPolyOrderY(); j>=0; j--) {
            Z_mat[c] = Sxyz[i][j][1];
            c++;
        }
    }

    // Solve the linear system [XY] [P] = [Z] using the Jama.Matrix routines
	// 	[A_mat] [x_vec] = [b_vec]
	// (see example at   http://math.nist.gov/javanumerics/jama/doc/Jama/Matrix.html)
    //IJ.showProgress(5,6);
    //IJ.showStatus("Solving linear system of equations");
	Matrix A_mat = new Matrix(XY_mat);
	Matrix b_vec = new Matrix(Z_mat, MatSize);
	Matrix x_vec = A_mat.solve(b_vec);

	// Place the Least Squares Fit results into the array Pfit
	double[] Pfit = new double[MatSize];
	for(i=0; i<MatSize; i++) {
		Pfit[i] = x_vec.get(i, 0);
	}

	// Reformat the results into a 2-D array where the array indices
    // specify the power of pixel indices.  For example,
    // z =    (G[2][3] y^2 + G[1][3] y^1 + G[0][3] y^0) x^3
    //      + (G[2][2] y^2 + G[1][2] y^1 + G[0][2] y^0) x^2
    //      + (G[2][1] y^2 + G[1][1] y^1 + G[0][1] y^0) x^1
    //      + (G[2][0] y^2 + G[1][0] y^1 + G[0][0] y^0) x^0
    double[][] Gfit = new double[getPolyOrderY() + 1][getPolyOrderX() + 1];
    c = 0;
    for(i=getPolyOrderX(); i>=0; i--) {
		for(j=getPolyOrderY(); j>=0; j--) {
            Gfit[j][i] = Pfit[c];
            c++;
        }
    }
    this.setgFit(Gfit);
    return ( Gfit );
} 
}
