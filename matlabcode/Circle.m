function [ CenterPointx,CenterPointy,Radius ] = Circle( Anchorx1,Anchory1,Angle1,Anchorx2,Anchory2,Angle2 )
%UNTITLED3 Summary of this function goes here
%   Detailed explanation goes here

OpenAngle = abs(Angle1 - Angle2);
Length12 = sqrt((Anchorx1 - Anchorx2)^2 + (Anchory1 - Anchory2)^2);
Radius = Length12/(2*sind(OpenAngle));

MiddlePointx = (Anchorx1 + Anchorx2)/2;
MiddlePointy = (Anchory1 + Anchory2)/2;

if OpenAngle == 180
    CenterPointx = 0;
    CenterPointy = 0;
    Radius = 0;
    return;
elseif  OpenAngle == 90
    CenterPointx = MiddlePointx;
    CenterPointy = MiddlePointy;
    return;
end

[b,c] = solve('Anchorx1+b*Anchory1+c=0','Anchorx2+b*Anchory2+c=0');
b = eval(b(1));
c = eval(c(1));


f1 = '(MiddlePointx - x)*(Anchorx1 - Anchorx2) + (MiddlePointy - y)*(Anchory1-Anchory2) = 0';
f2 = '(Anchorx1 - x)^2 + (Anchory1 - y)^2 - Radius^2 = 0';
g = solve(f1,f2,'x,y');
if size(g.x) == 1
    CenterPointx = eval(g.x);
    CenterPointy = eval(g.y);
    return;   
end
tmpx = eval(g.x(1));
tmpy = eval(g.y(1));
%g.x(1) = eval(g.x(1));
%g.y(1) = eval(g.y(1));

%根据OpenAngle角度大小选择合适的圆心点，若OpenAngle为锐角则圆心与未知点同侧，否则异侧。
%此处由于Anchor点坐标有一定的规律，所以根据原点来选择适当的圆心坐标
if Anchory1 == Anchory2 && Anchory1 == 0             %两点在Y = 0直线上
    if OpenAngle < 90
        if tmpy < 0
            CenterPointx = tmpx;
            CenterPointy = tmpy;
        else
            CenterPointx = eval(g.x(2));
            CenterPointy = eval(g.y(2));
       end
    else
        if tmpy > 0
            CenterPointx = tmpx;
            CenterPointy = tmpy;
        else
            CenterPointx = eval(g.x(2));
            CenterPointy = eval(g.y(2));
        end
    end
elseif Anchory1 == Anchory2 && Anchory1 == -4           %两点在Y = -3直线上
    if OpenAngle < 90
        if tmpy < -4
            CenterPointx = tmpx;
            CenterPointy = tmpy;
        else
            CenterPointx = eval(g.x(2));
            CenterPointy = eval(g.y(2));
        end
    else
        if tmpy < -4
            CenterPointx = eval(g.x(2));
            CenterPointy = eval(g.y(2));
        else
            CenterPointx = tmpx;
            CenterPointy = tmpy;
        end
    end
elseif (Anchory1 - Anchory2)/(Anchorx1 - Anchorx2) > 0     %两点斜率为正
    answer = c*(tmpx + tmpy*b + c);
    if OpenAngle < 90
        if answer < 0
            CenterPointx = tmpx;
            CenterPointy = tmpy;
        else
            CenterPointx = eval(g.x(2));
            CenterPointy = eval(g.y(2));
        end
    else
        if answer < 0
            CenterPointx = eval(g.x(2));
            CenterPointy = eval(g.y(2));
        else
            CenterPointx = tmpx;
            CenterPointy = tmpy;
        end
    end    
else                                                      %两点斜率为负
    answer = c*(tmpx + tmpy*b + c);
    if OpenAngle < 90
        if answer > 0
            CenterPointx = tmpx;
            CenterPointy = tmpy;
        else
            CenterPointx = eval(g.x(2));
            CenterPointy = eval(g.y(2));
        end
    else
        if answer < 0
            CenterPointx = tmpx;
            CenterPointy = tmpy;
        else
            CenterPointx = eval(g.x(2));
            CenterPointy = eval(g.y(2));
        end
    end    
end

end

