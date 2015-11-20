function [ error,error1 ] = calculateERR( gp,gp1,x,y )
%ERROR Summary of this function goes here
%   Detailed explanation goes here
a = size(gp,2);
error = zeros(1,a);
error1 = zeros(1,a);
for i = 1:1:a
error(1,i) = sqrt((gp(1,i)-x)^2 + (gp(2,i)-y)^2);
error1(1,i) = sqrt((gp1(1,i)-x)^2 + (gp1(2,i)-y)^2);
end

end

