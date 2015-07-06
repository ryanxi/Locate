function [ Dis ] = Distance( x1,y1,x2,y2,x3,y3 )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here

%g = solve('a*x1+c=y1','a*x2+c=y2','a,c');
%tmpa=eval(g.a(1));
%tmpc=eval(g.c(1));
%Dis = abs(tmpa*x3-y3+tmpc)/sqrt(1+tmpa^2);


S = abs((y3-y1)*(x2-x1)-(x3-x1)*(y2-y1))/2;
length12 = sqrt((x2-x1)^2 + (y2-y1)^2);
Dis = 2*S/length12;



end

