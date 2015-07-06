function [ CrossPointx,CrossPointy ] = CircleCrossPoint( Anchorx1,Anchory1,Angle1,Anchorx2,Anchory2,Angle2,Anchorx3,Anchory3,Angle3 )
%UNTITLED Summary of this function goes here
%CircleCrossPoint函数功能是已知三点坐标以及未知点到三点的角度，求出未知点的坐标。
%输入：
%Anchorx1，Anchorx2，Anchorx3-分别表示三个已知点的X轴坐标
%Anchory1，Anchory2，Anchory3-分别表示三个已知点的Y轴坐标
%Angle1,Angle2,Angle3-分别表示未知点到三个已知点的角度
%输出：
%CrossPointx,CrossPointy-表示未知点的X,Y轴坐标
%   Detailed explanation goes here

%利用已知两点坐标和到达角度求相应圆的圆心坐标和圆半径。
[cpx1,cpy1,r1]=Circle(Anchorx1,Anchory1,Angle1,Anchorx2,Anchory2,Angle2);
[cpx2,cpy2,r2]=Circle(Anchorx1,Anchory1,Angle1,Anchorx3,Anchory3,Angle3);
[cpx3,cpy3,r3]=Circle(Anchorx3,Anchory3,Angle3,Anchorx2,Anchory2,Angle2);

circle1 = '(x - cpx1)^2 + (y - cpy1)^2 = r1^2';
circle2 = '(x - cpx2)^2 + (y - cpy2)^2 = r2^2';
circle3 = '(x - cpx3)^2 + (y - cpy3)^2 = r3^2';

CrossPointx = inf;
CrossPointy = inf;

%初始化一个2*3的数组，存放三个交点坐标
cp = zeros(2,3);
 %求解圆circle12与circle13交点坐标，如果交点为一个则判断是否在circle23上，若是则作为未知节点坐标返回，
 %如果交点为两个，若交点在圆circle23内则保存在数组cp，若在圆上则作为未知节点坐标返回，若在圆外则不处理。
cp12 = solve(circle1,circle2,'x,y');
tmpx = eval(cp12.x(2));
tmpy = eval(cp12.y(2));
if size(cp12.x,1) == 1
   if abs((tmpx - cpx3)^2 + (tmpy - cpy3)^2 - r3^2)<= 1.0e-10
        CrossPointx = tmpx;
        CrossPointy = tmpy;
        return;
    end
elseif size(cp12.x,1) == 2
    if abs((tmpx - cpx3)^2 + (tmpy - cpy3)^2 - r3^2)<= 1.0e-10
        CrossPointx = tmpx;
        CrossPointy = tmpy;
        return;
    elseif  (tmpx - cpx3)^2 + (tmpy - cpy3)^2 < r3^2
        cp(1,1) = tmpx;
        cp(2,1) = tmpy;
        tmpx = eval(cp12.x(1));
        tmpy = eval(cp12.y(1)); 
        if abs((tmpx - cpx3)^2 + (tmpy - cpy3)^2 - r3^2)<= 1.0e-10
            CrossPointx = tmpx;
            CrossPointy = tmpy;
            return;            
        elseif (tmpx - cpx3)^2 + (tmpy - cpy3)^2 < r3^2
            return;
        end
    else
        tmpx = eval(cp12.x(1));
        tmpy = eval(cp12.y(1)); 
        if abs((tmpx - cpx3)^2 + (tmpy - cpy3)^2 - r3^2)<= 1.0e-10
            CrossPointx = tmpx;
            CrossPointy = tmpy;
            return;            
        elseif (tmpx - cpx3)^2 + (tmpy - cpy3)^2 < r3^2
            cp(1,1) = tmpx;
            cp(2,1) = tmpy;
        else
            return;
        end
    end
else
    return;
end

cp13 = solve(circle1,circle3,'x,y');
tmpx = eval(cp13.x(2));
tmpy = eval(cp13.y(2));
if size(cp13.x,1) == 1
   if abs((tmpx - cpx2)^2 + (tmpy - cpy2)^2 - r2^2) <= 1.0e-10
        CrossPointx = tmpx;
        CrossPointy = tmpy;
        return;
    end
elseif size(cp13.x,1) == 2
    if abs((tmpx - cpx2)^2 + (tmpy - cpy2)^2 - r2^2) <= 1.0e-10
        CrossPointx = tmpx;
        CrossPointy = tmpy;
        return;
    elseif (tmpx - cpx2)^2 + (tmpy - cpy2)^2 < r2^2
        cp(1,2) = tmpx;
        cp(2,2) = tmpy;
        tmpx = eval(cp13.x(1));
        tmpy = eval(cp13.y(1)); 
        if abs((tmpx - cpx2)^2 + (tmpy - cpy2)^2 - r2^2) <= 1.0e-10
            CrossPointx = tmpx;
            CrossPointy = tmpy;
            return;
        elseif (tmpx - cpx2)^2 + (tmpy - cpy2)^2 < r2^2
            return;
        end   
    else
        tmpx = eval(cp13.x(1));
        tmpy = eval(cp13.y(1)); 
        if abs((tmpx - cpx2)^2 + (tmpy - cpy2)^2 - r2^2) <= 1.0e-10
            CrossPointx = tmpx;
            CrossPointy = tmpy;
            return;
        elseif (tmpx - cpx2)^2 + (tmpy - cpy2)^2 < r2^2
            cp(1,2) = tmpx;
            cp(2,2) = tmpy;
        else
            return;
        end
    end
else
    return;
end

cp23 = solve(circle2,circle3,'x,y');
tmpx = eval(cp23.x(2));
tmpy = eval(cp23.y(2));
if size(cp23.x,1) == 1
   if abs((tmpx - cpx1)^2 + (tmpy - cpy1)^2 - r1^2) <= 1.0e-10
        CrossPointx = tmpx;
        CrossPointy = tmpy;
        return;
    end
elseif size(cp23.x,1) == 2
    if abs((tmpx - cpx1)^2 + (tmpy - cpy1)^2 - r1^2) <= 1.0e-10
        CrossPointx = tmpx;
        CrossPointy = tmpy;
        return;
    elseif (tmpx - cpx1)^2 + (tmpy - cpy1)^2 < r1^2
        cp(1,3) = tmpx;
        cp(2,3) = tmpy;
        tmpx = eval(cp23.x(1));
        tmpy = eval(cp23.y(1)); 
        if abs((tmpx - cpx1)^2 + (tmpy - cpy1)^2 - r1^2) <= 1.0e-10
            CrossPointx = tmpx;
            CrossPointy = tmpy;
            return;
        elseif (tmpx - cpx1)^2 + (tmpy - cpy1)^2 < r1^2
            return;
        end  
    else
        tmpx = eval(cp23.x(1));
        tmpy = eval(cp23.y(1)); 
        if abs((tmpx - cpx1)^2 + (tmpy - cpy1)^2 - r1^2) <= 1.0e-10
            CrossPointx = tmpx;
            CrossPointy = tmpy;
            return;
        elseif (tmpx - cpx1)^2 + (tmpy - cpy1)^2 < r1^2
            cp(1,3) = tmpx;
            cp(2,3) = tmpy;
        else
            return;
        end
    end
else
    return;
end

%求解三个交点所在圆的圆心坐标（x1,y1）
circle12 = '(cp(1,1) - x1)^2 + (cp(2,1) - y1)^2 = cr^2';
circle13 = '(cp(1,2) - x1)^2 + (cp(2,2) - y1)^2 = cr^2';
circle23 = '(cp(1,3) - x1)^2 + (cp(2,3) - y1)^2 = cr^2';
circle = solve(circle12,circle13,circle23,'x1,y1,cr');
x1 = eval(circle.x1(1));
y1 = eval(circle.y1(1));
r =  eval(circle.cr(1));

%圆心坐标作为未知点的坐标返回
CrossPointx = x1;
CrossPointy = y1;

end

