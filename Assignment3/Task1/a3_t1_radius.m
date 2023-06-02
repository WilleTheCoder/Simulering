
data1 = load('stra2_radius.txt');
x1 = data1(:, 1); 
y1 = data1(:, 2);

plot(x1, y1, 'r', 'LineWidth', 2);


xlabel('Radius');
ylabel('Throughput');
title('Throughput vs Radius');
