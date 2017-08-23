import sys
import numpy as np 
import pylab as pl
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm
from os.path import basename
import os


#load decay length data from text file
data = np.loadtxt(sys.argv[1])

fig = pl.figure()
ax = fig.add_subplot(111, projection = '3d')
X = np.arange(0, 1, 0.01)
Y = np.arange(0, 1, 0.01)
X, Y = np.mgrid[0:len(data):1, 0:len(data):1]
# plot a contour plot
cp = ax.plot_surface(X, Y, data[:], cmap = cm.jet, linewidth = 0, cstride = 1, rstride = 1)

#cp = axes[0].contourf(X, Y, FR)
#pl.colorbar(c1, ax=axes[0]);

fig.colorbar(cp, shrink=0.5, aspect=5)
# add labels to axis, and add title
pl.xlabel('x coordinate')
pl.ylabel('y coordinate')

#pl.xlim(0, 10)
#pl.ylim(0, 10)
pl.title('3D contour plot of ' +os.path.splitext(basename(sys.argv[1]))[0])

#os.path.splitext(basename(sys.argv[1]))[0]     <- should give file name without extension

# now you can call it directly with basename
pl.savefig(os.path.splitext(basename(sys.argv[1]))[0] + '_3D_contour_plot.png')
#pl.show()


