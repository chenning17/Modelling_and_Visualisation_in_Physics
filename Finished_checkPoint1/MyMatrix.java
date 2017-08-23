// C. Henning 23/01/2017
// Matrix class


/*Define Matrix	class:		
    Implement the class with suitable methods	for:	
    Instantiation	
    Addition	
    Multiplication	
    Perform same operations but using a matrix algebra	package	
    By way of a matrix exercise you will formulate a chi^2 for later use in parameter 		  estimation	*/

public class MyMatrix{

    private int rows = 0;
    private int cols = 0;
    private double[][] data;

    //public methods
    //constructor with dimensions
    public MyMatrix( int row, int col )
    {
        rows = row ;
        cols = col ;
        data = new double[rows][cols];
        for( int i=0; i<rows; i++ ) 
        {
            for(  int j=0; j<cols; j++ )
            {
                data[i][j] = 0;
            }
        }
    }	

    //addition of a specified matrix to a matrix if they are of the same dimension

    public MyMatrix add( MyMatrix A )
    {

        if( this.rows()== A.rows() &&  this.cols()== A.cols() ) 
        {
            MyMatrix Added = new MyMatrix( this.rows(), this.cols() ) ;
            for( int i=0; i<rows;i++ ) 
            {
                for(  int j=0; j<cols; j++ )
                {
                    Added.setElement(i,j, this.getElement(i,j) + A.getElement(i,j) ) ;
                }
            }
            return Added ;
        }
        else return null;
    }

    //multiplication of two matrices
    public MyMatrix multiply( MyMatrix A )
    {
        if( this.cols() == A.rows() ) 
        {
            MyMatrix Multi = new MyMatrix( this.rows(), A.cols() ) ;
            
            for( int i=0; i<this.rows(); i++ ) 
            {
                for(  int j=0; j<A.cols(); j++ ) 
                {
                    double value = 0;
                    for( int p=0; p< this.cols(); p++ ) 
                    { 
                        value += this.getElement( i, p ) * A.getElement( p, j) ;
                        
                    }
                    Multi.setElement(i,j,value);
                }
            }
            return Multi ;
        }
        else return null ;
    }

    //copy/clone

    public MyMatrix copy()
    {
        MyMatrix Clone = new MyMatrix( this.rows(), this.cols() ) ;
        for( int i=0; i<rows; i++ ) 
        {
            for(  int j=0; j<cols; j++ )
            {
                Clone.setElement(i,j, this.getElement(i,j));
            }
        }
        return Clone ;
    }
    


    //set elemenents in matrix
    public void setElement( int row,  int col, double value )
    {
        if( inMatrix( row,col) ) {
            data[row][col] = value ;
        }
    }

    //get specific element from matrix
    public double getElement( int row,  int col )
    {
        if( inMatrix( row,col) ) 
        {
            return data[row][col]; //returns element specified
        }
        else
        {
            return 0; //specified element not in matrix if here
        }
    }





    //Check to see if specified elements exist in matrix
    public boolean inMatrix( int i, int j ) 
    {
        if( (i>=0) && (i<rows) && (j>=0) && (j<cols) ) {return true;}
        return false;
    }

    //method to print specific matrix element
    public void printElement(int i, int j) 
    {
        System.out.println(Double.toString(data[i][j]));	
    }

    //method to convert specific matrix element to string
    public String stringElement(int i, int j) 
    {
        return Double.toString(data[i][j]);	
    }
    
    //method to print entire matrix to terminal
    public void printMatrix()
    {
        String temp = "";
        for(int i=0; i<rows; i++)
        {
            for( int j=0; j<cols; j++ )
            {
                if(Double.parseDouble(stringElement(i,j))>0){
                    temp += "+";
                }
                temp += stringElement(i,j) + "\t"; //call string element and add it to temp string 
            }
            System.out.println(temp); //prints matrix row on single line
            temp = "";
        }
    }

    //Use to return number of rows and cols for use in comparison operations
    public int rows() { return rows ; }
    public int cols() { return cols ; }

}
