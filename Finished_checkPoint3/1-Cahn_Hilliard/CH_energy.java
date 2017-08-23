//import java.util.Random;
import java.awt.Color;
//import java.io.Console;
import java.io.*;
import java.util.Scanner;


public class CH_energy{

    
    //visualise the system
    static void visualize(Visualization vis, MyMatrix grid, int rows, int cols){
        for (int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                
                //vis.set(c, r, (grid.getElement(r,c) >= 0.03 ) ? Color.red : ((grid.getElement(r,c) >= 0.02 ) ? Color.orange : ((grid.getElement(r,c) >= 0.01 ) ? Color.yellow : (grid.getElement(r,c) >= 0.0 ? Color.green : ((grid.getElement(r,c) >= -0.01 ? Color.blue : (grid.getElement(r,c) >= -0.02 ? Color.pink : Color.black))))))) ;   //if 0 it it S so susceptible so is orange, if 1 is I so is infected so red, if 2 is ,R, recovered, so is green.
                
                //phi should be between -1 and 1?? so this is okay??
                vis.set(c, r, Color.getHSBColor((float)(grid.getElement(r,c) + 1)/3, 1, 1));
            }
        }
        vis.draw();
    }


    //function to initialise values for sigma array
    static void initialiseSigma(MyMatrix sigma, double sigma_0){
        
        //double sigma_0 = 0;
        double randNoise = 0;
        
        for(int i = 0; i<sigma.rows(); i++){
            for(int j = 0; j<sigma.cols(); j++){
                
                randNoise =(Math.random())/50; //should give value in range 0 -> 0.02 (or 2x10^-2)
                randNoise -= 0.01; //subtract 0.01 from value to give random value in range -0.01 -> 0.01
                
                sigma.setElement(i,j,  sigma_0 + randNoise);
                //System.out.printf("%f", sigma_0 + randNoise);
            }
        }
    }
    
    
    //function to compute chemical potential (mu)
    static void updateMu(MyMatrix mu, MyMatrix sigma, double a, double kappa, double dx){
        
        //double a = 0.3;  //higher number = faster speed
        double b = a;
        //double kappa = 0.1;   //determines particle size / speed of simulation (smaller number = quicker)
        //double dx = 0.5;
        double i_plus, i_minus, j_plus, j_minus, ij;
        double calcVal = 0;
        
        for(int i = 0; i<mu.rows(); i++){
            for(int j = 0; j<mu.cols(); j++){
                
                //these contain the values of sigma(i+1,j), sigma(i-1,j), sigma(i,j+1), sigma(i,j-1), used to calculate mu values
                i_plus = sigma.getElement( (i+1)%sigma.rows(),  j );
                i_minus = sigma.getElement( (i-1+sigma.rows())%sigma.rows(),  j );
                j_plus = sigma.getElement( i,  (j+1)%sigma.rows() );
                j_minus = sigma.getElement( i,  (j-1+sigma.rows())%sigma.rows() );
                
                ij = sigma.getElement( i,  j ); //get value for sigma(i,j)
                
                //calculate the value for mu using the values for sigma
                calcVal = -a*ij + b*(Math.pow(ij, 3.0)) -  (kappa/(Math.pow(dx,2)))*( i_plus + i_minus + j_plus + j_minus -(4.0*ij)    ) ;
                
                mu.setElement(i,j,  calcVal);
            }
        }
    }
    
    
    
    //function to compute chemical potential (mu)
    static void updateSigma(MyMatrix mu, MyMatrix sigma,  double M, double dx, double dt){
        
        /* double M = 0.1;
        double dx = 0.5;
        double dt = 0.1; */
        
        double[][] temp = new double[sigma.rows()][sigma.cols()];
        
        double i_plus, i_minus, j_plus, j_minus, ij;
        double calcVal = 0;
        
        for(int i = 0; i<sigma.rows(); i++){
            for(int j = 0; j<sigma.cols(); j++){
                
                //these contain the values of mu(i+1,j), mu(i-1,j), mu(i,j+1), mu(i,j-1), used to calculate new sigma values
                i_plus = mu.getElement( (i+1)%mu.rows(),  j );
                i_minus = mu.getElement( (i-1+mu.rows())%mu.rows(),  j );
                j_plus = mu.getElement( i,  (j+1)%mu.rows() );
                j_minus = mu.getElement( i,  (j-1+mu.rows())%mu.rows() );
                
                ij = mu.getElement( i,  j ); //get value for mu(i,j)
                
                //calculate the value for mu using the values for sigma
                calcVal =  sigma.getElement( i,  j ) +      ( (M*dt)/(Math.pow(dx,2.0) ) )  *   ( i_plus + i_minus + j_plus + j_minus -(4.0*ij)    )    ;
                
                //copy to temporary matrix for copying later
                temp[i][j] = calcVal;
                
            }
        }
        
        
        for(int i = 0; i<sigma.rows(); i++){
            for(int j = 0; j<sigma.cols(); j++){
                
                //update sigma now all values have been calculated
                sigma.setElement(i,j,  temp[i][j]);
            }
        }
    }
    
    //method to calculate the free energy of the system.
    static double updateFreeEnergy( MyMatrix sigma, double a, double kappa, double dx ){
        
        double firstTerm, secondTerm, thirdTerm;
        //variables to calculate the values of sigma above and below current matrix element
        double j_plus, j_minus;
        
        double freeEnergy = 0;
        for(int i = 0 ; i<sigma.rows(); i++){
            for(int j = 0 ; j<sigma.cols(); j++){
                j_plus = sigma.getElement( i,  (j+1)%sigma.rows() );
                j_minus = sigma.getElement( i,  (j-1+sigma.rows())%sigma.rows() );
                firstTerm = -1*(a/2) * (Math.pow(sigma.getElement(i,j),2));
                secondTerm = (a/4)* (Math.pow(sigma.getElement(i,j),4));
                thirdTerm = (kappa/2) * ((j_plus-j_minus)/(2*dx));
                freeEnergy += firstTerm+secondTerm+thirdTerm;
            }
        }
        return freeEnergy/(sigma.rows()*sigma.cols());
    }
    
    
    
    

    public static void main(String[] args) throws Exception{
        if(args.length != 6){
            System.out.println("Incorrect number of arguments...\n1) input dimension\n2)input phi_0\n3) a\n4) kappa\n5) dx\n6) dt\n ");
            System.exit(0);
        }
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[0]);
        
        double sigma_0 = Double.parseDouble(args[1]);

        //System.out.print("input 'a' value: ");
        double a = Double.parseDouble(args[2]);
        //double sigma_0 = 0; 
        // double a = 0.1;
        //System.out.print("input kappa value: ");
        double kappa = Double.parseDouble(args[3]);
        //double kappa = 0.1;
        // System.out.print("input dx value: ");
        double dx = Double.parseDouble(args[4]);       
        //double dx = 0.5;
        //System.out.print("input dt value: ");
        double dt = Double.parseDouble(args[5]);  
        //double dt = 0.1; 
        
        /* if(args.length != 1){
            System.out.println("Incorrect number of arguments...\n1) input dimension\n ");
            System.exit(0);
        }
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[0]);
        
        // create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);

        //  prompt for sigma_0 value
        System.out.print("input sigma_0, a, kappa, dx, dt: ");
        double sigma_0 = Double.parseDouble(scanner.next());

        //System.out.print("input 'a' value: ");
        double a = Double.parseDouble(scanner.next());
        //double sigma_0 = 0; 
        // double a = 0.1;
        //System.out.print("input kappa value: ");
        double kappa = Double.parseDouble(scanner.next());
        //double kappa = 0.1;
        // System.out.print("input dx value: ");
        double dx = Double.parseDouble(scanner.next());       
        //double dx = 0.5;
        //System.out.print("input dt value: ");
        double dt = Double.parseDouble(scanner.next());  
        //double dt = 0.1; */
        

        /*  System.out.print("input M value: ");
    double M = Double.parseDouble(scanner.next());  */ 
        double M = a;

        
        
        
        //make array for sigma
        MyMatrix sigma = new MyMatrix( rows, cols );

        initialiseSigma(sigma, sigma_0);
        
        
        //Visualization vis = new Visualization(rows,cols);
        //visualize(vis, sigma, rows, cols);
        
        //make array for mu
        MyMatrix mu = new MyMatrix( rows, cols );
        updateMu(mu, sigma, a, kappa, dx);
        
        //make free energy
        double freeEnergy  = 0;

        freeEnergy = updateFreeEnergy(sigma, a, kappa, dx );
        
        int count = 0;
        
        String fname = Double.toString(sigma_0) +"_Free_energy_data.dat";
        PrintWriter output = new PrintWriter(new FileWriter(fname));
        
        while(count < 100000){ 
            
            
            
            
            /* if(count%100 == 0){
                output.printf("%f \n", freeEnergy);
                visualize(vis, sigma, rows, cols);      
            } */
            /*  System.out.printf("%f ", sigma.getElement(1,1));
        System.out.printf("%f ", sigma.getElement(40,26));
        System.out.printf("%f \n", sigma.getElement(10,19));
        } */
            updateSigma(mu,sigma, M, dx, dt);

            updateMu(mu, sigma, a, kappa, dx);
            
            freeEnergy = updateFreeEnergy(sigma, a, kappa, dx );
            if(count%100 == 0){
                freeEnergy = updateFreeEnergy(sigma, a, kappa, dx );
                output.printf("%f \n", freeEnergy);
               }
            //output.printf("%f \n", freeEnergy);
            count++;
        }
        
        /*  for(int i = 0; i<mu.rows(); i++){
            for(int j = 0; j<mu.cols(); j++){
        System.out.printf("%f ", sigma.getElement(i,j));

        }
        System.out.printf("\n"); 
        } */
        
        
        output.close();
        System.exit(0);
    }
}