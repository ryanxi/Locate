function  [] = improplot( imp1)
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here

initValue=0;
step=10;

 [xTime1,yPercentage1]=cdf(initValue,step,100,imp1);

set(gca,'Position',[.13 .17 .70 .54]);
plot(xTime1,yPercentage1,'r-');
hold on 
maxValue = max(imp1);
axis([0 maxValue 0 1]);
set(gca,'FontSize',16);
set(gca,'XTick',0:20:maxValue);
set(gca,'YTick',0:0.2:1); 
ylabel('CDF (100%)');
xlabel('Improvement in Accuracy (%)');


legend('SITE','Location','SouthEast');

fontsize = 20;
set(get(gca,'XLabel'),'FontSize',fontsize);
set(get(gca,'YLabel'),'FontSize',fontsize);

end
