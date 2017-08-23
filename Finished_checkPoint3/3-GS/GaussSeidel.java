//import java.util.Random;
import java.awt.Color;
//import java.io.Console;
import java.io.*;
//import java.util.Scanner;


public class GaussSeidel{

    
    //visualise the system
    static void visualize(Visualization vis, MyMatrix3D grid, int rows, int cols){
        //height value
        int h = grid.height()/2;
        for (int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){

                
                // vis.set(c, r, (grid.getElement(r,c,h) >= 3 ) ? Color.red : ((grid.getElement(r,c,h) >= 2 ) ? Color.orange : ((grid.getElement(r,c,h) >= 1 ) ? Color.yellow : (grid.getElement(r,c,h) >= 0.0 ? Color.green : ((grid.getElement(r,c,h) >= -1 ? Color.blue : (grid.getElement(r,c,h) >= -2 ? Color.pink : Color.black))))))) ;   //if 0 it it S so susceptible so is orange, if 1 is I so is infected so red, if 2 is ,R, recovered, so is green.
                //    vis.set(c, r, (grid.getElement(r,c,h) >= 0.0 ) ? Color.red : Color.blue);
                //phi should be between -1 and 1?? so this is okay??
                
                //this might need some tweaking in order to allow larger charges to be used (so colours don't just cycle through as values increase)
                vis.set(c, r, Color.getHSBColor((float)(grid.getElement(r,c,h)), 1, 1));
            }
        }
        vis.draw();
    }

    
    //function to initialise values for rho array, the charge density of the system
    static void initialiseRho(MyMatrix3D rho, double rho_0){
        
        //double rho_0 = 0;
        double randNoise = 0;
        
        for(int i = 0; i<rho.rows(); i++){
            for(int j = 0; j<rho.cols(); j++){
                for(int k = 0; k<rho.height(); k++){
                    
                    //randNoise =(Math.random()); //should give value in range 0 -> 1 (or 2x10^-2)
                    //randNoise -= 0.5; //subtract 0.01 from value to give random value in range -0.01 -> 0.01
                    
                    //create center charge
                    if(i == rho.rows()/2 && j == rho.cols()/2 && k == rho.height()/2){
                        rho.setElement(i,j,k,  rho_0);    
                    }
                    
                    /*  if(i >= (rho.rows()/2)-3 && i <= (rho.rows()/2)+3 && j>= (rho.cols()/2)-3 && j <= (rho.cols()/2)+3 && k >= (rho.height()/2)-3 && k <= (rho.height()/2)+3){
                    rho.setElement(i,j,k,  rho_0);    
                }
                */
                    
                    /*  //create border charges
                else if(i == 0 || j == 0 || k == 0 || i%(rho.rows()-1) == 0 || j%(rho.cols()-1) == 0 || k%(rho.height()-1) == 0){
                    rho.setElement(i,j,k,  rho_0);    
                } */
                    else{
                        rho.setElement(i,j,k,  0);
                    }
                    
                    //System.out.printf("%f", sigma_0 + randNoise);
                }
            }
        }
    }
    
    
    
    //function to initialise values for sigma array
    static void initialisePhi(MyMatrix3D phi){
        
        double phi_0 = 0;
        double randNoise = 0;
        
        for(int i = 0; i<phi.rows(); i++){
            for(int j = 0; j<phi.cols(); j++){
                for(int k = 0; k<phi.height(); k++){
                    
                    //randNoise =(Math.random())/50; //should give value in range 0 -> 0.02 (or 2x10^-2)
                    //randNoise -= 0.01; //subtract 0.01 from value to give random value in range -0.01 -> 0.01
                    
                    phi.setElement(i,j,k,  0);
                    //System.out.printf("%f", sigma_0 + randNoise);
                }
            }
        }
    }
    
    
    
    //function to compute chemical potential (mu)
    static void updatePhi(MyMatrix3D phi, MyMatrix3D rho, double dx){
        
        /* double M = 0.1;
        double dx = 0.5;
        double dt = 0.1; */
        
        double i_plus, i_minus, j_plus, j_minus, k_plus, k_minus, rijk;
        double calcVal = 0;
        
        for(int i = 0; i<phi.rows(); i++){
            for(int j = 0; j<phi.cols(); j++){
                for(int k = 0; k<phi.height(); k++){
                
                //if the point is at a the boundary, set the potential equal to 0 here
                if(i == 0 || j == 0 || k == 0 || i%(rho.rows()-1) == 0 || j%(rho.cols()-1) == 0 || k%(rho.height()-1) == 0){
                    phi.setElement(i,j,k,  0);
                }
                
                else{
                //these contain the values of mu(i+1,j), mu(i-1,j), mu(i,j+1), mu(i,j-1), used to calculate new phi values
                i_plus = phi.getElement( (i+1)%phi.rows(),  j , k);
                i_minus = phi.getElement( (i-1+phi.rows())%phi.rows(),  j , k);
                j_plus = phi.getElement( i,  (j+1)%phi.rows() , k);
                j_minus = phi.getElement( i,  (j-1+phi.rows())%phi.rows(), k );
                k_plus = phi.getElement( i,  j , (k+1)%phi.rows());
                k_minus = phi.getElement( i,  j, (k-1+phi.rows())%phi.rows() );
                
                rijk = rho.getElement(i,j,k);
                
                phi.setElement(i,j,k, (1.0/6.0)* (i_plus+ i_minus + j_plus + j_minus + k_plus + k_minus  + dx*dx*rijk));
                }
                }
            }
        }
        
        
        
        
    }


    
    //method to calculate the error in the system
    static double calcError( MyMatrix3D phi_curr, MyMatrix3D phi_old ){
        
        double error = 0;
        
        for(int i = 0 ; i<phi_curr.rows(); i++){
            for(int j = 0 ; j<phi_curr.cols(); j++){
                for(int k = 0 ; k<phi_curr.height(); k++){
                    error += Math.abs(phi_curr.getElement(i,j,k) - phi_old.getElement(i,j,k));
                }
            }
        }
        return error;
    }
    
    
    //method to calculate the error in the system
    static void updateE( MyMatrix3D phi, MyMatrix3D Ex, MyMatrix3D Ey, MyMatrix3D Ez, double dx){
        
        double i_plus, i_minus, j_plus, j_minus, k_plus, k_minus, rijk;
        double calcValx = 0;
        double calcValy = 0;
        double calcValz = 0;
        double magnitude = 0;
        
        for(int i = 0 ; i<phi.rows(); i++){
            for(int j = 0 ; j<phi.cols(); j++){
                for(int k = 0 ; k<phi.height(); k++){
                    
                    //if at edge of 3d cube, set E to 0 due to boundary conditions
                    if(i == 0 || j == 0 || k == 0 || i%(phi.rows()-1) == 0 || j%(phi.cols()-1) == 0 || k%(phi.height()-1) == 0){
                        Ex.setElement(i,j,k, 0);
                        Ey.setElement(i,j,k, 0);
                        Ez.setElement(i,j,k, 0);
                    }
                    
                    else{
                        //calculate E = -nabla(phi), for x, y, z components
                        i_plus = phi.getElement( (i+1),  j , k);
                        i_minus = phi.getElement( (i-1),  j , k);
                        calcValx =  (i_plus - i_minus)/(-2*dx);
                        
                        j_plus = phi.getElement( i,  (j+1) , k);
                        j_minus = phi.getElement( i,  (j-1), k );
                        calcValy =  (j_plus - j_minus)/(-2*dx);

                        k_plus = phi.getElement( i,  j , (k+1));
                        k_minus = phi.getElement( i,  j, (k-1) );     
                        calcValz =  (k_plus - k_minus)/(-2*dx); 
                        //normalise values
                        magnitude = Math.pow((calcValx*calcValx + calcValy*calcValy + calcValz*calcValz),0.5);
                       // System.out.printf("%f \n", magnitude);
                        Ex.setElement(i,j,k, calcValx/magnitude); 
                        Ey.setElement(i,j,k, calcValy/magnitude);
                        Ez.setElement(i,j,k, calcValz/magnitude);
                    }
                }
            }
        }
        
        
    }
    
    
    /////////////
    //print all E field components and their associated positions to file in format, (i,j,k,Ex,Ey,Ez)
    static void  toFileE(MyMatrix3D Ex, MyMatrix3D Ey, MyMatrix3D Ez) throws Exception{

        String fname = "E_field_data_GS.dat";
        PrintWriter output = new PrintWriter(new FileWriter(fname));
        
        String fname2 = "E_field_vs_centre_GS.dat";
        PrintWriter output2 = new PrintWriter(new FileWriter(fname2));
        
        double magnitude = 0;
        
        for(int i = 0 ; i<Ex.rows(); i++){
            for(int j = 0 ; j<Ex.cols(); j++){
                for(int k = 0 ; k<Ex.height(); k++){
                    magnitude = Math.pow((Ex.getElement(i,j,k)*Ex.getElement(i,j,k) + Ey.getElement(i,j,k)*Ey.getElement(i,j,k) + Ez.getElement(i,j,k)*Ez.getElement(i,j,k)),0.5);
                    output.printf("%d %d %d %f %f %f\n", i, j, k, Ex.getElement(i,j,k), Ey.getElement(i,j,k), Ez.getElement(i,j,k));
                    //System.out.printf("%f\n", magnitude );
                    
                    /////////////////////////
                    
                    if(i>=Ez.rows()/2 && j == Ez.cols()/2 && k == Ez.height()/2){
                        //output the x component of the e field vs distance from centre to a file
                        output2.printf("%d %f\n", i-Ez.rows()/2, Ez.getElement(i,j,k));
                    }
                    
        
                    
                    
                    /////////////////////
                }
            }
        }
        
        output.close();
        output2.close();
        
    }
    
    
    
    
    
    public static void main(String[] args)  throws Exception{

        
        if(args.length != 4){
            System.out.println("Incorrect number of arguments...\n1) input dimension\n2) input rho_0\n3) value for dx \n4) input error tolerance \n");
            System.exit(0);
        }
        int rows = Integer.parseInt(args[0]);
        int cols = Integer.parseInt(args[0]);
        int height  = Integer.parseInt(args[0]);
        
        double rho_0 = Double.parseDouble(args[1]);
        
        double dx = Double.parseDouble(args[2]);
        double tolerance = Double.parseDouble(args[3]);
        
        double error = 0;
        
        //make array for phi
        MyMatrix3D phi = new MyMatrix3D( rows, cols , height);
        MyMatrix3D phi_old = new MyMatrix3D( rows, cols , height);
        
        //make 3 arrays for each component of the electric field
        MyMatrix3D Ex = new MyMatrix3D( rows, cols , height);
        MyMatrix3D Ey = new MyMatrix3D( rows, cols , height);
        MyMatrix3D Ez = new MyMatrix3D( rows, cols , height);
        
        //make array for phi
        MyMatrix3D rho = new MyMatrix3D( rows, cols , height);
        initialisePhi(phi);
        //copy original values of phi to another array
        phi_old = phi.copy();
        initialiseRho(rho, rho_0);
        
        //initialise / calculate first values for E field
        updateE( phi, Ex, Ey, Ez, dx);
        
        
        //phi.printMatrixCenterSlice();
        //updatePhi(phi, rho, dx);
        
        // phi.printMatrix3D();
        
        
      //  Visualization vis = new Visualization(rows,cols);
       // visualize(vis, phi, rows, cols);
        
        /* Visualization vis2 = new Visualization(rows,cols);
        visualize(vis2, Ex, rows, cols);
        
        Visualization vis3 = new Visualization(rows,cols);
        visualize(vis2, Ey, rows, cols);
        
        Visualization vis4 = new Visualization(rows,cols);
        visualize(vis2, Ez, rows, cols); */
        
        //visualize(vis, rho, rows, cols);   //visualise the charge distribution
        
        int count = 0;
        
        while(System.in.available() == 0){ 
            
            if(count%10 == 0){
                //visualize(vis, phi, rows, cols);
                
                
                //visualize(vis2, Ex, rows, cols);
                
                
                //visualize(vis2, Ey, rows, cols);
                
                
                // visualize(vis2, Ez, rows, cols);
                
                
                //visualize(vis, rho, rows, cols);                 //visualise the charge distribution
                
            }
            updatePhi(phi, rho, dx);

            //update values for E field
            updateE( phi, Ex, Ey, Ez, dx);
            
            //phi has now been updated so calculate the error in values using phi_old
            error = calcError(phi, phi_old);
            
           // System.out.printf("%f\n", error);
            
            if(error<= tolerance){
                break;
            }
            
            //copy new phi values to phi_old
            phi_old = phi.copy();
            
            count++;
        }
        
        //phi.printMatrixCenterSlice();
        //Ex.printMatrixCenterSlice();
        //Ey.printMatrixCenterSlice();
        //Ez.printMatrixCenterSlice();
        
        
        //print all E field components and their associated positions to file in format, (i,j,k,Ex,Ey,Ez)
        toFileE(Ex,Ey,Ez);

        //write data from the centre slice to file so (3D) contour plots can be made
        String fname = "Potential_data_GS.dat";
        PrintWriter output = new PrintWriter(new FileWriter(fname));
        phi.printMatrixCenterSlice(output);
        output.close();
        
        int mid_y = phi.cols()/2;
        int mid_z = phi.height()/2;
        //write data from the centre slice to file so (3D) contour plots can be made
        String fname2 = "Potential_vs_centre_GS.dat";
        PrintWriter output2 = new PrintWriter(new FileWriter(fname2));
         for(int i = phi.rows()/2; i<phi.rows(); i++){
            
                    output2.printf("%d %f\n", i-phi.rows()/2, phi.getElement(i,mid_y,mid_z));
                    
                 }
        
        output2.close();
        
        
        //rho.printMatrixCenterSlice();
        //rho.printMatrix3D();
        
        //System.out.printf("%f\n", phi.getElement(rows/2,cols/2,height/2));
        //System.out.printf("%f\n", phi.getElement(2,2,height/2));
        
        System.exit(0);
    }
}