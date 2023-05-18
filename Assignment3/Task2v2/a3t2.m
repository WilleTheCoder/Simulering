data = load("radius_res.txt")
x = data(:,1)
y = data(:,2)
plot(x,y)

title("Throughput vs Radius")
xlabel("Radius")
ylabel("Throughput")
