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

bar(va,1,'grouped');

set(gca,'xticklabel',3:4:5:6);

legend('mean error','median error');
ylabel('error of accuracy (m)');
xlabel('number of anchor');

end

