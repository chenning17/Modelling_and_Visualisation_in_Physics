javac *.java
java GaussSeidel 51 1 1 0.01
python PlotContour.py Potential_data_GS.dat
python PlotContour3D.py Potential_data_GS.dat
gnuplot plot.txt
pause