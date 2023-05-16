addpath("C:\code\Simulering\Assignment3\Task1")
data = load("stra1_lossrate.txt")
data2 = load("stra2_lossrate.txt")
x = data(:,1)
y = data(:,2)
y2 = data2(:,2)
plot(x,y)
hold on
plot(x,y2)
title("Loss Rate vs Network Load")
%title("Throughput vs Network Load")
xlabel("Network Load")
ylabel("Loss Rate")
%ylabel("Throughput")
legend("Strategy 1", "Strategy 2")








