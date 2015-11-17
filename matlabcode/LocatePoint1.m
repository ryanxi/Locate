function [ Locatex,Locatey ] = LocatePoint1( Angles )
%UNTITLED Summary of this function goes here
%Function LocatePoint() caculate the coordinate of cross point using the
%vertic Angles[] present the opening angle from X axis to acoustic source
%   Detailed explanation goes here

format long;

AnchorPoint = [6,12,18;0,0,0];

Point = zeros(2,10);
circlePoint = zeros(3,10);

m=1;
for i=1:1:2
    for j=i+1:1:3
        [circlePoint(1,m),circlePoint(2,m),circlePoint(3,m)] = Circle(AnchorPoint(1,i),AnchorPoint(2,i),Angles(1,i),AnchorPoint(1,j),AnchorPoint(2,j),Angles(1,j));
        m=m+1;
    end
end 

for i=1:1:10
      Point(1,i) = inf;
      Point(2,i) = inf;
end

count = 1;
for i=1:1:1
    for j=i+1:1:2
        for k=j+1:1:3
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

count1 = count - 1;
minimum = inf;
for i=1:1:count1
    sum = 0;
    for j=1:1:2
        for k = j+1:1:3
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

count2 = count - 1;
minimum = inf;
for i=1:1:count2
    sum = 0;
    for j=1:1:m-1
        dis = abs(sqrt((Point(1,i)-circlePoint(1,j))^2 + (Point(2,i)-circlePoint(2,j))^2) - circlePoint(3,j));
        sum = sum +dis;
    end
    if sum < minimum 
        minimum = sum;
        minimumPoint1 = i;
    end
end

Locatex1 = Point(1,minimumPoint1);
Locatey1 = Point(2,minimumPoint1);

end

