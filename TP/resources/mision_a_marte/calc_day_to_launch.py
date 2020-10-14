from decimal import Decimal

def load_data(file_index):
    data = []
    with open('viajes/' + str(file_index) + '-day.tsv', 'r') as f:
        for line in f:
            d = line[:-1].split('    ')
            data.append(float(d[1]))
    return data

global_min = 1e20
file_idx = -1

for file in range(730):
    min_val = min(load_data(file))
    if min_val < global_min:
        global_min = min_val
        file_idx = file

print("File: ", file_idx)
print("Global Min Value: ", '%.2E' % Decimal(global_min))
