from decimal import Decimal

registros = []

global_min = 1e20
file_idx = -1
global_idx = -1

def load_data(file_index):
    times = []
    data = []
    with open('viajes/' + str(file_index) + '-day.tsv', 'r') as f:
        for line in f:
            d = line[:-1].split('    ')
            times.append(float(d[0]))
            data.append(float(d[1]))
    return times, data

def find_local_minimum(data):
    min_val = data[0]
    min_idx = 0
    for i in range(len(data)):
        if data[i] < min_val:
            min_val = data[i]
            min_idx = i
    return min_idx, data[min_idx]

#agarramos el archivo de distancias para el lanzamiento del día 276
times, data = load_data(276)
#agarramos el valor donde está el mínimo
min_idx, min_val = find_local_minimum(data)
#agarramos la velocidad que tenía en ese índice
#cargar el archivo de velocidades
data = []
with open('velocity.tsv', 'r') as f:
    for line in f:
        d = line[:-1].split('    ')
        data.append(float(d[0]))
vel = []
with open('vec_velocidades.tsv', 'r') as f:
    for line in f:
        d = line[:-1].split('    ')
        v  = []
        v.append(float(d[0]))
        v.append(float(d[1]))
        vel.append(v)

#agarramos el valor en el índice que habíamos leído anteriormente
print('Tiempo hasta llegar a marte (desde el despegue): %s dias' % min_idx)
print('Velocidad cuando llega a marte:', '%.2E' % Decimal(data[min_idx]))
print('Vector velocidad al llegar a marte:', '%.2E' % Decimal(vel[min_idx][0]), '%.2E' % Decimal(vel[min_idx][1]))
