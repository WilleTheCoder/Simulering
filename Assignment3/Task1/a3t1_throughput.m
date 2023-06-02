
data1 = load('stra1_throughput.txt');
x1 = data1(:, 1); 
y1 = data1(:, 2);

data2 = load('stra2_throughput.txt');
x2 = data2(:, 1); 
y2 = data2(:, 2);

figure;
hold on;
plot(x1, y1, 'r', 'LineWidth', 2);
plot(x2, y2, 'b', 'LineWidth', 2); 
hold off;

xlabel('Network Load');
ylabel('Throughput');
title('Throughput vs Network Load');
legend('Strategy 1', 'Strategy 2');
