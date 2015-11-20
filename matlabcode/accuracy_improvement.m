x = 1:1:7;
y=[43.18,13.3297,78.3167,9.574,22.3846,64.538,62.6938];

plot(x,y);

set(gca,'xtick',[1 2 3 4 5 6 7])
set(gca,'xticklabel',{'(6,-4)','(12,-4)','(18,-4)','(24,-4)','(12,-7)','(18,-7)','(24,-7)'})
set(gca,'ytick',0:20:100)
xlabel('spot position');
ylabel('accuracy improvement(%)');
