javac *.java
java Poisson 51 1 1 0.01
python PlotContour.py Potential_data_P.dat
python PlotContour3D.py Potential_data_P.dat
gnuplot plot.txt
pause