function [  ] = plots( err1,err2,err3,err4 )
%UNTITLED5 Summary of this function goes here
%   Detailed explanation goes here


initValue=0;
step=0.05;
maxValue1 = max(err1);
maxValue2 = max(err2);
maxValue3 = max(err3);
maxValue4 = max(err4);
maxValue = max(max(maxValue1,maxValue2),max(maxValue3,maxValue4));


[xTime1,yPercentage1]=cdf(initValue,step,maxValue,err1);
plot(xTime1,yPercentage1,'r');
hold on 

[xTime2,yPercentage2]=cdf(initValue,step,maxValue,err2);
plot(xTime2,yPercentage2,'g');
hold on

[xTime3,yPercentage3]=cdf(initValue,step,maxValue,err3);
plot(xTime3,yPercentage3,'b');
hold on

[xTime4,yPercentage4]=cdf(initValue,step,maxValue,err4);
plot(xTime4,yPercentage4,'y');
hold on
 
legend('number of acoustic source = 3','number of acoustic source = 4','number of acoustic source = 5','number of acoustic source = 6');
ylabel('CDF of accuracy error (%)');
xlabel('error(m)');


end

