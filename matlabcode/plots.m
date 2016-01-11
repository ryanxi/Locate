function [  ] = plots( err1,err2,err3,err4)
%UNTITLED5 Summary of this function goes here
%   Detailed explanation goes here


initValue=0;
step=0.1;
maxValue1 = max(err1);
maxValue2 = max(err2);
maxValue3 = max(err3);
maxValue4 = max(err4);
maxValue = max(max(maxValue1,maxValue2),max(maxValue3,maxValue4));

set(gca,'Position',[.13 .17 .70 .54]);
[xTime1,yPercentage1]=cdf(initValue,step,maxValue1,err1);
plot(xTime1,yPercentage1,'-r','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',4);
hold on 

[xTime2,yPercentage2]=cdf(initValue,step,maxValue2,err2);
plot(xTime2,yPercentage2,'-b>','LineWidth',2,'MarkerEdgeColor','b','MarkerFaceColor','b','MarkerSize',4);
hold on

[xTime3,yPercentage3]=cdf(initValue,step,maxValue3,err3);
plot(xTime3,yPercentage3,'--gx','LineWidth',2,'MarkerEdgeColor','g','MarkerFaceColor','g','MarkerSize',4);
hold on

[xTime4,yPercentage4]=cdf(initValue,step,maxValue4,err4);
plot(xTime4,yPercentage4,'-m*','LineWidth',2,'MarkerEdgeColor','m','MarkerFaceColor','m','MarkerSize',4);
hold on

%legend({'Swadloon-LoS','SITE-LoS','Swadloon-NLoS','SITE-NLoS'},'Location','SouthEast');
legend({'Number = 3','Number = 4','Number = 5','Number = 6'},'Location','SouthEast');
%legend('Swadloon','ILAPS');
axis([0 maxValue 0 1]);
set(gca,'XTick',0:0.5:maxValue,'FontSize',20);
set(gca,'YTick',0:0.2:1);
ylabel('CDF (100%)');
xlabel('Localization Error(m)');

fontsize = 20;
set(get(gca,'XLabel'),'FontSize',fontsize);
set(get(gca,'YLabel'),'FontSize',fontsize);
%set(get(gca,'xticklabel'),'FontSize',fontsize);
%set(get(gca,'YTick'),'FontSize',fontsize);
%set(gca,'FontSize',fontsize);

end

