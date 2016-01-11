function [  ] = meanerr( err1,err2,err3,err4 )
%MEANERR Summary of this function goes here
%   Detailed explanation goes here
m(1,1)=mean(err1);
m(1,2)=mean(err2);
m(1,3)=mean(err3);
m(1,4)=mean(err4);

md(1,1)=median(err1);
md(1,2)=median(err2);
md(1,3)=median(err3);
md(1,4)=median(err4);

va(:,1) = m';
va(:,2) = md';

maxValue = max(max(md(:,1)),max(md(:,2)));

set(gca,'Position',[.13 .17 .70 .54]);

bar(va,1);

set(gca,'FontSize',14);
%set(gca,'xticklabel',{'Swadloon-LoS','SITE-LoS','Swadloon-NLoS','SITE-NLoS'});
set(gca,'xticklabel',{'3','4','5','6'});
%axis([0 maxValue 0 1]);
%set(gca,'YTick',0:0.5:maxValue);
legend('mean error','median error');

ylabel('Localization Error (m)');
%xlabel('Different Measurements')
xlabel('Number of acoustic sources');

fontsize = 20;
set(get(gca,'XLabel'),'FontSize',fontsize);
set(get(gca,'YLabel'),'FontSize',fontsize);

%applyhatch(gcf,'\x');
%set(gcf,'color','w');
end

