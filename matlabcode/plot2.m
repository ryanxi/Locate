function  [y] = plot2( err,err1)
%function  [y] = plot2( err1)
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here
figure

set(gca,'Position',[.13 .17 .70 .54]);
initValue=0;
step=0.1;

[xTime1,yPercentage1]=cdf(initValue,step,max(err),err);
plot(xTime1,yPercentage1,'-g*','LineWidth',2,'MarkerEdgeColor','g','MarkerFaceColor','g','MarkerSize',4);
hold on;
[xTime2,yPercentage2]=cdf(initValue,step,max(err1),err1);
plot(xTime2,yPercentage2,'-rs','LineWidth',2,'MarkerEdgeColor','r','MarkerFaceColor','r','MarkerSize',4);

maxValue=max(max(err1),max(err));
%maxValue=max(max(err1));
axis([0 maxValue 0 1]);
set(gca,'XTick',0:0.5:maxValue);
set(gca,'YTick',0:0.2:1);

ylabel('CDF (100%)');
%xlabel('Relative Direction Error (degree)');
xlabel('Localization Error (m)');
legend({'Swadloon','SITE'},'Location','SouthEast');
%legend('SITE','Locatioin','SouthEast');

fontsize = 20;
set(get(gca,'XLabel'),'FontSize',fontsize);
set(get(gca,'YLabel'),'FontSize',fontsize);
set(gca,'FontSize',fontsize);

figure
set(gca,'Position',[.13 .17 .70 .54]);
%subplot(2,1,2);
e = sort(err1);
k = 1;
for i=6:0.5:9.5
    y(k,1) = prctile(e,i*10);
    k = k+1;
end

bar(y,1);
grid on;
set(gca,'xticklabel',{'60','65','70','75','80','85','90','95'},'FontSize',16);

%ylabel('localization error (m)');
ylabel('localization error (degree)');
xlabel('percentile (%)');

fontsize = 20;
set(get(gca,'XLabel'),'FontSize',fontsize);
set(get(gca,'YLabel'),'FontSize',fontsize);
%set(get(gca,'XTick'),'FontSize',16);
%set(get(gca,'YTick'),'FontSize',fontsize);

end

