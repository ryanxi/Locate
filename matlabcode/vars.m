function [  ] = vars( y )
%VARS Summary of this function goes here
%   Detailed explanation goes here
bar(y,1,'grouped');
set(gca,'xticklabel',{'(6,-4)','(6,-7)','(12,-4)','(12,-7)','(18,-4)','(18,-7)','(24,-4)','(24,7)'});

legend('Swadloon','ILAPS');
ylabel('variance of accuracy');
xlabel('localization coordinate');

end

