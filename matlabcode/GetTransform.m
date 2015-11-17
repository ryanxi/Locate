function [ R,t ] = GetTransform( local_coordinate, global_coordinate)
%   Object Geo-tagging: Finding the transformation(local to global), rotation R and
%   translation t, between local and global coordinates
%   Detailed explanation goes here
%   local_coordinate - set of N positions in local camera frame
%   global_coordinate - set of N positions in global frame
%   

N = size(local_coordinate,2);

mean_local_coordinate = mean(local_coordinate,2);
mean_global_coordinate = mean(global_coordinate,2);

C = zeros(3);
for i=1:1:N
    localvec = local_coordinate(:,i) - mean_local_coordinate;
    globalvec = global_coordinate(:,i) - mean_global_coordinate;
    tmp = localvec*globalvec';
    C = C + tmp;
end

[U,S,V] = svd(C);

R = V*U';
t = mean_global_coordinate - R*mean_local_coordinate; 

end

