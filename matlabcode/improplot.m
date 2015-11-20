function  [] = improplot( imp1)
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here

initValue=0;
step=10;

 [xTime1,yPercentage1]=cdf(initValue,step,100,imp1);

plot(xTime1,yPercentage1,'r');
hold on 

 
ylabel('CDF (%)');
xlabel('Percent improvement in Accuracy (%)');


legend('ILAPS');

end
