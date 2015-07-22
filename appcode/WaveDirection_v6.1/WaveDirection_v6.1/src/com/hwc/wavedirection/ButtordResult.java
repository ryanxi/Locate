package com.hwc.wavedirection;



public class ButtordResult {
	int n; // The lowest order for a Butterworth filter which meets specs.
	// ��ͨ�˲�����wn�϶��ǰ�����������
	double wnl;// The Butterworth natural frequency (i.e. the
				// "3dB frequency"). Should be used with `butter` to give
				// filter results.
	double wnh;
}
