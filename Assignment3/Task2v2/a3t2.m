data = load("distribution.txt")
x = data(:,1)/60
y = data(:,2)
bar(x,y)
title("Frequency vs Time")
xlabel("Time (min)")
ylabel("Frequency")

