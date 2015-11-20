function [ improvement ] = impro( err,err1 )
%UNTITLED6 Summary of this function goes here
%   Detailed explanation goes here

improvement = zeros(1,size(err,2));

for i=1:1:size(err,2)
    improvement(1,i) = (err(1,i)-err1(1,i))/err(1,i) * 100;
end

end

