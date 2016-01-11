function [  ] = vars( y )
%VARS Summary of this function goes here
%   Detailed explanation goes here
set(gca,'Position',[.13 .17 .70 .54]);
r = size(y,1);

y(r+1,1) = mean(y(:,1));
y(r+1,2) = mean(y(:,2));

bar(y,1,'grouped');
set(gca,'FontSize',14);
set(gca,'xticklabel',{'(6,-4)','(6,-7)','(12,-4)','(12,-7)','(18,-4)','(18,-7)','(24,-4)','(24,7)'});
set(gca,'YTick',0:0.2:1);
legend('Swadloon','SITE');
ylabel('variance of accuracy (m)');
xlabel('localization coordinate');

fontsize = 20;
set(get(gca,'XLabel'),'FontSize',fontsize);
set(get(gca,'YLabel'),'FontSize',fontsize);

end

