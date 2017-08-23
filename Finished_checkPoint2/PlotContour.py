import sys
import numpy as np 
import pylab as pl
import os
from os.path import basename


#load decay length data from text file
data = np.loadtxt(sys.argv[1])

X, Y = np.mgrid[0:1:20j, 0:1:20j]
# plot a histogram of the data into 50 bins
cp = pl.contourf(X, Y, data[:])

#cp = axes[0].contourf(X, Y, FR)
#pl.colorbar(c1, ax=axes[0]);

pl.colorbar(cp)
# add labels to axis, and add title
pl.xlabel('p1')
pl.ylabel('p3')
#pl.xlim(0, 10)
#pl.ylim(0, 10)
pl.title('contour plot of ' + os.path.splitext(basename(sys.argv[1]))[0]) #puts file name excluding extension into plot title
pl.savefig(os.path.splitext(basename(sys.argv[1]))[0] + '_2D_contour_plot.png')
pl.show()
