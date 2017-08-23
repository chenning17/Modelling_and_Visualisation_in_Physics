This is all of my work for checkpoint 3 of the MVP course.

The different parts of the checkpoint have been split into different folders labelled:
    
    '1-Cahn-Hilliard'      -> contains the CH simulation of oil droplets and water, and 
                              plots of free energy for phi_0 values of 0 and 0.5 
    
    '2-Poisson'            -> contains the Jacobi solution to the poisson equation, and 
                              plots of potential field (3D, contour, and versus distance 
                              from centre), and electric field vector plots 
    
    '3-GS'                 -> contains the Gauss Seidel solution to the poisson equation, 
                              and plots of potential field (3D, contour, and versus distance 
                              from centre), and electric field vector plots
    
    '4-SOR'                -> contains the SOR solution to the poisson equation, and plot 
                              of number of steps required vs omega (for a system of dimension 
                              50 this was found to be 1.88), and also the SOR method acting on 
                              a wire and a plot of the resulting magnetic and electric fields.
    
    'required_plots'       -> contains all of the above plots in the one folder
    
In each of the section folders is a file named 'run.cmd' which on a windows pc you can just 
double click and it will run all the code required to produce the above plots. If not, you 
can open it with a text editor then copy the commands over to run the programs.


List of all programs, and how to run them:

[folder]            | [program name] |  [example arguments // what each value represents]          |   [purpose]
_________________________________________________________________________________________________________________________________________________________________
                    |                |                                                             |
'1-Cahn-Hilliard'   |  CH            |   50 0 0.1 0.1 1 1  // dimension, phi_0, a, kappa, dx, dt   |   runs simulation of oil and water 
                    |                |                                                             |   droplets with visualisation
                    |                |                                                             |
'1-Cahn-Hilliard'   |  CH_energy     |   20 0 0.1 0.1 1 1  // dimension, phi_0, a, kappa, dx, dt   |   runs simulation of oil and water 
                    |                |                                                             |   droplets in order to calculate values   
                    |                |                                                             |   for the free energy
                    |                |                                                             |   
'2-Poisson'         |  Poisson       |   51 1 1 0.01    // dimension, charge, dx, tolerance value  |   jacobi method to solve poisson equation for 
                    |                |                                                             |   a point charge. creates files that can be used 
                    |                |                                                             |   to make plots of the potential etc.
                    |                |                                                             |   
'3-GS'              |  GaussSeidel   |    51 1 1 0.01   // dimension, charge, dx, tolerance value  |   gauss seidel method to solve poisson equation 
                    |                |                                                             |   for a point charge. creates files that can be 
                    |                |                                                             |   used to make plots of the potential etc.
                    |                |                                                             |   
'4-SOR'             |  SOR           |   50 1 1 1 0.1   // dimension, charge, dx,                  |   SOR method to solve poisson equation for a point
                    |                |                     intial omega value, tolerance value     |   charge, alters the initial value of omega incrementally 
                    |                |                                                             |   by 0.01 until it reaches a value of 2. Outputs the data
                    |                |                                                             |   to a text file that can be used to plot number of steps required
                    |                |                                                             |   versus value of omega such that it is possible to see the best 
                    |                |                                                             |   value of omega to use in future runs.
                    |                |                                                             |   
'4-SOR'             |  SORMagnetic   |   50 1 1 1.88 0.01 // dimension, charge, dx, omega,         |   SOR method to solve poisson equation for a wire 
                    |                |                       error tolerance value                 |   and to calculate the electric and magnetic field
                    |                |                                                             |   around it. These values are written to a file that can then 
                    |                |                                                             |   be plotted using gnuplot to make vector plots

('2-Poisson' and '3-GS' contain python programs 'PlotContour.py' and 'PlotContour3D.py' that can be used to plot the flat contour plot and the 
 3D surface contour plot of the potential)
 
 There are also files named 'plot.txt', or 'gnuplot.txt' which when used as arguments to gnuplot create the plots seen.
 