function [ output_args ] = meanstd( input_args )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here

a = size(input_args,1);

output_args = zeros(a,2);

figure;

xlabel('mean square error');
ylabel('mean localiztion error');
for i = 1:1:a
    output_args(i,1) = mean(input_args(i,:));
    output_args(i,2) = std(input_args(i,:));
    plot(output_args(i,2),output_args(i,1),'o');
    hold on;
end

end

