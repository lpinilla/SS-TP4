times = []
data = []

with open('distances', 'r') as f:
    for line in f:
        d = line[:-1].split('    ')
        times.append(d[0])
        data.append(d[1])

min_val = data[0]
min_idx = 0

for i in range(len(data)):
    if data[i] < min_val:
        min_val = data[i]
        min_idx = i

print(times[min_idx], data[min_idx])
