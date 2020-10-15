import matplotlib.pyplot as plt
import matplotlib.pyplot as error_curve

import numpy as np
import pandas as pd
import seaborn as sns;
from sklearn.metrics import mean_squared_error



# si no tengo 10 archivos, no anda el for j
cant_files = 6
# eje_x = np.arange(1,6,1)
eje_x=[0.0000001,0.000001,0.00001,0.0001,0.001]
print (eje_x)
rta_beeman = []
rta_gear= []
rta_meuler= []
col_list = ["x", "y"]

for i in range(1, cant_files):
    with open("error_files/analytic_1" + str(i) + ".tsv") as analy, open(
            "error_files/beeman_1" + str(i) + ".tsv") as beeman, open(
            "error_files/gearPredictorCorrector_1" + str(i) + ".tsv") as gear, open(
            "error_files/euler_1" + str(i) + ".tsv") as meuler:

        # read_gear = pd.read_csv(analy,sep='    ',header=None,usecols=[0, 1],names=col_list)
        aux_analy = pd.read_csv(analy,sep='    ',usecols=col_list)
        aux_analy_x=aux_analy["x"].to_numpy()
        aux_beeman = pd.read_csv(beeman,sep='    ',usecols=col_list)
        aux_beeman_x=aux_beeman["x"].to_numpy()
        aux_gear = pd.read_csv(gear,sep='    ',usecols=col_list)
        aux_gear_x=aux_gear["x"].to_numpy()
        aux_meuler = pd.read_csv(meuler,sep='    ',usecols=col_list)
        aux_meuler_x=aux_meuler["x"].to_numpy()

        rta_beeman.append(mean_squared_error(aux_analy_x,aux_beeman_x))
        rta_gear.append(mean_squared_error(aux_analy_x,aux_gear_x))
        rta_meuler.append(mean_squared_error(aux_analy_x,aux_meuler_x))
        # aux1=aux_analy_x-aux_beeman_x
        # rta_beeman.append(np.sum(np.power(aux1, 2)))
        # aux1=aux_analy_x-aux_gear_x
        # rta_gear.append(np.sum(np.power(aux1, 2)))
        # aux1=aux_analy_x-aux_meuler_x
        # rta_meuler.append(np.sum(np.power(aux1, 2)))

# print(rta_beeman,rta_gear,rta_meuler)


# plt.title(r'Pendiente $[\frac{m^2}{s}]$')
plt.ylabel('Error cuadratico medio [$m^{2}$]')
plt.xlabel('Delta tiempo [s] ')
plt.yscale('log')
plt.xscale('log')
plt.plot(eje_x,rta_beeman,'ob')
plt.plot(eje_x,rta_gear,'or')
plt.plot(eje_x,rta_meuler,'og')
plt.legend(['Beeman','Gear','Modified Euler'])

plt.show()
#
