function [ Locatex,Locatey ] = LocatePoint( Angles )
%UNTITLED Summary of this function goes here
%Function LocatePoint() caculate the coordinate of cross point using the
%vertic Angles[] present the opening angle from X axis to acoustic source
%   Detailed explanation goes here

AnchorPoint = {0,6,12,18,24,30;-3,0,0,0,0,-3};

Point = zeros(2,30);
for i=1:1:30
      Point(1,i) = inf;
      Point(2,i) = inf;
end

count = 1;
for i=1:1:4
    for j=i+1:1:5
        for k=j+1:1:6
            [tmpx,tmpy] = CircleCrossPoint(AnchorPoint(1,i),AnchorPoint(2,i),Angles(1,i),AnchorPoint(1,j),AnchorPoint(2,j),Angles(1,j),AnchorPoint(1,k),AnchorPoint(2,k),Angles(1,k));
            if tmpx == inf || tmpy == inf
                continue;
            else
                Point(1,count) = tmpx;
                Point(2,count) = tmpy;
                count = count + 1;
            end
        end
    end
end

minimum = inf;
for i=1:1:count
    sum = 0;
    for j=1:1:5
        for k = j+1:1:6
            dis = Distance(AnchorPoint(1,j),AnchorPoint(2,j),AnchorPoint(1,k),AnchorPoint(2,k),Point(1,i),Point(2,i));
            sum = sum + dis;
        end
    end
    if sum < minimum 
        minimum = sum;
        minimumPoint = i;
    end
end

Locatex = Point(1,minimumPoint);
Locatey = Point(2,minimumPoint);

end

