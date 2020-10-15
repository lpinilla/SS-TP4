import matplotlib.pyplot as plt
import matplotlib.pyplot as err_plt

import numpy as np
import pandas as pd

# cant files
# cant_files = 2
# cant lineas en los files
cant = 2500
empty_array = np.empty((cant, 0), int)
eje_x = np.arange(0, 5, 0.002)
col_list = ["x", "y"]

gear_numpy_x = []
# analizo Gear
# for i in range(0, cant_files):
#     with open("../resources/gearPredictorCorrector_" + str(i) + ".tsv") as data:
#         read_gear = pd.read_csv(data,sep='    ', usecols=col_list)
#         gear_numpy_x.append(read_gear["x"].to_numpy())

read_gear = pd.read_csv("error_files/gearPredictorCorrector_5.tsv", sep='    ', usecols=col_list)
gear_numpy_x.append(read_gear["x"].to_numpy())
# get Mean of file
gear_arr = np.array(gear_numpy_x).astype(np.float)
gear_mean = np.nanmean(gear_arr, axis=0)
# get error of file
gear_err = np.nanstd(gear_arr, axis=0)
plt.plot(eje_x, gear_mean, '--b')

# analizo Beeman
beeman_numpy_x = []
read_beeman = pd.read_csv("error_files/beeman_5.tsv", sep='    ', usecols=col_list)
beeman_numpy_x.append(read_beeman["x"].to_numpy())
# for i in range(0, cant_files):
#     with open("../resources/beeman_" + str(i) + ".tsv") as data:
#         read_beeman = pd.read_csv(data,sep='    ', usecols=col_list)
#         beeman_numpy_x.append(read_beeman["x"].to_numpy())

# get Mean of file
beeman_arr = np.array(beeman_numpy_x).astype(np.float)
beeman_mean = np.nanmean(beeman_arr, axis=0)
# get error of file
beeman_err = np.nanstd(beeman_arr, axis=0)
plt.plot(eje_x, beeman_mean, '--r')

# analizo Euler
euler_numpy_x = []
read_euler = pd.read_csv("error_files/euler_5.tsv", sep='    ', usecols=col_list)
euler_numpy_x.append(read_euler["x"].to_numpy())
# for i in range(0, cant_files):
#     with open("../resources/modifiedEuler_" + str(i) + ".tsv") as data:
#         read_euler = pd.read_csv(data,sep='    ', usecols=col_list)
#         euler_numpy_x.append(read_euler["x"].to_numpy())

# get Mean of file
euler_arr = np.array(euler_numpy_x).astype(np.float)
euler_mean = np.nanmean(euler_arr, axis=0)
# get error of file
euler_err = np.nanstd(euler_arr, axis=0)
plt.plot(eje_x, euler_mean, '--g')

# analizo Analitica
analytic_numpy_x = []
read_analytic = pd.read_csv("error_files/analytic_5.tsv", sep='    ', usecols=col_list)
analytic_numpy_x.append(read_analytic["x"].to_numpy())
# for i in range(0, cant_files):
#     with open("../resources/analytic_" + str(i) + ".tsv") as data:
#         read_analytic = pd.read_csv(data,sep='    ', usecols=col_list)
#         analytic_numpy_x.append(read_analytic["x"].to_numpy())

# get Mean of file
analytic_arr = np.array(analytic_numpy_x).astype(np.float)
analytic_mean = np.nanmean(analytic_arr, axis=0)
# get error of file
analytic_err = np.nanstd(analytic_arr, axis=0)
plt.plot(eje_x, analytic_mean , color='m')

plt.xlabel('Tiempo [s]')
plt.ylabel('Posicion [m]')
plt.legend(['Gear', 'Beeman', 'Euler', 'Analitica'])
plt.show()


