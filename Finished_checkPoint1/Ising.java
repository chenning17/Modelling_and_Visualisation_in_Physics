import java.util.Random;
import java.awt.Color;
import java.io.Console;

public class Ising{

    //function to call visualize and draw matrix 
    static void visualize(Visualization vis, MyMatrix grid, int rows, int cols){
        for (int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                //vis.set(c, r, grid.getElement(r,c) == 1.0 ? Color.BLACK : Color.WHITE);
                //vis.set(c, r, grid.getElement(r,c) == 1.0 ? Color.RED : Color.GREEN);
                //vis.set(c, r, grid.getElement(r,c) == 1.0 ? Color.BLUE : Color.ORANGE);
                //vis.set(c, r, grid.getElement(r,c) == 1.0 ? Color.PINK : Color.YELLOW);
                vis.set(c, r, grid.getElement(r,c) == 1.0 ? Color.red : Color.blue);
            }
        }
        vis.draw();
    }

    //Glauber function to carry out glauber dynamics on ising system
    static void glauber(MyMatrix lat, int K, int J, double T){
        //pick random matrix element
        Random rand = new Random();
        int row = rand.nextInt(lat.rows());//should return random matrix element in dimension of matrix
        int col = rand.nextInt(lat.cols());
        
        
        //check energy before flip:
        double Eorig = checkEnergy(J, lat, row, col, false);
        
        //check energy after flip:
        double Efin = checkEnergy(J, lat, row, col, true);
        
        double deltaE = Efin - Eorig;
        //used to see if particle should be flipped or not
        double prob = Math.random(); //should be a value 0=<prob<1
        double calcProb = Math.exp(-1*deltaE/(K*T));
        
        //if change in energy is -ve then flip spin i.e. value * -1
        if(deltaE < 0){
            lat.setElement(row, col, lat.getElement(row,col)*-1); //flip element
        }
        
        else{
            if(prob<=calcProb){
                lat.setElement(row, col, lat.getElement(row,col)*-1); //flip element
            }
            
        }
        
        //System.out.printf("row = %d, col = %d \n", row, col);
        
    }

    
    

    
    static void kawasaki(MyMatrix lat, int K, int J, double T){
        // glauber it but without flips to original matrix
        // store values of energy
        // glauber a second point but without changing original matrix (yet)
        // if sum of both deltaE < 0 do swap
        //else do same probability of it happening
        
        
        //pick random matrix element
        Random rand = new Random();
        int row1 = rand.nextInt(lat.rows());//should return random matrix element in dimension of matrix
        int col1 = rand.nextInt(lat.cols());
        double deltaE1 = deltaEnergy(lat, J, row1, col1);
        
        
        int row2 = rand.nextInt(lat.rows());//should return random matrix element in dimension of matrix
        int col2 = rand.nextInt(lat.cols());
        double deltaE2 = deltaEnergy(lat, J, row2, col2);
        
        double sumDeltaE = deltaE1 + deltaE2;
        
        double prob = Math.random(); //should be a value 0=<prob<1
        double calcProb = Math.exp(-1*sumDeltaE/(K*T));
        double copy;
        if(sumDeltaE < 0 ){
            //swap
            copy = lat.getElement(row1, col1);
            lat.setElement(row1, col1, lat.getElement(row2,col2)); 
            
            lat.setElement(row2, col2, copy); 
        }
        else{
            //swap with certain probability
            if(prob<=calcProb){
                //swap
                copy = lat.getElement(row1, col1);
                lat.setElement(row1, col1, lat.getElement(row2,col2)); 
                //lat.setElement(row1, col1, lat.getElement(row1,col1)); 
                lat.setElement(row2, col2, copy);
            }
        }
        
        //TODO add compensation for ones that are next to each other
    }
    
    //Glauber function to carry out glauber dynamics on ising system
    static double deltaEnergy(MyMatrix lat, int J, int row, int col){
        
        double deltaE;        
        //check energy before flip:
        double Eorig = checkEnergy(J, lat, row, col, false);
        
        //check energy after flip:
        double Efin = checkEnergy(J, lat, row, col, true);
        
        return deltaE = Efin - Eorig;
        
    }

    
    
    
    
    
    
    
    
    //returns total energy of nearest neighbours of a lattice point
    static double checkEnergy(int J, MyMatrix data, int row, int col, boolean flip){
        //nearest neighbours calc by +1, -1 of matrix elements j,i   e.g. 3,7 -> n.n. = 3-6, 3-8, 4-7, 2-7 ---- 0-0 >> 0-1, 0-matrix dimension 
        //row and column +/- 1 element for nearest neighbours

        
        
        double rplus, rminus, cplus, cminus;
        double E = 0;

        rplus = data.getElement((row+1)%data.rows(), col);
        rminus = data.getElement((row-1+data.rows())%data.rows(), col);
        cplus = data.getElement(row,(col+1)%data.rows());
        cminus = data.getElement(row,(col-1+data.rows())%data.rows());


        E += cplus*-1* data.getElement(row,col);
        E += cminus*-1*data.getElement(row,col);
        E += rplus*-1* data.getElement(row,col);
        E += rminus*-1*data.getElement(row,col);
        if(flip==true){
            return J*E*-1;
        } 
        else{
            return J*E;
        }   
        
    }
    
    
    //returns total energy of whole lattice
    static double totEnergy(MyMatrix lat, int J){
        double totE = 0;
       for(int i=0; i<lat.rows(); i++)
       {
            for(int j=0; j<lat.cols(); j++)
            {
                totE += checkEnergy(J, lat, i, j, false);
            }
        }
        return totE;
    }
    
    
    
    
    
    
    
    
    // function used to calculate total magnetisation of the system i.e. sum of all elements in matrix
    
    static double totMagnetisation(MyMatrix lat){
        double magnetisation = 0;
        for(int i=0; i<lat.rows(); i++){
            for(int j=0; j<lat.cols(); j++){
                magnetisation += lat.getElement(i,j);
            }
        }
        return magnetisation;
    }
    
    //function used to calculate the average magnetisation of the system by using the previous 
    //average value, multiplying it by the previous value for the total number of measurements, 
    //then adding the current value for the total magnetisation and taking the average of this value.
    
    static double averMagnetisation(double currentTotMag, double prevAverage, int numMeasures){
        double newAverage = (prevAverage * (numMeasures - 1) + currentTotMag)/numMeasures;
        return newAverage;
    }
    
    static double susceptibility(double currentTotMag, double averageMag, int numMeasures, int K, double T){
        double suscept = (1/(numMeasures * K * T))  * ((Math.pow(currentTotMag, 2)/numMeasures)   -   (Math.pow(averageMag,2)));    //    (1/N kb T) * ( <current Mag ^2>) - (average mag)^2)        <-- N = num of measurements???
        return suscept;
    }
    
    
    

   
    
    
    


    public static void main (String[] args) throws Exception{

        if(args.length != 3){
            System.out.println("Incorrect number of arguments...input dimension, temperature and preferred system dynamics ('k' or 'g')\n");
        }
        
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[0]);
        //int cols = Integer.parseInt(args[1]);
        Temperature T = new Temperature(Double.parseDouble(args[1]));
        //T.setT(Double.parseDouble(args[1]));
        //int option = Integer.parseInt(args[2]);
        char option = args[2].charAt(0);
        //String option = args[2];
         
        
        //physical values: energy, boltzmann constant, temperature
        int J = 1;
        int K = 1;
        // double T = 0.5;
        
        
        MyMatrix A = new MyMatrix( rows, cols );
        Random rand = new Random();
        int element;
        //Random.nextInt(1);   //supposed to randomly make number between 0,1  (can subtract later to make 1, -1)
        for( int i=0; i<rows; i++ ) {
            for(  int j=0; j<cols; j++ ) {
                element = rand.nextInt(2); 
                if(element == 0){
                    element -= 1;
                }
                A.setElement(i,j, element ) ; //public int nextInt(int bound);
                
                //rand = Random.nextInt(1); 
            }
        }
        
        //A.printMatrix();
        
        //attempt to convert number matrix into visual coloured matrix made of pixels:
        // int[][] grid = new int[rows][cols];
        Visualization vis = new Visualization(rows,cols, T);
        visualize(vis, A, rows, cols);
        
        //////////
        // int testval = Integer.parseInt(args[2]);
        //double test = checkEnergy(J, A, testval, testval, false);
        //System.out.println(test);
        /////////////////
        
        
        // initial energy value
        System.out.printf("Original total energy is: %f\n", totEnergy(A, J));
        
        // initialise value of the average magnetisation of the system.
        double prevAveMag = 0;
        double currAveMag = 0;
        double averSuscept = 0;
        int i = 0;
        // while(true){ 
        //this while loop allows the user to exit it when return key is pressed
        while(System.in.available() == 0){    
            
            
            if(i%(rows*cols)== 0){visualize(vis, A, rows, cols);
            //System.out.println(T.getT());
            } //swap rows and columns??
            //visualize(vis, A, rows, cols);
            //Thread.sleep(1000); //number in milliseconds
            
            
            if(option == 'g'){ 
                glauber(A, K, J, T.getT());}
            if(option == 'k'){
                kawasaki(A, K, J, T.getT());}
            i++;
            
            //System.out.println(totMagnetisation(A));
            
            currAveMag = averMagnetisation(totMagnetisation(A), prevAveMag, i);
            prevAveMag = currAveMag;
            //System.out.println(averMagnetisation(totMagnetisation(A), prevAveMag, i));
            
            //System.out.println(susceptibility(totMagnetisation(A), currAveMag, i, K, T));
            averSuscept = ((averSuscept * (i-1)) + susceptibility(totMagnetisation(A), currAveMag, i, K, T.getT()) ) / i;
            
            //System.out.println(totEnergy(A, J));
            
            
        }
        
        System.out.printf("Average magnetisation value is: %f\n", averMagnetisation(totMagnetisation(A), prevAveMag, i));
        
        System.out.printf("Average susceptibility value is: %f\n", averSuscept);
        
        System.out.printf("Final total energy is: %f\n", totEnergy(A, J));
        
        /* Console con = System.console();
        String input = con.readLine("New lattice after one random flip:\n");
        //System.out.println(""); 
        A.printMatrix();
        
        visualize(vis, A, rows, cols);*/
        
    System.exit(0);
    }
    
}