%%%%%%%%%%%%%%%%%v1改动%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
把里面很多的注释都删掉了，做了一下改动：
1.把原先的写文件部分的的代码，替换成填充数组
2.添加了一个函数processdata，就是用来处理数据的，在原先关闭文件的地方，执行processdata
3.添加了一个文本框，用来显示最终求出来的角度

%%%%%%%%%%%%%%%%%v2改动%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
1.重新加入写文件的部分

%%%%%%%%%%%%%%%%%v3改动%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
1.加入了指南针，以及指示方向的箭头

%%%%%%%%%%%%%%%%%v4改动%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
1.加入用加速度和磁场传感器计算的RotationMatrix
2.求出了加速度和磁场传感器确立的坐标系
3.根据加速度和磁场传感器确立的坐标系和RotationVector确立的坐标系，在设备坐标系下
的夹角以及原先在RotationVector确立的坐标系下计算的目标方向角度，求出目标在加速度和磁
场传感器确立的坐标系下的方向角度

%%%%%%%%%%%%%%%%%v5改动%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
0.将result的范围从[-90,270]度，改成[0,360]度
1.将
(1)目标在RotationVector确立的坐标系下的方向角度，x轴逆时针旋转的角度,result~[0,360]度
(2)加速度和磁场传感器确立的坐标系的北方映射到设备坐标系，计算出相对于设备坐标系y轴顺时针旋转的角度,mAngle2~[0,360]度
(3)RotationVector确立的坐标系的北方映射到设备坐标系，计算出相对于设备坐标系y轴顺时针旋转的角度,mAngle3~[0,360]度
(4)计算的目标在加速度和磁场传感器确立的坐标系下的方向角度，x轴逆时针旋转的角度,result1~[0,360]度,(mAngle3-mAngle2+result) 伪mod 360 
(5)目标在设备坐标系相对于设备坐标系y轴顺时针旋转的角度,angle~[0,360]度
写入angle.dat文件(按下Click to Stop按钮之后的角度值)

2.将
加速度和磁场传感器确立的坐标系的北方映射到设备坐标系，计算出相对于设备坐标系y轴顺时针旋转的角度
写入mangle2.dat文件(实时记录)

3.将
RotationVector确立的坐标系的北方映射到设备坐标系，计算出相对于设备坐标系y轴顺时针旋转的角度
写入mangle3.dat文件(实时记录)

4.wave.dat文件，原先存的是PLL计算的相位值(Float类型)，现在改成存声音的原始值(Short类型)。

%%%%%%%%%%%%%%%%%v6改动%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
1.加速度的三次样条插值改成线性插值
2.mu由0.2改成0.02,又从0.02改成0.03
3.增加保存rotationmatrix(16维的float)，加速度磁场对应的rotationmatrix存到acc_mag_rotationmatrix.dat,
rotationvector对应的rotationmatrix存到rotationvector_rotationmatrix.dat;每SensorChange一次就保存一次
4.增加保存参与运算的mRotationMatrix(16维的float)到rotationvector_rotationmatrix1.dat文件
5.galaxy note 7.7 + android 3.2的event.timestamp是自开机以来的一个纳秒计时数,nexus7+android4.?的则不是
http://code.google.com/p/android/issues/detail?id=7981 
Documentation Enhancement: SensorEvent timestamp
6.界面上加了一个加速度磁场确定的指南针，现在界面上的头三个指示器，分别是指向锚点的箭头，加速度磁场指南针，rotationvector指南针
7.把传感器的registerListener和unregisterListener，移到onResume和onPause方法中

8.修正巨型bug一个，
把下面这句话注释了
if (update > 0.15)
	update = 0.15;
if (update < -0.15)
	update = -0.15;

%%%%%%%%%%%%%%%%%v6.1改动%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
1.UI改动：指示器变为4个，两个箭头:第一个表示RotationVector对应的目标位置方向，
第二个表示我们自己算出来的RotationVector对应的目标位置方向,
两个罗盘，第一个表示RotationVector对应的罗盘，第二个表示我们自己算出来的RotationVector对应的罗盘

2.mAngle2 现在用来存放我们计算得到的RotationMatrix对应的目标位置方向