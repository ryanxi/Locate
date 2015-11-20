function [ points ] = locate( angles )
%LOCATE Summary of this function goes here
%   Detailed explanation goes here
column = size(angles,1);
points = zeros(column,2);

for i = 1:1:column
    [x,y] = LocatePoint(angles(i,:));
    points(i,1) = x;
    points(i,2) = y;
end

end

