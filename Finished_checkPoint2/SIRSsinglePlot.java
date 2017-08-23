import java.util.Random;
import java.awt.Color;
import java.io.*;
//import java.io.Console;

public class SIRSsinglePlot{

    //function to call visualize and draw matrix 
    static void visualize(Visualization vis, MyMatrix grid, int rows, int cols){
        for (int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                
                //vis.set(c, r, grid.getElement(r,c) == 0.0 ? Color.BLACK : (grid.getElement(r,c) == 1.0? Color.RED : Color.green) );   //if 0 it it S so susceptible so is orange, if 1 is I so is infected so red, if 2 is ,R, recovered, so is green.
                vis.set(c, r, grid.getElement(r,c) == 0.0 ? Color.ORANGE : (grid.getElement(r,c) == 1.0? Color.RED : Color.GREEN) );
            }
        }
        vis.draw();
    }

    //returns current total number of Infected cells in matrix  
    static double fracTotI(MyMatrix data){
        //value I represents is 1 therefore count the number of elements that contain a 1 in matrix
        int counts = 0;
        for (int i=0; i<data.rows(); i++){
            for(int j=0; j<data.cols(); j++){
                if(data.getElement(i,j) == 1){
                    counts++;
                }
            }
        }
        return (double)counts/(data.rows()*data.cols());
    }

    static int totI(MyMatrix data){
        //value I represents is 1 therefore count the number of elements that contain a 1 in matrix
        int counts = 0;
        for (int i=0; i<data.rows(); i++){
            for(int j=0; j<data.cols(); j++){
                if(data.getElement(i,j) == 1){
                    counts++;
                }
            }
        }
        return counts;
    }

    static void Infect(MyMatrix data, double prob1, double prob2, double prob3){

        Random rand = new Random();
        int row = rand.nextInt(data.rows());
        int col = rand.nextInt(data.rows());
        double rplus, rminus, cplus, cminus;

        double prob = Math.random(); //should be a value 0=<prob<1

        //if the random element is susceptible then infect with certain probability
        if(data.getElement(row, col) == 0){



            //check neighbours to see if any are infected and can pass on infection
            rplus = data.getElement((row+1)%data.rows(), col);
            rminus = data.getElement((row-1+data.rows())%data.rows(), col);
            cplus = data.getElement(row,(col+1)%data.rows());
            cminus = data.getElement(row,(col-1+data.rows())%data.rows());
            // 0 == S, 1 == I, 2 == R
            if(rplus == 1 || rminus == 1 || cplus == 1 || cminus == 1){

                // infect with probability 'prob1'
                if(prob<=prob1){
                    data.setElement(row, col, 1);
                }
            }
        }
        
        //if the element is Infected then change to R with probability 'prob2'
        else if(data.getElement(row, col) == 1){
            if(prob<=prob2){
                data.setElement(row, col, 2);
            }
        }
        //element is R then change to susceptible with probability 'prob3'
        else{
            if(prob<=prob3){
                data.setElement(row, col, 0);
            }
        }


    }








    public static void main (String[] args) throws Exception{

        if(args.length != 2){
            System.out.println("Incorrect number of arguments...\n1) input dimension \n2) option w,s,i etc...\n");
            System.exit(0);
        }
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[0]);
        
        //java SIRSsinglePlot 50 0.33 0.5 0.4 10000 seemed to work
        
        char option = args[1].charAt(0);
        double prob1 = 0;
        double prob2 = 0;
        double prob3 = 0;
        //int range = 10000;
        
        int range = 500;
        //close to waves from own plot
        if(option == 's'){ 
            prob1 = 0.31;
            prob2 = 0.5;
            prob3 = 0.84;
        }
        //plots waves from 'known values'
        if(option == 'w'){ 
            prob1 = 0.9;
            prob2 = 0.5;
            prob3 = 0.08;
        }
        
        if(option == 'p'){ 
            prob1 = 1.0;
            prob2 = 0.5;
            prob3 = 0.05;
        }
        
        //plots absorbing state from own plot
        if(option == 'a'){ 
            prob1 = 0.2;
            prob2 = 0.5;
            prob3 = 0.2;
        }

        //plots dynamic equilibrium state from own plot
        if(option == 'd'){ 
            prob1 = 0.7;
            prob2 = 0.5;
            prob3 = 0.7;
        }          
        

        
        //double averI = 0;
        double averFrac = 0;
        
        //  char option = args[2].charAt(0);
        //String option = args[2];
        
        
        String fname = "singlePlot_"+option+".dat";
        PrintWriter output = new PrintWriter(new FileWriter(fname));
        
        MyMatrix A = new MyMatrix( rows, cols );
        Random rand = new Random();
        int element;
        

        //Random.nextInt(1);   //supposed to randomly make number between 0,1  (can subtract later to make 1, -1)
        for( int i=0; i<rows; i++ ) {
            for(  int j=0; j<cols; j++ ) {
                element = rand.nextInt(3);   //should fill randomly with 0,1,2
                
                A.setElement(i,j, element ) ; //public int nextInt(int bound);
                
                //rand = Random.nextInt(1); 
            }
        }
        
        //A.printMatrix();
        
        //attempt to convert number matrix into visual coloured matrix made of pixels:
        // int[][] grid = new int[rows][cols];
        Visualization vis = new Visualization(rows,cols);
        visualize(vis, A, rows, cols);
        
        
        
        
        //initialise number of counts for updates / N value
        int i = 0;
        
        //int range = (int)speed*800;
        //int writeCount = 0;
        //this while loop allows the user to exit it when return key is pressed
        
        
        while(i < range){    
            
            for(int q = 0; q<rows*cols; q++){
                Infect(A, prob1, prob2, prob3);
            }
            
            visualize(vis, A, rows, cols);
            //output.printf("%f\n", fracTotI(A));
            
            //Infect(A, prob1, prob2, prob3);
            
            //if(i>range-500){ 
            
            output.printf("%f\n", fracTotI(A));   
            //}
            
            
            
            
            
            
            //this while loop allows the user to exit it when return key is pressed
            /*  while(i < range){    
            
            for(int q = 0; q<rows*cols; q++){
                Infect(A, prob1, prob2, prob3);
            }
            //Infect(A, prob1, prob2, prob3);
            visualize(vis, A, rows, cols);


            
            
            output.printf("%d %f\n", i, fracTotI(A)); */
            //visualize(vis, A, rows, cols);
            //Thread.sleep(1000); //number in milliseconds
            
            i++;
            averFrac += fracTotI(A);
            
            
        }

        //speed is measured in number of MC-sweeps
        
        /* if(i%(speed)== 0){
                
                visualize(vis, A, rows, cols);
                if(writeCount%10 == 0){
                    output.printf("%f\n", fracTotI(A));
                }
                writeCount++;}  */
        
        
        //visualize(vis, A, rows, cols);
        //Thread.sleep(1000); //number in milliseconds
        
        
        averFrac = averFrac/i;
        System.out.printf("average fraction of infected cells = %f\n", averFrac); 
        output.close();
        System.exit(0);
    }
}

