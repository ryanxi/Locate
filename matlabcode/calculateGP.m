function [ gp1 ] = calculateGP( r,t,rp )
%CALCULATEGP Summary of this function goes here
%   Detailed explanation goes here

a = size(rp,1);
b = size(rp,2);
gp1 = zeros(a,b);

for i =1:1:b
gp1(:,i) = r*rp(:,i) + t
end

end

