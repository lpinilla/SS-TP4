import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import matplotlib.ticker as mticker


filename="../resources/mision_a_marte/min_distance_per_day.tsv"
my_csv = pd.read_csv(filename,sep='    ',header=None,usecols=[0,1], names=['dia', 'dist'])
dates = my_csv.dia
distance=my_csv.dist

# plt.yscale('log')
f = mticker.ScalarFormatter(useOffset=False, useMathText=True)
g = lambda x,pos : "${}$".format(f._formatSciNotation('%1.10e' % x))
plt.gca().yaxis.set_major_formatter(mticker.FuncFormatter(g))
plt.ylabel('Distancia a Marte [m]')
plt.xlabel('Dias en el año 2020')
plt.plot(dates,distance)
plt.show()