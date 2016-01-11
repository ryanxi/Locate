%function [ error,error1 ] = calculateERR( gp,gp1,x,y )
function [ error ] = calculateERR( gp,x,y )

%ERROR Summary of this function goes here
%   Detailed explanation goes here
a = size(gp,1);
error = zeros(a,1);
%error1 = zeros(1,a);
for i = 1:1:a
error(i,1) = sqrt((gp(i,1)-x)^2 + (gp(i,2)-y)^2);
%error1(1,i) = sqrt((gp1(1,i)-x)^2 + (gp1(2,i)-y)^2);
end

end

