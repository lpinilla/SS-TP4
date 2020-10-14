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

for file in range(0, 1):
    #cargar los datos
    times, data = load_data(file)
    #calcular el mínimo de ese archivo
    min_idx, minimum = find_local_minimum(data)
    #comparar con el mínimo de todos los archivos
    #print(times[min_idx], data[min_idx])
    if minimum < global_min:
        global_min = minimum
        file_idx = file
        global_idx = min_idx

print("File: ", file_idx)
print("Global Min Value: ", '%.2E' % Decimal(global_min))
print("Global Min idx: ", global_idx)
