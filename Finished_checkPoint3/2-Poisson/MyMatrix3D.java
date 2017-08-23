// C. Henning 23/01/2017
// Matrix class
import java.io.*;

/*Define Matrix	class:		
    Implement the class with suitable methods	for:	
    Instantiation	
    Addition	
    Multiplication	
    Perform same operations but using a matrix algebra	package	
    By way of a matrix exercise you will formulate a chi^2 for later use in parameter 		  estimation	*/

public class MyMatrix3D{

    private int rows = 0;
    private int cols = 0;
    private int height = 0;
    private double[][][] data;

    //public methods
    //constructor with dimensions
    public MyMatrix3D( int row, int col, int h )
    {
        rows = row ;
        cols = col ;
        height = h;
        
        data = new double[rows][cols][height];
        for( int i=0; i<rows; i++ ) 
        {
            for(  int j=0; j<cols; j++ )
            {
                for(  int k=0; k<height; k++ )
                {
                    data[i][j][k] = 0;
                }
            }
        }
    }	

    //addition of a specified matrix to a matrix if they are of the same dimension

    public MyMatrix3D add( MyMatrix3D A )
    {

        if( this.rows()== A.rows() &&  this.cols()== A.cols() &&  this.height()== A.height() ) 
        {
            MyMatrix3D Added = new MyMatrix3D( this.rows(), this.cols() , this.height()) ;
            for( int i=0; i<rows;i++ ) 
            {
                for(  int j=0; j<cols; j++ )
                {
                    for(  int k=0; k<height; k++ )
                    {
                        Added.setElement(i,j,k, this.getElement(i,j,k) + A.getElement(i,j,k) ) ;
                    }
                }
            }
            return Added ;
        }
        
        else return null;
    }



    //copy/clone matrix

    public MyMatrix3D copy()
    {
        MyMatrix3D Clone = new MyMatrix3D( this.rows(), this.cols(), this.height() ) ;
        for( int i=0; i<rows; i++ ) 
        {
            for(  int j=0; j<cols; j++ )
            {
                for(  int k=0; k<height; k++ )
                {
                    Clone.setElement(i,j,k, this.getElement(i,j,k));
                }
            }
        }
        return Clone ;
    }
    




    //set elements in matrix
    public void setElement( int row,  int col, int height, double value )
    {
        if( inMatrix( row,col, height) ) {
            data[row][col][height] = value ;
        }
    }

    //get specific element from matrix
    public double getElement( int row,  int col, int height )
    {
        if( inMatrix( row,col, height) ) 
        {
            return data[row][col][height]; //returns element specified
        }
        else
        {
            return 0; //specified element not in matrix if here
        }
    }





    //Check to see if specified elements exist in matrix
    public boolean inMatrix( int i, int j, int k ) 
    {
        if( (i>=0) && (i<rows) && (j>=0) && (j<cols) && (k>=0) && (k<height) ) {return true;}
        return false;
    }


    //method to print specific matrix element
    public void printElement(int i, int j, int k) 
    {
        System.out.println(Double.toString(data[i][j][k]));	
    }

    //method to convert specific matrix element to string
    public String stringElement(int i, int j, int k) 
    {
        return Double.toString(data[i][j][k]);	
    }
    
    //method to print entire matrix to terminal
    public void printMatrix3D()
    {
        String temp = "";
        for(int i=0; i<rows; i++)
        {
            for( int j=0; j<cols; j++ )
            {
                for( int k=0; k<height; k++ )
                {
                    if(Double.parseDouble(stringElement(i,j,k))>0){
                        temp += "+";
                    }
                    temp += stringElement(i,j,k) + "\t"; //call string element and add it to temp string 
                }
                System.out.println(temp); //prints matrix row on single line
                
            temp = "";
            }
            
            System.out.println("\n");
            
        }
    }  
    
    
    //method to print entire matrix to terminal
    public void printMatrixCenterSlice()
    {
        String temp = "";
        for(int i=0; i<rows; i++)
        {
            for( int j=0; j<cols; j++ )
            {
               
                    if(Double.parseDouble(stringElement(i,j,height/2))>0){
                        temp += "+";
                    }
                    temp += stringElement(i,j,height/2) + "\t"; //call string element and add it to temp string 
               
                
            }
            System.out.println(temp); //prints matrix row on single line
                
            temp = "";
            
           
            
        }
         System.out.println("\n");
    } 


   //method to print entire matrix to terminal
    public void printMatrixCenterSlice(PrintWriter output)
    {
        String temp = "";
        for(int i=0; i<rows; i++)
        {
            for( int j=0; j<cols; j++ )
            {
               
                    if(Double.parseDouble(stringElement(i,j,height/2))>0){
                        temp += "+";
                    }
                    temp += stringElement(i,j,height/2) + "\t"; //call string element and add it to temp string 
               
                
            }
            output.println(temp); //prints matrix row on single line
                
            temp = "";
            
           
            
        }
         output.println("\n");
    }     


    //Use to return number of rows and cols for use in comparison operations
    public int rows() { return rows ; }
    public int cols() { return cols ; }
    public int height() { return height ; }

}
