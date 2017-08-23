import java.util.Random;
import java.awt.Color;
//import java.io.Console;
import java.io.*;

//TODO: make it reach EQUILIBRIUM for each run by waiting until 1000 MC sweeps have happened.
// write values to a matrix and then plot a contour plot of these, with p1 and p3 as the two axis

public class SIRSplot{

    //function to call visualize and draw matrix 
    static void visualize(Visualization vis, MyMatrix grid, int rows, int cols){
        for (int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                
                vis.set(c, r, grid.getElement(r,c) == 0.0 ? Color.ORANGE : (grid.getElement(r,c) == 1.0? Color.RED : Color.green) );   //if 0 it it S so susceptible so is orange, if 1 is I so is infected so red, if 2 is ,R, recovered, so is green.
            }
        }
        vis.draw();
    }

    //returns current fraction of the total number of Infected cells in matrix  
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

    
    //returns current total number of Infected cells in matrix  
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

        if(args.length != 3){
            System.out.println("Incorrect number of arguments...\n1) input dimension \n2) value for probability 2 (I->R)\n3) level of detail for contour plot \n ");
            System.exit(0);
        }
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[0]);
        

        //double prob1 = Double.parseDouble(args[1]);
        double prob2 = Double.parseDouble(args[1]);
        //double prob3 = Double.parseDouble(args[3]);

        double prob1, prob3;
        
        double averFrac;
        
        int detail = Integer.parseInt(args[2]);
        
        double[][] contour = new double[detail+1][detail+1];
        
        double[][] variance = new double[detail+1][detail+1];
        double squaredAverFrac;
        double totFrac;
        double squaredTotFrac;
        //  char option = args[2].charAt(0);
        //String option = args[2];
        
        
        MyMatrix A = new MyMatrix( rows, cols );
        Random rand = new Random();
        int element;
        int loops;
        
        double infected = 0;
        double infectedSquared = 0;
        double runningTotalInfected = 0;
        double runningTotalInfectedSquared = 0;
        double calcs = 0;
        double averageInfected = 0;
        double averageInfectedSquared = 0;
        double N = 0;
        
        
        double contourSum = 0;
        double contourSingleRun = 0;
        double varianceSum = 0;
        double varianceSingleRun = 0;
        
        

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
        //Visualization vis = new Visualization(rows,cols);
        //visualize(vis, A, rows, cols);
        
        int REPEATS = 5;
        
        int SIMS = 1000;
        int EQUILIBRIUM = SIMS/2;
        
        for(int p1 = 1; p1<=detail; p1++){

            prob1 = (double)p1/(detail-1/((detail-1)*10));   //make sure probability value is in range 0-1
            
            for(int p3 = 1; p3<=detail; p3++){

                prob3 = (double)p3/(detail-1/((detail-1)*10));        //make sure probability value is in range 0-1
                
                for(int rep = 0; rep<=REPEATS; rep++){
                    //initialise number of counts for updates / N value
                    loops = 0;
                    totFrac = 0;
                    squaredTotFrac = 0;
                    
                    
                    
                    //loop through number of monte-carlo updates of system
                    while(loops < SIMS){    
                        
                        //do 1 monte-carlo sweep:
                        for(int i = 0; i<rows*cols; i++){
                            Infect(A, prob1, prob2, prob3);
                        }
                        
                        // if(loops%(rows*cols)== 0){
                        //    visualize(vis, A, rows, cols);
                        
                        //} 
                        //visualize(vis, A, rows, cols);
                        //Thread.sleep(1000); //number in milliseconds
                        
                        loops++;
                        
                        //once half of total number of sims has been completed, equilibrium state is assumed
                        //now carry out calculations for <I>, <I^2> etc...
                        if(loops>EQUILIBRIUM){
                            //if(loops == EQUILIBRIUM + 1){
                            //System.out.println("Now averaging\n");
                            //}
                            
                            //calculate values for every 10 MC sweeps
                            if(loops%10==0){
                                
                                // all variance etc calculations here //
                                
                                infected = totI(A);
                                //System.out.printf("I = %d\n", infected);
                                infectedSquared = infected*infected;
                                //System.out.printf("I = %d\n", infectedSquared);
                                
                                runningTotalInfected += infected;
                                runningTotalInfectedSquared += infectedSquared;
                                
                                calcs ++;
                                
                                /* totFrac += fracTotI(A);
                        squaredTotFrac += Math.pow(totI(A), 2); */
                            }
                        }
                        
                    }
                    
                    
                    averageInfectedSquared = runningTotalInfectedSquared / calcs;  // <I^2>
                    averageInfected = runningTotalInfected / calcs; //<I>
                    N = rows * cols;
                    
                    //System.out.printf("<I^2> = %f\n", averageInfectedSquared);
                    //System.out.printf("<I> = %f\n", averageInfected);
                    //System.out.printf("p1 = %d, p3 = %d, prob1 = %f, prob3 = %f\n", p1, p3, prob1, prob3);
                    
                    //averFrac = totFrac/((loops-EQUILIBRIUM)/10);
                    //squaredAverFrac = squaredTotFrac/((loops-EQUILIBRIUM)/10);
                    // contour[p1][p3] = averageInfected/N;  // <= <I>
                    //calculate variance for this value: (<I^2> - <I>^2)/N
                    //variance[p1][p3] = (averageInfectedSquared - (averageInfected*averageInfected))/(N*N);
                    
                    
                    contourSingleRun = averageInfected/N;  // <= <I>
                    //calculate variance for this value: (<I^2> - <I>^2)/N
                    varianceSingleRun = (averageInfectedSquared - (averageInfected*averageInfected))/(N*N);
                    
                    contourSum += contourSingleRun;
                    varianceSum += varianceSingleRun;
                    
                    runningTotalInfected = 0;
                    runningTotalInfectedSquared = 0;
                    averageInfected = 0;
                    averageInfectedSquared = 0;
                    calcs = 0;
                    
                    
                    
                    
                    // System.out.printf("current run completed...\n\n");
                    
                    //re-initialise matrix here
                    for( int i=0; i<rows; i++ ) {
                        for(  int j=0; j<cols; j++ ) {
                            element = rand.nextInt(3);   //should fill randomly with 0,1,2
                            
                            A.setElement(i,j, element ) ; //public int nextInt(int bound);
                            
                            //rand = Random.nextInt(1); 
                        }
                    }                
                }
                
                contour[p1][p3] = contourSum/REPEATS;
                variance[p1][p3] = varianceSum/REPEATS;
                
                contourSum = 0;
                contourSingleRun = 0;
                varianceSum = 0;
                varianceSingleRun = 0;
            }
            System.out.printf("progress: %.2f%%\r", 100*(double)p1/detail);
        }
        
        String fname = "p1-p3_ave_frac.dat";
        PrintWriter output = new PrintWriter(new FileWriter(fname));
        
        String fname2 = "p1-p3_variance.dat";
        PrintWriter output2 = new PrintWriter(new FileWriter(fname2));
        
        for(int i = 0; i<detail; i++){
            for(int j = 0; j<detail;j++){
                output.printf("%1.6f ", contour[i][j]);  //in output file, columns are p1 values, rows are p3 values, 
                //-> python code plots p1 along x axis and p3 along y axis
                
                output2.printf("%1.6f ", variance[i][j]);
            }
            output.printf("\n");
            output2.printf("\n");
        }
        output.close();
        output2.close();
        
        System.exit(0);
    }
    
}
