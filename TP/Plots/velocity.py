import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import matplotlib.ticker as mticker
import math

eje_x = np.arange(0, 365, 1)
filename="../resources/mision_a_marte/vec_velocidades.tsv"
my_csv = pd.read_csv(filename,sep='    ',header=None,usecols=[0,1], names=['vx', 'vy'])
vx = my_csv.vx
vy=my_csv.vy

vx2=np.power(vx,2)
vy2=np.power(vy,2)
suma=vx2+vy2
v=np.sqrt(suma)
plt.ylabel(r'Velocidad $[\frac{m}{s}]$')
plt.xlabel('Dias en el año')
plt.plot(eje_x,v)
plt.show()
