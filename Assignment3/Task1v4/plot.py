import numpy as np
import matplotlib.pyplot as plt

n = [1000 * i for i in range(1, 11)]

# t_put_ab = [0.1555, 0.1896, 0.1697, 0.1329, 0.1037, 0.0716, 0.0558, 0.0374, 0.0246, 0.0171]
# packetLoss_ab = [0.3921, 0.6149, 0.7724, 0.8683, 0.9174, 0.9524, 0.9677, 0.9813, 0.9892, 0.9931]
# CI_ab = [0.000568, 0.000325, 0.000332, 0.000149, 0.000119, 0.0000633, 0.0000692, 0.0000406, 0.0000272, 0.0000249]

# # Plotting confidence interval for the packet loss
# for i in range(10):
#     top = packetLoss_ab[i] + CI_ab[i]
#     bottom = packetLoss_ab[i] - CI_ab[i]
#     plt.plot([n[i], n[i]], [top, bottom])


# plt.plot(n, t_put_ab)
# plt.plot(n, packetLoss_ab)
# plt.xticks(n)
# plt.xlabel("Nbr Sensors")
# plt.ylabel("Probability")
# plt.legend(['Throughput', 'Packet loss'])
# plt.savefig("images/throughput_packetLoss_ab")


first = open("10_100", "r")
second = open("100_1000", "r")
third = open("1000_10000", "r")
fourth = open("2000_20000", "r")
fifth = open("10000_100000", "r")

files = [first, second, third, fourth, fifth]


for i in range(len(files)):
    t_put = []
    packetLoss = []
    CI = []
    for line in files[i]:
        t, p, c = line.split(" ")
        t_put.append(float(t))
        packetLoss.append(float(p))
        CI.append(float(c))
    #plt.plot(n, t_put)
    plt.plot(n, packetLoss)

plt.xticks(n)
plt.legend(['lb = 10, ub = 100','lb = 100, ub = 1000', 'lb = 1000, ub = 10000', 'lb = 2000, ub = 20000', 'lb = 10000, ub = 100000'])

plt.xlabel("Nbr Sensors")
plt.ylabel("Probability")
plt.savefig("images/1cthroughput")
plt.savefig("images/1cpacketloss")


# first = open("vary_r", "r")
# r = [i * 1000 for i in range(6, 12)]

# t_put = []
# packetLoss = []
# CI = []
# for line in first:
#     t, p, c = line.split(" ")
#     t_put.append(float(t))
#     packetLoss.append(float(p))
#     CI.append(float(c))


# plt.plot(r, t_put)
# plt.xticks(r)
# plt.xlabel("Radius")
# plt.ylabel("Transmission Rate")
# plt.savefig("images/1d")