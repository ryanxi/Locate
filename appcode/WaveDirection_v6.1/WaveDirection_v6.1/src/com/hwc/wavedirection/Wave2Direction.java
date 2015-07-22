//fSensorFile format: Timestamp in nanosecond, lacc0, lacc1, lacc2, ori0, ori1, ori2, (LONG, float*6)
//fWaveFile format: (44100Hz) 16bit wave, Big endian
package com.hwc.wavedirection;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import flanagan.interpolation.CubicSpline;
import flanagan.interpolation.LinearInterpolation;

public class Wave2Direction extends Activity implements SensorEventListener {
	public static double mean(double[] m) {
		double sum = 0;
		for (int i = 0; i < m.length; i++) {
			sum += m[i];
		}
		return sum / m.length;
	}

	static double mean(List<Double> p) {
		double sum = 0; // sum of all the elements
		for (int i = 0; i < p.size(); i++) {
			sum += p.get(i);
		}
		return sum / p.size();
	}
	
	public float getmangle(float[] paramArrayOfFloat){
		
		float[] worldNorthVector = {0.0F,1.0F,0.0F,1.0F};
		float[] deviceNorthVector = new float[4];
		
		Matrix.multiplyMV(deviceNorthVector, 0, paramArrayOfFloat, 0, worldNorthVector, 0);
		
		return 90.0F - (float)(180.0D * Math.atan2(deviceNorthVector[1], deviceNorthVector[0])/3.141592653589793D);
		
	}

	public class BPFThread extends Thread {
		// public void run() {
		// //init BPF data
		// filterwave.clear();
		//
		// FilterDesign fd = new FilterDesign();
		// double fs = 44100;
		// ButtordResult br = fd.buttord(18500*2/fs, 19500*2/fs,18000*2/fs,
		// 20000*2/fs, 10, 40);
		// BiResult bir = fd.butter(br.n, br.wnl, br.wnh);
		// int size=bir.a.length;
		// double[] a=new double[size];
		// double[] b=bir.b;
		// for (int i=0;i<size;i++)
		// a[i]=bir.a[size-i-1];
		// double uni=a[size-1];
		// for (int i=0;i<size;i++)
		// {
		// a[i]/=uni;
		// b[i]/=uni;
		// }
		// //TODO: the size is set to 15, but will change when a and b change
		// double[] xwindow=new double[size];
		// double[] ywindow=new double[size];
		// double[] buffer=new double[buffersize]; //output
		// double[] wavebuffer;
		// try {
		// wavebuffer=wavedata.take();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return;
		// }
		//
		// int i=0;
		// int j=0;
		// double tmpy=0;
		// while(isRecording)
		// {
		// int outputindex=i%buffersize;
		// // currentphase=i;
		//
		// xwindow[size-1]=wavebuffer[outputindex];
		// ywindow[size-1]=0;
		// buffer[outputindex]=0;
		// tmpy=0;
		// for(j=0;j<size-1;j++)
		// {
		// tmpy+=b[j]*xwindow[j]-a[j]*ywindow[j];
		// ywindow[j]=ywindow[j+1];
		// xwindow[j]=xwindow[j+1];
		// }
		// tmpy+=b[size-1]*xwindow[size-1];
		// ywindow[size-2]=tmpy;
		// buffer[outputindex]=tmpy;
		// // currentphase=tmpy;
		//
		// // try {
		// // Audiodataoutputstream.writeDouble(tmpy);
		// // } catch (IOException e1) {
		// // // TODO Auto-generated catch block
		// // e1.printStackTrace();
		// // }
		// i++;
		// if(i%buffersize==0)
		// {
		// int tmpsize=i/buffersize;
		// try {
		// wavebuffer=wavedata.take();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return;
		// }
		//
		// try {
		// filterwave.put(buffer);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// buffer = new double[buffersize];
		// }
		//
		// }
		//
		// }
		public void run() {
			// init BPF data
			filterwave.clear();

			double[] a = new double[15];
			double[] b = new double[15];
			a[14] = 0;
			a[13] = 11.7721066157362;
			a[12] = 65.3077589324416;
			a[11] = 226.185657446171;
			a[10] = 546.150036171117;
			a[9] = 972.316446369654;
			a[8] = 1315.93086786799;
			a[7] = 1375.24519518761;
			a[6] = 1115.19286352373;
			a[5] = 698.306288807843;
			a[4] = 332.415854579607;
			a[3] = 116.676646053042;
			a[2] = 28.5536436089761;
			a[1] = 4.36288610741819;
			a[0] = 0.314203960928239;
			b[14] = 3.34338905570474e-07;
			b[13] = 0;
			b[12] = -2.34037233899332e-06;
			b[11] = 0;
			b[10] = 7.02111701697995e-06;
			b[9] = 0;
			b[8] = -1.17018616949666e-05;
			b[7] = 0;
			b[6] = 1.17018616949666e-05;
			b[5] = 0;
			b[4] = -7.02111701697995e-06;
			b[3] = 0;
			b[2] = 2.34037233899332e-06;
			b[1] = 0;
			b[0] = -3.34338905570474e-07;
			// TODO: the size is set to 15, but will change when a and b change
			double[] xwindow = new double[15];
			double[] ywindow = new double[15];
			double[] buffer = new double[buffersize]; // output
			double[] wavebuffer;
			try {
				//wavebuffer = wavedata.take();
				wavebuffer = wavedata.poll(threaddelay, TimeUnit.SECONDS);
				if(wavebuffer == null)
					return;
				//Log.d("BPF take wavebuffer[0]:", "" + wavebuffer[0]);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}

			int i = 0;
			int j = 0;
			double tmpy = 0;
			
			int tmpcount = 0;
			
			while (isRecording) {
				int outputindex = i % buffersize;
				// currentphase=i;

				xwindow[14] = wavebuffer[outputindex];
				ywindow[14] = 0;
				buffer[outputindex] = 0;
				tmpy = 0;
				for (j = 0; j < 14; j++) {
					tmpy += b[j] * xwindow[j] - a[j] * ywindow[j];
					ywindow[j] = ywindow[j + 1];
					xwindow[j] = xwindow[j + 1];
				}
				tmpy += b[14] * xwindow[14];
				ywindow[13] = tmpy;
				buffer[outputindex] = tmpy;
				
				// try {
				// Audiodataoutputstream.writeDouble(tmpy);
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
				i++;
				if (i % buffersize == 0) {
					int tmpsize = i / buffersize;
					try {
						//wavebuffer = wavedata.take();
						wavebuffer = wavedata.poll(threaddelay, TimeUnit.SECONDS);
						if(wavebuffer == null)
							return;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}

					try {
						//filterwave.put(buffer);
						filterwave.offer(buffer, threaddelay, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					buffer = new double[buffersize];
				}

			}

		}
	}

	public class AGCThread extends Thread {
		public void run() {
			// init agcdata
			agcwave.clear();

			final int meanlength = 11;
			final double alpha = 0.9;
			final int R = 1;

			double A = 1;
			double[] buffer = new double[buffersize]; // output
			int i = 0;
			double[] filterbuffer;

			try {				
				//filterbuffer = filterwave.take();
				filterbuffer = filterwave.poll(threaddelay, TimeUnit.SECONDS);
				if(filterbuffer == null)
					return;
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}

			double[] meanbuf = new double[meanlength];
			double[] readbuf = new double[meanlength];
			double read = 0;
			for (i = 0; i < meanlength; i++) {
				read = filterbuffer[i];
				readbuf[i] = read;
				meanbuf[i] = Math.abs(read);
			}
			i = 0;
			while (isRecording) {
				int outputindex = i % buffersize;
				// buffer[outputindex]=filterbuffer[outputindex]*A;
				// buffer[i%buffersize]=readbuf[i%meanlength]*A;
				double result = readbuf[i % meanlength] * A;

				// if(result>1.5)
				// result=1.5;
				// if(result<-1.5)
				// result=-1.5;

				buffer[i % buffersize] = result;
				//Log.d("buffer "+(i%buffersize)+":",""+result);
				// try {
				// int k=i;
				// Audiodataoutputstream.writeDouble(buffer[outputindex]);
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }

				// double ARNT=mean(meanbuf);
				// A=A*(1-alpha*mean(meanbuf))+alpha*R;
				A = Math.exp(Math.log(A) * (1 - alpha) - alpha
						* Math.log(mean(meanbuf) / R));
				if(Double.isInfinite(A))
				{
					A = 1.0;
				}
				

				if ((i + meanlength) % buffersize == 0) {
					try {
						//filterbuffer = filterwave.take();
						filterbuffer = filterwave.poll(threaddelay, TimeUnit.SECONDS);
						if(filterbuffer == null)
							return;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
				}
				read = filterbuffer[(i + meanlength) % buffersize];
				meanbuf[i % meanlength] = Math.abs(read);
				readbuf[i % meanlength] = read;
				i++;
				if (i % buffersize == 0)
					try {
						//agcwave.put(buffer);
						agcwave.offer(buffer, threaddelay, TimeUnit.SECONDS);
						buffer = new double[buffersize];
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}

	public class PLLThread extends Thread {
		public void run() {
			// init
			theta.clear();

			final int meanlength = 11;
			// final double mu=0.08;
			final double mu = 0.03;
			double fc = 19000;
			double fliph[] = new double[11];
			final double pi = 3.1415926535;
			final double Ts = 1 / 44100.0;
			fliph[0] = 0.246942129113388;
			fliph[1] = 0.00262959944057275;
			fliph[2] = 0.00217401867197993;
			fliph[3] = 0.00224742173595086;
			fliph[4] = 0.00242199262250945;
			fliph[5] = 0.00247578810967912;
			fliph[6] = 0.00242199262250945;
			fliph[7] = 0.00224742173595086;
			fliph[8] = 0.00217401867197993;
			fliph[9] = 0.00262959944057275;
			fliph[10] = 0.246942129113388;

			double[] buffer = new double[buffersize]; // output
			buffer[0] = 2;
			int i = 0;
			double[] z = new double[11];
			// int totalsize=wavesize-meanlength-2;
			double[] agcbuffer;
			// double[] agcbuffer=agcwave.get(0);//input

			try {
				//agcbuffer = agcwave.take();
				agcbuffer = agcwave.poll(threaddelay, TimeUnit.SECONDS);
				if(agcbuffer == null)
					return ;				
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			// while(agcwave.isEmpty())
			// SystemClock.sleep(sleeptime);
			// synchronized (agcwave) {
			// agcbuffer=agcwave.get(0);
			// }
			while (isRecording) {
				double update = 0;

				for (int j = 0; j < 10; j++) {
					z[j] = z[j + 1];
					update += z[j] * fliph[j];
				}
				double tmp = buffer[i % buffersize];
				z[10] = agcbuffer[i % buffersize]
						* Math.sin(2 * pi * fc * Ts * (i + 1) + tmp);
				update += z[10] * fliph[10];

				i++;
				if (i % buffersize == 0) {
					int tmpsize = i / buffersize;

					try {
						//agcbuffer = agcwave.take();
						agcbuffer = agcwave.poll(threaddelay, TimeUnit.SECONDS);
						if(agcbuffer == null)
							return ;	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					}
					// while (agcwave.size()<=tmpsize)
					// SystemClock.sleep(sleeptime);
					// synchronized (agcwave) {
					// agcbuffer=agcwave.get(tmpsize);
					// }
					// agcbuffer=agcwave.get(i/buffersize);
					try {						
						//theta.put(buffer);
						theta.offer(buffer, threaddelay, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// synchronized (theta) {
					// theta.add(buffer);
					// }
					buffer = new double[buffersize];

					//
				}

				// try {
				// Audiodataoutputstream.writeDouble(update);
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				// if (update > 0.15)
				// update = 0.15;
				// if (update < -0.15)
				// update = -0.15;

				buffer[i % buffersize] = tmp - mu * update;
				// globalangle=tmp*5;

			}

		}
	}

	public class ProcessPhase extends Thread {
		public void run() {
			double[] buffer;
			while (true) {
				try {
					//buffer = theta.take();
					buffer = theta.poll(threaddelay, TimeUnit.SECONDS);
					if(buffer == null)
						return;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				for (int i = 0; i < buffersize; i++) {
					currentphase = buffer[i];
					drawpoint = buffer[i] * 5;

					// try {
					// Audiodataoutputstream.writeDouble(currentphase);
					move.add(currentphase * 340 / (2 * Math.PI * f));//always add NaN !!!!!
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				}
			}

		}
	}

	Button btnStart, btnStop, btnExit;
	TextView tvaccx, tvaccy, tvaccz, tvresult;
	ArrView arr;
	CompassView cv2, cv3;
	double currentphase = 0;
	float tmpx, tmpy, tmpz;
	SurfaceView sfv_soundwave;
	SeekBar skbVolume;// 调节音量
	boolean isRecording = false;// 是否录放的标记
	int settextcount = 0;
	int showtextperiod = 20;
	
	long threaddelay = 1L;

	// 参数调整
	static final long sleeptime = 10;
	static final int configAccelerameterRate = 500; // the desired delay between
													// events in microsecond
	static final int frequency = 44100;
	int f = 19000; // 产生的超声频率
	static final int meanlength = 4000; // 用meanlength个点做位置值的平滑
	static final int channelinConfiguration = AudioFormat.CHANNEL_IN_MONO;
	static final int channeloutConfiguration = AudioFormat.CHANNEL_OUT_MONO;
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	static final String filenameWave = "wave.dat";
	static final String filenameSensor = "sensor.dat";
	static final String filenameAngle = "angle.dat";
	static final String filenameMAngle2 = "mangle2.dat";
	static final String filenameMAngle3 = "mangle3.dat";
	static final String filenameRVRotationMatrix = "acc_mag_rotationmatrix.dat";
	static final String filenameAMRotationMatrix = "rotationvector_rotationmatrix.dat";
	static final String filenameRVRotationMatrix1 = "rotationvector_rotationmatrix_1.dat";

	String DirnameHWCphone = "/mnt/sdcard/hwcWaveSample/";

	// draw wave 参数
	int rateX = 16;
	int rateY = 8;
	int baseLine = 0;
	static final int xMax = 16;// X轴缩小比例最大值,X轴数据量巨大，容易产生刷新延时
	static final int xMin = 8;// X轴缩小比例最小值
	static final int yMax = 10;// Y轴缩小比例最大值
	static final int yMin = 1;// Y轴缩小比例最小值

	static final int buffersize = 4096;
	static final int queuesize = 3;
	BlockingQueue<double[]> wavedata = new ArrayBlockingQueue<double[]>(
			queuesize);
	BlockingQueue<double[]> filterwave = new ArrayBlockingQueue<double[]>(
			queuesize);
	BlockingQueue<double[]> agcwave = new ArrayBlockingQueue<double[]>(
			queuesize);
	BlockingQueue<double[]> theta = new ArrayBlockingQueue<double[]>(queuesize);
	// ArrayList<double[]> wavedata = new ArrayList<double[]>();
	// ArrayList<double[]> filterwave = new ArrayList<double[]>();
	// ArrayList<double[]> agcwave = new ArrayList<double[]>();
	// ArrayList<double[]> theta = new ArrayList<double[]>();
	int wavesize = 0;

	private ArrayList<short[]> inBuf = new ArrayList<short[]>();
	private ArrayList<short[]> mAudioData = new ArrayList<short[]>();
	Paint mPaint;

	int playBufSize;
	AudioRecord audioRecord;
	AudioTrack audioTrack;

	SensorManager sensorManager;
	public String directoryName;
	// public FileOutputStream fWaveRecord;
	// public RecordAudioThread recordThread;
	public DrawWaveThread drawThread;
	public String fulldirectoryName;

	public String fullFilenameWave;
	public FileOutputStream fWaveRecord;
	public BufferedOutputStream fBufferWaveRecord;
	public DataOutputStream Audiodataoutputstream;

	public String fullFilenameSensor;
	public FileOutputStream fSensorRecord;
	public BufferedOutputStream fBufferSensorRecord;
	public DataOutputStream Sensordataoutputstream;

	String fullFilenameAngle;
	String fullFilenameMAngle2;
	String fullFilenameMAngle3;
	String fullFilenameAMRotationMatrix;
	String fullFilenameRVRotationMatrix;
	String fullFilenameRVRotationMatrix1;
	DataOutputStream angleOutputstream;
	DataOutputStream mAngle2Outputstream;
	DataOutputStream mAngle3Outputstream;
	DataOutputStream myRotationMatrixOutputstream;
	DataOutputStream rVRotationMatrixOutputstream;
	DataOutputStream rVRotationMatrix1Outputstream;

	long gyroTimestamp;
	ArrayList<Double> move = new ArrayList<Double>(frequency * 10); // 预计会采样10秒这个数量级
	ArrayList<Float> timestamp = new ArrayList<Float>(1000);
	ArrayList<Float> lacc0 = new ArrayList<Float>(1000); // 加速度采样频率100Hz数量级*采样10秒数量级
	ArrayList<Float> lacc1 = new ArrayList<Float>(1000);
	ArrayList<Float> lacc2 = new ArrayList<Float>(1000);
	// public long sensorRecordtime;
	// private float[] accValue=new float[3];
	private float[] rotateValue = new float[3];
	float[] calcvector = new float[3];

	// private float[] magValue=new float[3];
	private boolean iscounting = false;
	private int minbuffersize;
	private TextView tvphase;
	// public long audiorecordtime;
	private double drawpoint;
	private long sensorRecordtime;
	private long audioRecordtime = 0;
	float[] mRotationMatrix = new float[16];
	float[] mRotationMatrix1 = new float[16];
	//private float[] mGravity;
	//private float[] mMagnetic;
	float mAngle2; // the north of acc&magnetic determined coordinate in device
					// coordinate
	float mAngle3;
	private boolean rotateget = false; // the north of rotation_vector
										// determined coordinate int

	// device coordinate

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wave2_direction);
		DirnameHWCphone = Environment.getExternalStorageDirectory().getPath()
				+ "/hwcWaveSample";
		File destDir = new File(DirnameHWCphone);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		// Audio configuration
		minbuffersize = AudioRecord.getMinBufferSize(frequency,
				channelinConfiguration, audioEncoding);

		playBufSize = AudioTrack.getMinBufferSize(frequency,
				channeloutConfiguration, audioEncoding);
		// -----------------------------------------
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelinConfiguration, audioEncoding, buffersize);


		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channeloutConfiguration, audioEncoding, playBufSize,
				AudioTrack.MODE_STREAM);
		// ------------------------------------------
		btnStart = (Button) this.findViewById(R.id.button_Start);
		btnStart.setOnClickListener(new ClickEvent());
		tvaccx = (TextView) this.findViewById(R.id.textaccx);
		tvaccy = (TextView) this.findViewById(R.id.textaccy);
		tvaccz = (TextView) this.findViewById(R.id.textaccz);
		tvphase = (TextView) this.findViewById(R.id.textPhase);
		tvresult = (TextView) this.findViewById(R.id.textResult);
		arr = (ArrView) findViewById(R.id.arr);
		cv2 = (CompassView) findViewById(R.id.cv2);
		cv3 = (CompassView) findViewById(R.id.cv3);
		tvaccx.setVisibility(4);

		// Sensor configuration
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// TODO set gravity
		// SensorManager.gr;
		// sensorManager
		// .registerListener(Wave2Direction.this, sensorManager
		// .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		// configAccelerameterRate);
		// sensorManager
		// .registerListener(Wave2Direction.this, sensorManager
		// .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
		// configAccelerameterRate);
		// sensorManager
		// .registerListener(Wave2Direction.this, sensorManager
		// .getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
		// configAccelerameterRate);
		// sensorManager
		// .registerListener(Wave2Direction.this, sensorManager
		// .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
		// configAccelerameterRate);
		// SoundWave surface
		sfv_soundwave = (SurfaceView) this
				.findViewById(R.id.surfaceView_soundwave);
		mPaint = new Paint();
		mPaint.setColor(Color.GREEN);// 画笔为绿色
		mPaint.setStrokeWidth(1);// 设置画笔粗细
		// aaaa=new BlockingQueue<double[]>(40);
		// recordThread=new RecordAudioThread();
		// drawThread=new DrawWaveThread(sfv_soundwave, mPaint);

	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(Wave2Direction.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(Wave2Direction.this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
				configAccelerameterRate);
		sensorManager.registerListener(Wave2Direction.this, 
				sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
				configAccelerameterRate);
		sensorManager.registerListener(Wave2Direction.this,
				sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				configAccelerameterRate);
		sensorManager.registerListener(Wave2Direction.this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_GAME);
		sensorManager.registerListener(Wave2Direction.this,
				sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_wave2_direction, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			isRecording = false;
			inBuf.clear();
			SystemClock.sleep(300);
			this.finish();
			return false;
		}
		return false;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		//Log.d("timestamp", "" + event.timestamp);
		if (!isRecording) {
			// just show two compass
			if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
//				rotateValue[0] = event.values[0];
//				rotateValue[1] = event.values[1];
//				rotateValue[2] = event.values[2];
//				float[] worldNorthVector = new float[] { 0, 1, 0, 1 };
//				float[] deviceNorthVector = new float[4];
				rotateValue = (float[])event.values.clone();
				SensorManager.getRotationMatrixFromVector(mRotationMatrix,
						event.values);
//				Matrix.multiplyMV(deviceNorthVector, 0, mRotationMatrix, 0,
//						worldNorthVector, 0);
//				mAngle3 = (float) (Math.atan(deviceNorthVector[0]
//						/ deviceNorthVector[1]) * 180 / Math.PI);
//				if (deviceNorthVector[1] < 0) {
//					mAngle3 = mAngle3 + 180;
//				} else if (deviceNorthVector[0] < 0) {
//					mAngle3 = mAngle3 + 360;
//				}
				
				mAngle3 = getmangle(mRotationMatrix);
				cv3.setDirection((int) mAngle3);
			}
			// else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// Log.d("acc", "acc");
			// mGravity = event.values.clone();
			// } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			// {
			// Log.d("mf", "mf");
			// mMagnetic = event.values.clone();
			// }
			// if (mGravity != null && mMagnetic != null) {
			// // Log.d("null",
			// //
			// ""+mGravity[0]+","+mGravity[1]+","+mGravity[2]+","+mMagnetic[0]+","+mMagnetic[1]+","+mMagnetic[2]);
			// SensorManager.getRotationMatrix(mRotationMatrix1, null,
			// mGravity, mMagnetic);
			// float[] worldNorthVector = new float[] { 0, 1, 0, 1 };
			// float[] deviceNorthVector = new float[4];
			// Matrix.multiplyMV(deviceNorthVector, 0, mRotationMatrix1, 0,
			// worldNorthVector, 0);
			// mAngle2 = (float) (Math.atan(deviceNorthVector[0]
			// / deviceNorthVector[1]) * 180 / Math.PI);
			// if (deviceNorthVector[1] < 0) {
			// mAngle2 = mAngle2 + 180;
			// } else if (deviceNorthVector[0] < 0) {
			// mAngle2 = mAngle2 + 360;
			// }
			// // cv2.setDirection((int) mAngle2);
			// }
			return;
		}
		// if(!iscounting)
		// iscounting=true;

		// TODO: maintaining iscounting is click the button again
		if (!iscounting) {
			if (event.timestamp < audioRecordtime || audioRecordtime == 0)
				return;
			else {
				sensorRecordtime = event.timestamp;
				iscounting = true;
			}

		}
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			float[] laccValue = new float[4];
			float[] matrixtrans = new float[16];
			float[] outputvalue = new float[4];
			laccValue[0] = event.values[0];
			laccValue[1] = event.values[1];
			laccValue[2] = event.values[2];
			laccValue[3] = 1;

			float time = (event.timestamp - sensorRecordtime) * 1.0f / 1000000000.0f;
			Matrix.transposeM(matrixtrans, 0, mRotationMatrix, 0);
			// Matrix.invertM(RotationMatrixbar,0,matrixtrans,0);
			Matrix.multiplyMV(outputvalue, 0, matrixtrans, 0, laccValue, 0);
			// outputvalue[0]=event.values[0];
			// outputvalue[1]=event.values[1];
			// outputvalue[2]=event.values[2];

			// float[] testvector=new float[4];
			// float[] testout=new float[4];
			// testvector[0]=1;
			// Matrix.multiplyMV(testout, 0, RotationMatrixbar, 0, testvector,
			// 0);
			// globalangle=Math.atan(testout[0]/testout[1])*400/3.14;

			// globalangle=10*Math.sqrt(outputvalue[0]*outputvalue[0]+outputvalue[1]*outputvalue[1]+outputvalue[2]*outputvalue[2]);
			// globalangle=1*Math.sqrt(laccValue[0]*laccValue[0]+laccValue[1]*laccValue[1]+laccValue[2]*laccValue[2]);
			// drawpoint=outputvalue[1]*10;
			// globalangle=rotateValue[0]*1000;
			// globalangle=Math.atan(outputvalue[0]/outputvalue[1])*400/3.14;

			try {
				Sensordataoutputstream.writeFloat(time);
				Sensordataoutputstream.writeFloat(outputvalue[0]);
				Sensordataoutputstream.writeFloat(outputvalue[1]);
				Sensordataoutputstream.writeFloat(outputvalue[2]);
				timestamp.add(time);
				lacc0.add(outputvalue[0]);
				lacc1.add(outputvalue[1]);
				lacc2.add(outputvalue[2]);
				// setText:
				settextcount++;
				float mean = 100.0f;
				tmpx = (outputvalue[0] + (mean - 1) * tmpx) / mean;
				tmpy = (outputvalue[1] + (mean - 1) * tmpy) / mean;
				tmpz = (outputvalue[2] + (mean - 1) * tmpz) / mean;
				if (settextcount % showtextperiod == 0) {
					tvaccx.setText(String.format("%1$,.5f", tmpx));
					tvaccy.setText(String.format("%1$,.5f", tmpy));
					tvaccz.setText(String.format("%1$,.5f", rotateValue[2]));
					// tvaccz.setText(String.format("%1$,.5f",(sensorRecordtime-audioRecordtime)*0.000000001f));
					// tvaccz.setText(String.format("%1$,.5f",Math.atan(tmpx/tmpy)*180/3.14));
					tvphase.setText("   "
							+ String.format("%1$,.2f", currentphase * 340 / 190
									/ 2 / 3.1415) + "cm");
					// tvphase.setText(String.valueOf(currentphase));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
//			rotateValue[0] = event.values[0];
//			rotateValue[1] = event.values[1];
//			rotateValue[2] = event.values[2];
//			float[] worldNorthVector = new float[] { 0, 1, 0, 1 };
//			float[] deviceNorthVector = new float[4];
//			SensorManager.getRotationMatrixFromVector(mRotationMatrix,
//					event.values);
//			Matrix.multiplyMV(deviceNorthVector, 0, mRotationMatrix, 0,
//					worldNorthVector, 0);
//			mAngle3 = (float) (Math.atan(deviceNorthVector[0]
//					/ deviceNorthVector[1]) * 180 / Math.PI);
//			if (deviceNorthVector[1] < 0) {
//				mAngle3 = mAngle3 + 180;
//			} else if (deviceNorthVector[0] < 0) {
//				mAngle3 = mAngle3 + 360;
//			}
//			try {
//				mAngle3Outputstream.writeFloat(mAngle3);
//				for (int i = 0; i < mRotationMatrix.length; ++i) {
//					rVRotationMatrixOutputstream.writeFloat(mRotationMatrix[i]);
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			cv3.setDirection((int) mAngle3);
			// arr.setDirection((int) angle);
			// rotateValue[3]=event.values[3];
			
			rotateValue = (float[])event.values.clone();
			SensorManager.getRotationMatrixFromVector(mRotationMatrix,
					event.values);
			mAngle3 = getmangle(mRotationMatrix);
			cv3.setDirection((int) mAngle3);
			
		}

		// else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		// Log.d("acc", "acc");
		// mGravity = event.values.clone();
		// } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
		// Log.d("mf", "mf");
		// mMagnetic = event.values.clone();
		// }
		if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

			if (0 != rotateValue[0] && rotateget == false) {

				calcvector[0] = rotateValue[0];
				calcvector[1] = rotateValue[1];
				calcvector[2] = rotateValue[2];
				SensorManager.getRotationMatrixFromVector(mRotationMatrix1,calcvector);
//				SensorManager.getQuaternionFromVector(calcvector, rotateValue);
				rotateget = true;
				gyroTimestamp = 0L;
			}
			if (rotateget == false)
				return;
			float[][] deltavector = new float[3][3];
			float[] deltaRotationVector = new float[4];
			if (gyroTimestamp != 0) {
				float dT = (event.timestamp - gyroTimestamp) * 1.0f / 1000000000.0f;
				// Axis of the rotation sample, not normalized yet.
				float axisX = event.values[0];
				float axisY = event.values[1];
				float axisZ = event.values[2];

				// Calculate the angular speed of the sample
				float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY
						* axisY + axisZ * axisZ);

				// Normalize the rotation vector if it's big enough to get
				// the
				// axis
				// (that is, EPSILON should represent your maximum allowable
				// margin of error)
				float EPSILON = (float) 0.00000000000005;
				if (omegaMagnitude > EPSILON) {
					axisX /= omegaMagnitude;
					axisY /= omegaMagnitude;
					axisZ /= omegaMagnitude;
				}

				// Integrate around this axis with the angular speed by the
				// timestep
				// in order to get a delta rotation from this sample over
				// the
				// timestep
				// We will convert this axis-angle representation of the
				// delta
				// rotation
				// into a quaternion before turning it into the rotation
				// matrix.
				float thetaOverTwo = omegaMagnitude * dT / 2.0f;
				float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
				float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
				deltaRotationVector[0] = sinThetaOverTwo * axisX;
				deltaRotationVector[1] = sinThetaOverTwo * axisY;
				deltaRotationVector[2] = sinThetaOverTwo * axisZ;
				deltaRotationVector[3] = cosThetaOverTwo;
//				calcvector[0] = calcvector[0]
//						+ (deltaRotationVector[0] + deltavector[2][0] * 2
//								+ deltavector[1][0] * 2 + deltavector[0][0])
//						/ 6;
//				calcvector[1] = calcvector[1]
//						+ (deltaRotationVector[1] + deltavector[2][1] * 2
//								+ deltavector[1][1] * 2 + deltavector[0][1])
//						/ 6;
//				calcvector[2] = calcvector[2]
//						+ (deltaRotationVector[2] + deltavector[2][2] * 2
//								+ deltavector[1][2] * 2 + deltavector[0][2])
//						/ 6;

				float[] deltaRotationMatrix = new float[16];
				  SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
			    
			    float[] postRotationMatrix=new float[16];
			    
				Matrix.multiplyMM(postRotationMatrix, 0,deltaRotationMatrix , 0, mRotationMatrix1, 0);
				mRotationMatrix1=postRotationMatrix;
				
				mAngle2 = getmangle(mRotationMatrix1);
				
//				float[] worldNorthVector = new float[] { 0, 1, 0, 1 };
//				float[] deviceNorthVector = new float[4];
////				SensorManager.getRotationMatrixFromVector(mRotationMatrix1,
////						calcvector);
//				Matrix.multiplyMV(deviceNorthVector, 0, mRotationMatrix1, 0,
//						worldNorthVector, 0);
//				mAngle2 = (float) (Math.atan(deviceNorthVector[0]
//						/ deviceNorthVector[1]) * 180 / Math.PI);
//				if (deviceNorthVector[1] < 0) {
//					mAngle2 = mAngle2 + 180;
//				} else if (deviceNorthVector[0] < 0) {
//					mAngle2 = mAngle2 + 360;
//				}
//				try {
//					mAngle2Outputstream.writeFloat(mAngle2);
//					for (int i = 0; i < mRotationMatrix1.length; ++i) {
//						myRotationMatrixOutputstream
//								.writeFloat(mRotationMatrix1[i]);
//					}
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				cv2.setDirection((int) mAngle2);
				// FIFO
				deltavector[0][0] = deltavector[1][0];
				deltavector[0][1] = deltavector[1][1];
				deltavector[0][2] = deltavector[1][2]; // first row

				deltavector[1][0] = deltavector[2][0];
				deltavector[1][1] = deltavector[2][1];
				deltavector[1][2] = deltavector[2][2]; // second row

				deltavector[2][0] = deltaRotationVector[0];
				deltavector[2][1] = deltaRotationVector[1];
				deltavector[2][2] = deltaRotationVector[2]; // third row
			}
			gyroTimestamp = event.timestamp;

		}
		// if (mGravity != null && mMagnetic != null) {
		// // Log.d("null",
		// //
		// ""+mGravity[0]+","+mGravity[1]+","+mGravity[2]+","+mMagnetic[0]+","+mMagnetic[1]+","+mMagnetic[2]);
		// SensorManager.getRotationMatrix(mRotationMatrix1, null, mGravity,
		// mMagnetic);
		// float[] worldNorthVector = new float[] { 0, 1, 0, 1 };
		// float[] deviceNorthVector = new float[4];
		// Matrix.multiplyMV(deviceNorthVector, 0, mRotationMatrix1, 0,
		// worldNorthVector, 0);
		// mAngle2 = (float) (Math.atan(deviceNorthVector[0]
		// / deviceNorthVector[1]) * 180 / Math.PI);
		// if (deviceNorthVector[1] < 0) {
		// mAngle2 = mAngle2 + 180;
		// } else if (deviceNorthVector[0] < 0) {
		// mAngle2 = mAngle2 + 360;
		// }
		// try {
		// mAngle2Outputstream.writeFloat(mAngle2);
		// for (int i = 0; i < mRotationMatrix.length; ++i) {
		// myRotationMatrixOutputstream
		// .writeFloat(mRotationMatrix1[i]);
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		return;
	}

	class ClickEvent implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == btnStart) {
				if (isRecording == false) {
					// sensorManager
					// .registerListener(Wave2Direction.this, sensorManager
					// .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					// configAccelerameterRate);
					// prepare the filename
					directoryName = new SimpleDateFormat("MM-dd--HH_mm_ss")
							.format(new Date(System.currentTimeMillis()));
					fulldirectoryName = DirnameHWCphone + "/" + directoryName;
					fullFilenameWave = fulldirectoryName + "/" + filenameWave;
					fullFilenameSensor = fulldirectoryName + "/"
							+ filenameSensor;
					fullFilenameAngle = fulldirectoryName + "/" + filenameAngle;
					fullFilenameMAngle2 = fulldirectoryName + "/"
							+ filenameMAngle2;
					fullFilenameMAngle3 = fulldirectoryName + "/"
							+ filenameMAngle3;
					fullFilenameAMRotationMatrix = fulldirectoryName + "/"
							+ filenameAMRotationMatrix;
					fullFilenameRVRotationMatrix = fulldirectoryName + "/"
							+ filenameRVRotationMatrix;
					fullFilenameRVRotationMatrix1 = fulldirectoryName + "/"
							+ filenameRVRotationMatrix1;
					File destDir = new File(fulldirectoryName);
					if (!destDir.exists()) {
						destDir.mkdirs();
					}
					// record sound wave to file
					try {
						fWaveRecord = new FileOutputStream(fullFilenameWave);
						fBufferWaveRecord = new BufferedOutputStream(
								fWaveRecord);
						Audiodataoutputstream = new DataOutputStream(
								fBufferWaveRecord);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// record acc X,Y,Z to file
					try {
						fSensorRecord = new FileOutputStream(fullFilenameSensor);
						fBufferSensorRecord = new BufferedOutputStream(
								fSensorRecord);
						Sensordataoutputstream = new DataOutputStream(
								fBufferSensorRecord);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// record angles v5 add
					try {
						angleOutputstream = new DataOutputStream(
								new BufferedOutputStream(new FileOutputStream(
										fullFilenameAngle)));
						mAngle2Outputstream = new DataOutputStream(
								new BufferedOutputStream(new FileOutputStream(
										fullFilenameMAngle2)));
						mAngle3Outputstream = new DataOutputStream(
								new BufferedOutputStream(new FileOutputStream(
										fullFilenameMAngle3)));
						myRotationMatrixOutputstream = new DataOutputStream(
								new BufferedOutputStream(new FileOutputStream(
										fullFilenameAMRotationMatrix)));
						rVRotationMatrixOutputstream = new DataOutputStream(
								new BufferedOutputStream(new FileOutputStream(
										fullFilenameRVRotationMatrix)));
						rVRotationMatrix1Outputstream = new DataOutputStream(
								new BufferedOutputStream(new FileOutputStream(
										fullFilenameRVRotationMatrix1)));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// start recording
					// recordStarttime=System.nanoTime();
					isRecording = true;
					new RecordAudioThread().start();// 开一条线程边录边放
					new DrawWaveThread(sfv_soundwave, mPaint).start();
					new BPFThread().start();
					new AGCThread().start();
					new PLLThread().start();
					new ProcessPhase().start();
					btnStart.setText("Already Started, Click to Stop");
				} else {
					// stop everything

					isRecording = false;
					audioRecord.stop();
					iscounting = false;
					// SystemClock.sleep(600);
					// for (int i=0;i<mAudioData.size();i++){
					// short[] tmp=mAudioData.get(i);
					//
					// for (int j=0;j<tmp.length;j++)
					//
					// try {
					//
					// Audiodataoutputstream.writeShort(tmp[j]);
					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// }
					processdataV(timestamp, lacc0, lacc1, lacc2, move);
					try {
						Audiodataoutputstream.close();
						fWaveRecord.close();
						Sensordataoutputstream.close();
						fSensorRecord.close();
						angleOutputstream.close();
						mAngle2Outputstream.close();
						mAngle3Outputstream.close();
						// previous forget to close
						myRotationMatrixOutputstream.close();
						rVRotationMatrixOutputstream.close();
						rVRotationMatrix1Outputstream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mAudioData.clear();
					inBuf.clear();
					// SystemClock.sleep(600);
					btnStart.setText("Already Stopped, Click to Start");
				}
			} else if (v == btnStop) {
				isRecording = false;
				audioRecord.stop();
			} else if (v == btnExit) {
				isRecording = false;
				audioRecord.stop();
				Wave2Direction.this.finish();
			}
		}
	}

	
	class RecordAudioThread extends Thread {

		public void run() {
			// init wavedata
			wavesize = 0;
			wavedata.clear();

			// long datetime=new Date().getTime()*1000000;
			short[] buffer = new short[minbuffersize];
			// audioRecordtime=System.nanoTime();
			audioRecord.startRecording();// 开始录制
//			audioRecordtime = new Timestamp(System.currentTimeMillis())
//					.getTime() * 1000000;

			audioRecordtime = System.nanoTime();
			double[] wavebuffer = new double[buffersize];
			while (isRecording) {
							
				// 从MIC保存数据到缓冲区
				int bufferReadResult = audioRecord.read(buffer, 0,
						minbuffersize);

				if(bufferReadResult < 0)
					continue;
				short[] tmpBuf = new short[bufferReadResult];
				System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
				
				for (int i = 0; i < bufferReadResult; i++) {
					try {
						Audiodataoutputstream.writeShort(tmpBuf[i]);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					wavebuffer[wavesize % buffersize] = tmpBuf[i] / 65536.0;// TODO:
																			// 1.0-->65536.0
					wavesize++;
					if (wavesize % buffersize == 0)
						try {
							//Log.d("wavedata put wavebuffer[0]:", ""+wavebuffer[0]);
							//wavedata.put(wavebuffer);
							wavedata.offer(wavebuffer, threaddelay, TimeUnit.SECONDS);
							wavebuffer = new double[buffersize];
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					// synchronized (wavebuffer) {
					// wavedata.add(wavebuffer);
					// }

				}
				// synchronized(tmpBuf){
				// mAudioData.add(tmpBuf);
				// }
				synchronized (inBuf) {//
					inBuf.add(tmpBuf);// 添加数据
					// inBuf.clear();
				}
				// 写入数据即播放
				// audioTrack.write(tmpBuf, 0, tmpBuf.length);
			}
			// audioTrack.stop();
			audioRecord.stop();

		}

	};

	class DrawWaveThread extends Thread {
		private int oldX = 0;// 上次绘制的X坐标
		private int oldY = 0;// 上次绘制的Y坐标
		private SurfaceView sfv;// 画板
		private int X_index = 0;// 当前画图所在屏幕X轴的坐标
		private Paint mPaint;// 画笔
		private int drawindex;
		private short[] drawbuf;

		public DrawWaveThread(SurfaceView sfv, Paint mPaint) {
			this.sfv = sfv;
			this.mPaint = mPaint;
			drawbuf = new short[2000];
		}

		@SuppressWarnings("unchecked")
		public void run() {
			while (isRecording) {
				int drawlength = 4;
				int length = sfv_soundwave.getWidth() / drawlength;
				drawbuf[drawindex % length] = (short) (-drawpoint * 10);
				drawindex++;
				// SystemClock.sleep(1);
				SimpleDraw(0, drawbuf, rateY, baseLine);
				// ArrayList<short[]> buf = new ArrayList<short[]>();
				// synchronized (inBuf) {
				// if (inBuf.size() == 0)
				// continue;
				// buf = (ArrayList<short[]>) inBuf.clone();// 保存
				// inBuf.clear();// 清除
				// }
				// for (int i = 0; i < buf.size(); i++) {
				// short[] tmpBuf = buf.get(i);
				// if(i%2==0)
				// SimpleDraw(X_index, tmpBuf, rateY, baseLine);// 把缓冲区数据画出来
				// X_index = X_index + tmpBuf.length;
				// if (X_index > sfv.getWidth()) {
				// X_index = 0;
				// }
				// }

				// int i=0;
				// double[] buffer=new double[buffersize];
				// while(theta.size()<=i);
				// buffer=theta.get(i);
				// // tvaccx.setText("a");
				// i++;
			}
		}

		/**
		 * 绘制指定区域
		 * 
		 * @param start
		 *            X轴开始的位置(全屏)
		 * @param buffer
		 *            缓冲区
		 * @param rate
		 *            Y轴数据缩小的比例
		 * @param baseLine
		 *            Y轴基线
		 */
		void SimpleDraw(int start, short[] buffer, int rate, int baseLine) {
			int drawlength = 4;
			baseLine = sfv_soundwave.getHeight() / 2;
			if (start == 0)
				oldX = 0;
			Canvas canvas = sfv.getHolder().lockCanvas(
					new Rect(start, 0, start + buffer.length, sfv.getHeight()));// 关键:获取画布
			canvas.drawColor(Color.BLACK);// 清除背景
			int y;
			for (int i = 0; i < buffer.length; i++) {// 有多少画多少
				// if (i%2!=0)
				// continue;
				int x = i * drawlength + start;
				y = buffer[i] / rate + baseLine;// 调节缩小比例，调节基准线

				canvas.drawLine(oldX, oldY, x, y, mPaint);
				oldX = x;
				oldY = y;
			}
			sfv.getHolder().unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
		}
	}

	public void processdata(ArrayList<Float> timestamp, ArrayList<Float> lacc0,
			ArrayList<Float> lacc1, ArrayList<Float> lacc2,
			ArrayList<Double> move) {
		Log.d("processdata()", "processdata...");
		// 2.以 meanlength=4000个点为一组对move进行分段求均值，得到meanmove数组
		Log.d("move size:", ""+move.size());
		Log.d("move[0]", ""+move.get(0));
		
		int meanmovesize = move.size() / meanlength; // 正整型运算相当于求floor
		Log.d("meanmovesize:", "" + meanmovesize);
		double[] tMeanmove = new double[meanmovesize];
		double[] meanmove = new double[meanmovesize];
		double dt = 0; // 为了使得超声计算出来的加速度和加速度传感器采样的加速度时间对齐而设
		for (int i = 0; i < meanmovesize; ++i) {
			meanmove[i] = mean(move.subList(i * meanlength, (i + 1)
					* meanlength));
			Log.d("meanmove[" + i + "]:", "" + meanmove[i]);
			tMeanmove[i] = (i * meanlength + dt) / frequency;
			Log.d("tmeanmove[" + i + "]:", "" + tMeanmove[i]);
		}
		// 3.对meanmove进行三次样条插值，并求二次导
		CubicSpline cs = new CubicSpline(tMeanmove, meanmove);// !!!自然三次样条插值(首尾节点的二次导为0)
		// 4.以meanmove所对应的时间对二次导函数进行采样,得到asample数组
		double[] asample = cs.getDeriv();
		// 5.对asample数组进行线性插值，得到插值函数
		// CubicSpline cs1 = new CubicSpline(tMeanmove, asample);
		LinearInterpolation ls = new LinearInterpolation(tMeanmove, asample);
		// 6.以加速度传感器的对应的采样时间，对样条函数进行采样，得到加速度数组Y
		int lastindex = 0;
		int firstindex = 0;
		for (int i = 0; i < timestamp.size(); ++i) {
			if (timestamp.get(i) > tMeanmove[0]) {
				firstindex = i;
				break;
			}
		}
		for (int i = timestamp.size() - 1; i >= 0; --i) {
			if (timestamp.get(i) < tMeanmove[tMeanmove.length - 1]) {
				lastindex = i;
				break;
			}
		}
		// 7.建立超声求出的加速度Y和加速度传感器读出的加速度X的函数关系，列出超定线性方程组lambda1*X(1)+lambda2*X(2)=Y并求解
		double[] Y = new double[lastindex + 1 - firstindex];
		double[][] X = new double[lastindex + 1 - firstindex][2];
		for (int i = 0; i < lastindex + 1 - firstindex; ++i) {
			Y[i] = ls.interpolate(timestamp.get(i + firstindex));
			X[i][0] = lacc0.get(i + firstindex);
			X[i][1] = lacc1.get(i + firstindex);
		}
		flanagan.math.Matrix x = new flanagan.math.Matrix(X);
		double[] lambda = x.solveLinearSet(Y);
		float result = (float) (Math.atan(lambda[1] / lambda[0]) * 180 / Math.PI);
		if (lambda[0] < 0) {
			result = result + 180;
			
		}
		if (result < 0) {
			result = result + 360;
		}
		// System.out.println(result);
		float[] worldNorthVector = new float[] {
				(float) Math.cos(result * Math.PI / 180),
				(float) Math.sin(result * Math.PI / 180), 0, 1 };
		float[] deviceNorthVector = new float[4];
		float[] rotationMatrix = new float[16];
		// for a little more safe
		for (int i = 0; i < 16; i++) {
			rotationMatrix[i] = mRotationMatrix[i];
		}
		for (int i = 0; i < 16; i++) {
			try {
				rVRotationMatrix1Outputstream.writeFloat(rotationMatrix[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Matrix.multiplyMV(deviceNorthVector, 0, rotationMatrix, 0,
				worldNorthVector, 0);
		float angle = (float) (Math.atan(deviceNorthVector[0]
				/ deviceNorthVector[1]) * 180 / Math.PI);
		if (deviceNorthVector[1] < 0) {
			angle = angle + 180;
		} else if (deviceNorthVector[0] < 0) {
			angle = angle + 360;
		}
		float result1;
		result1 = mAngle2 - mAngle3 + result;
		// 伪mod
		while (result1 < 0) {
			result1 += 360;
		}
		while (result1 > 360) {
			result1 -= 360;
		}
		try {
			angleOutputstream.writeFloat(result);
			// angleOutputstream.writeFloat(ourresult);
			angleOutputstream.writeFloat(mAngle2);
			angleOutputstream.writeFloat(mAngle3);
			angleOutputstream.writeFloat(result1);
			angleOutputstream.writeFloat(angle);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tvresult.setText("result:" + result + ";mAngle2:" + mAngle2
				+ ";mAngle3:" + mAngle3 + ";result1:" + result1 + ";angle:"
				+ angle);
		arr.setDirection((int) angle);
	}

public void processdataV(ArrayList<Float> timestamp, ArrayList<Float> lacc0,
		ArrayList<Float> lacc1, ArrayList<Float> lacc2,
		ArrayList<Double> move) {
	
	int lacc0Length = lacc0.size();
	double[] arraydouble1 = new double[lacc0Length];
	double[] arraydouble2 = new double[lacc0Length];
	double[] dT = new double[lacc0Length];
	arraydouble1[0] = 0.0;
	arraydouble1[1] = 0.0;
	arraydouble1[2] = 0.0;
	arraydouble2[0] = 0.0;
	arraydouble2[1] = 0.0;
	arraydouble2[2] = 0.0;
	dT[0] = 0.0;
	int j = 1;
	while(j < lacc0Length)
	{
		dT[j] = timestamp.get(j) - timestamp.get(j-1);
		j++;
	}
	
	int k =3;
	while(k < lacc0Length)
	{
		arraydouble1[k] = arraydouble1[k-1] + (lacc0.get(k)*dT[k] + lacc0.get(k-1)*dT[k-1] 
				+ lacc0.get(k-2)*dT[k-2] + lacc0.get(k-3)*dT[k-3])/6.0 ;
		arraydouble2[k] = arraydouble2[k-1] + (lacc1.get(k)*dT[k] + lacc1.get(k-1)*dT[k-1] 
				+ lacc1.get(k-2)*dT[k-2] + lacc1.get(k-3)*dT[k-3])/6.0 ;
		k++;
	}
	
	// 2.以 meanlength=4000个点为一组对move进行分段求均值，得到meanmove数组
	int meanmovesize = move.size() / meanlength; // 正整型运算相当于求floor
//	Log.d("meanmovesize:", "" + meanmovesize);
	double[] tMeanmove = new double[meanmovesize];
	double[] meanmove = new double[meanmovesize];
	double dt = 0; // 为了使得超声计算出来的加速度和加速度传感器采样的加速度时间对齐而设
	for (int i = 0; i < meanmovesize; ++i) {
		meanmove[i] = mean(move.subList(i * meanlength, (i + 1)
				* meanlength));
		tMeanmove[i] = (i * meanlength + dt) / frequency;
	}
	// 3.对meanmove进行三次样条插值，并求二次导
	CubicSpline cs = new CubicSpline(tMeanmove, meanmove);// !!!自然三次样条插值(首尾节点的二次导为0)
	// 4.以meanmove所对应的时间对二次导函数进行采样,得到asample数组
	//double[] asample = cs.getDeriv();
	// 5.对asample数组进行线性插值，得到插值函数
	//CubicSpline cs1 = new CubicSpline(tMeanmove, asample);
	//LinearInterpolation ls = new LinearInterpolation(tMeanmove, asample);
	// 6.以加速度传感器的对应的采样时间，对样条函数进行采样，得到加速度数组Y
	int lastindex = 0;
	int firstindex = 0;
	for (int i = 0; i < timestamp.size(); ++i) {
		if (timestamp.get(i) > tMeanmove[0]) {
			firstindex = i;
			break;
		}
	}
	for (int i = timestamp.size() - 1; i >= 0; --i) {
		if (timestamp.get(i) < tMeanmove[tMeanmove.length - 1]) {
			lastindex = i;
			break;
		}
	}
//	Log.d("first :",""+firstindex);
//	Log.d("lastindex:",	 ""+lastindex);
	
	// 7.建立超声求出的加速度Y和加速度传感器读出的加速度X的函数关系，列出超定线性方程组lambda1*X(1)+lambda2*X(2)=Y并求解
	double[] Y = new double[lastindex + 1 - firstindex];
	//double[][] X = new double[lastindex + 1 - firstindex][2];
	int[] intX = {lastindex + 1 -firstindex , 4 };
	double[][] X = (double[][])Array.newInstance(Double.TYPE, intX);
	
	for (int i = 0; i < lastindex + 1 - firstindex; i++) {		
		Y[i] = cs.interpolate_for_y_and_dydx(timestamp.get(firstindex + i))[1];
		//Log.d("Y["+i+"", ""+Y[i]);
		X[i][0] = arraydouble1[firstindex + i];
		X[i][1] = arraydouble2[firstindex + i];
		X[i][2] = 1.0;
		X[i][3] = i + 1;
		
//		Y[i] = ls.interpolate(timestamp.get(i + firstindex));
//		X[i][0] = lacc0.get(i + firstindex);
//		X[i][1] = lacc1.get(i + firstindex);
	}
	flanagan.math.Matrix x = new flanagan.math.Matrix(X);
	double[] lambda = x.solveLinearSet(Y);
//	Log.d("lambda[0] :",""+lambda[0]);
//	Log.d("lambda[1]:",	 ""+lambda[1]);
	
	float result = (float) (Math.atan2(lambda[1] , lambda[0]) * 180 / Math.PI);
//	if (lambda[1] < 0) {
//		result = result + 180;		
//	}
	if (result < 0) {
		result = result + 360;
	}
	// System.out.println(result);
	float[] worldNorthVector = new float[] {
			(float) Math.cos(result * Math.PI / 180),
			(float) Math.sin(result * Math.PI / 180), 0, 1 };
	float[] deviceNorthVector = new float[4];
	float[] rotationMatrix = new float[16];
	// for a little more safe
	for (int i = 0; i < 16; i++) {
		rotationMatrix[i] = mRotationMatrix[i];
	}
	for (int i = 0; i < 16; i++) {
		try {
			rVRotationMatrix1Outputstream.writeFloat(rotationMatrix[i]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	Matrix.multiplyMV(deviceNorthVector, 0, rotationMatrix, 0,
			worldNorthVector, 0);
	float angle = (float) (Math.atan2(deviceNorthVector[1],deviceNorthVector[0]) * 180 / Math.PI);
//	if (deviceNorthVector[1] < 0) {
//		angle = angle + 180;
//	} else if (deviceNorthVector[0] < 0) {
//		angle = angle + 360;
//	}
	float result1;
	result1 = mAngle2 - mAngle3 + result;
	// 伪mod
	while (result1 < 0) {
		result1 += 360;
	}
	while (result1 > 360) {
		result1 -= 360;
	}
	try {
		angleOutputstream.writeFloat(result);
		// angleOutputstream.writeFloat(ourresult);
		angleOutputstream.writeFloat(mAngle2);
		angleOutputstream.writeFloat(mAngle3);
		angleOutputstream.writeFloat(result1);
		angleOutputstream.writeFloat(angle);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	tvresult.setText("result:" + result + ";mAngle2:" + mAngle2
			+ ";mAngle3:" + mAngle3 + ";result1:" + result1 + ";angle:"
			+ angle);
	arr.setDirection((int) (90 - angle));
   }

}

// fullFilenameAccX=fulldirectoryName+"/"+filenameAccX;
// fullFilenameAccY=fulldirectoryName+"/"+filenameAccY;
// fullFilenameAccZ=fulldirectoryName+"/"+filenameAccZ;