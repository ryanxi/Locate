function fi = fi()
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
x = 1:1:7;
y1=[0.792,1.2774,1.2618,1.676,0.7322,1.4469,1.3416];
y2 = [0.45,1.1199,0.2736,1.5155,0.5683,0.5131,0.5005]

plot(x,y1);
hold on;
plot(x,y2);

set(gca,'xtick',[1 2 3 4 5 6 7])
set(gca,'xticklabel',{'(6,-4)','(12,-4)','(18,-4)','(24,-4)','(12,-7)','(18,-7)','(24,-7)'})
set(gca,'ytick',0:0.2:2)
xlabel('spot position');
ylabel('average error(m)');

legend('Swadloon''s static localization','ILAPS''s static localization')
end

