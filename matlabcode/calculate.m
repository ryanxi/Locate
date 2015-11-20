function [ points ] = calculate( angles )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here


points = zeros(10,2)
for i = 1:1:10
    [x,y] = LocatePoint1(angles(i,:));
    points(i,1) = x;
    points(i,2) = y;
end

end

