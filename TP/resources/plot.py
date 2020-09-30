import matplotlib.pyplot as plt
from matplotlib.ticker import (AutoMinorLocator, MultipleLocator)

x = []
y = []
vx = []
vy = []
#particle_id = 15
#radius = 1.0
#M = 13
L = 6

with open('RandomDynamicInput.txt') as f:
    next(f)
    for line in f:
        data = line[0:-1].split('\t')
        x.append(float(data[0]))
        #y.append(float(data[1]))
f.close()

fig, ax = plt.subplots()
#plt.plot(x[0], y[0], '.', color='red', markersize=165)
#plt.plot(x[1:], y[1:], '.', color='blue', markersize=45)
#plt.plot(x[5], y[5], '.', color='yellow', markersize=7, label='Selected')

#Círculos
#radius = 0.7
#cir = plt.Circle((x[0], y[0]), radius, color='green', fill=False)
#plt.gcf().gca().add_artist(cir)

#for i in range(1, len(x)):
#    radius = 0.2
#    cir = plt.Circle((x[i], y[i]), radius, color='green', fill=False)
#    plt.gcf().gca().add_artist(cir)


#circleCut = 5 - y[particle_id]
#cir = plt.Circle((x[particle_id], -circleCut), radius, color='green', fill=False)
#plt.gcf().gca().add_artist(cir)
plt.xlabel('t')
plt.ylabel('x')
plt.title('testing')
plt.legend()

#cambiando el tamaño de la grilla la grilla
ax.set_xlim(0, L)
ax.set_ylim(0, L)
ax.grid(linestyle='-', linewidth='0.5')
#ax.xaxis.set_major_locator(MultipleLocator(particle_id))
#ax.yaxis.set_major_locator(MultipleLocator(particle_id))
plt.show()
#plt.savefig('vecinos_de_0.png')
