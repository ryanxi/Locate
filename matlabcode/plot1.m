close all;
clear all;


N = 100000;
x = randn(1,N);
y = randn(1,N);
r = sqrt(0.5*(x.^2 + y.^2));%每个分量的方差为0.5
step = 0.1;
range = 0:step:3;
h = hist(r,range);



pr_approx_cdf = cumsum(h)/(sum(h));
figure(1)
plot(range,raylcdf(range,sqrt(0.5))),hold on
plot(range,pr_approx_cdf,'rs','LineWidth',2)
xlabel('z'),ylabel('CDF'),title('The CDF of Rayleigh distribution')
legend('pr\_theory\_matlab','pr\_approx\_cdf','Location','Best')