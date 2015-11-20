function  [] = plot2( err)
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here

initValue=0;
step=0.1;
%sample1=[0.7,1.2,1.5,2.0,1.3,1.7,2.2,2.5,3.6];
%sample2=[0.8,1.1,1.4,2.1,1.2,1.8,2.1,2.4,3.7,4.2,5.4];
%endValue1=ceil(max(sample1));
subplot(2,1,1);
 
%endValue=max(endValue1,endValue2);
 
%[xTime1,yPercentage1]=cdf(initValue,step,endValue,sample1);
%[xTime2,yPercentage2]=cdf(initValue,step,endValue,sample2);
 [xTime1,yPercentage1]=cdf(initValue,step,max(err),err);

plot(xTime1,yPercentage1,'r');
hold on 

%[xTime2,yPercentage2]=cdf(initValue,step,max(err1),err1);

%plot(xTime2,yPercentage2,'g');
 
 
ylabel('CDF (%)');
xlabel('Error (m)');


legend('ILAPS');

subplot(2,1,2);
e = sort(err);
for i=2:1:9
    y(i-1,1) = prctile(e,i*10);
end

bar(y,1);
grid on;
set(gca,'xticklabel',{'20%','30%','40%','50%','60%','70%','80%','90%'});

ylabel('accuracy error (m)');
xlabel('prctile (%)');
end

