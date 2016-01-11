function [  ] = fig( input_args )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
figure 
%a= size(input_args,1);

%for i = 1:1:a
 %   plot(input_args(i,2),input_args(i,1),'o');
  %  hold on;
%end

matrix = sortrows(input_args,2);

x = matrix(:,2);
y = matrix(:,1);

myfunc = inline('beta(2)*log(x+beta(3))+beta(1)','beta','x');
options = statset('MaxIter', 1000);
[beta,Res] = nlinfit(x,y,myfunc,[0 0 0],options);
a=beta(1),b=beta(2),k=beta(3)
rsqr = 1-(1/10)*sum(Res.^2)/var(y)
xx=min(x):max(x);
yy=b*log(xx+k)+a;
plot(x,y,'o',xx,yy);

%b = polyfit(x,y,3);
%yy = polyval(b,x);
%plot(x,y,'o');
%hold on;
%plot(x,yy,'r-');

max1 = max(input_args(:,1));
max2=max(input_args(:,2));

axis([0 max2 0 max1]);
%set(gca,'position',[0,0,61.8,100]);
set(gca,'Position',[.13 .17 .70 .54]);
set(gca,'FontSize',16);
set(gca,'XTick',0:0.5:max2);
set(gca,'YTick',0:0.5:max1); 

xlabel('Mead Standard Deviation(m)');
ylabel('Average Positioning Error(m)');



end

