import sys
import numpy as np 
import pylab as pl
import numpy.random as rand  # Random number generation module
from os.path import basename
import os

#load decay length data from text file
#x, y = np.loadtxt(sys.argv[1], unpack = True)

data = np.loadtxt(sys.argv[1], unpack = True)


#x = range(len(data))

# plot a fit to the normalised data
#pl.scatter(x, data, c='blue', label = 'waves')
#pl.plot(x, y, c='blue', label = 'waves')

pl.plot(data, c='blue', label = 'waves')

# label the axis and add a title to the plot
pl.xlabel('MC sweeps')
#pl.ylabel('fraction infected [I/N]')
pl.ylabel('number infected [I/N]')
#pl.title('fraction infected vs. no. of MC sweeps')
pl.title('number infected vs. no. of MC sweeps')
pl.legend()

pl.savefig(os.path.splitext(basename(sys.argv[1]))[0]+'.png')
pl.show()


