package com.hwc.wavedirection;



import java.math.BigInteger;

import flanagan.complex.Complex;
import flanagan.complex.ComplexPoly;

public class FilterDesign {
	/**
	 * 参照matlab和scipy的buttrord向Java的一个移植
	 * <p>
	 * buttrord目前只移植了passband filter的参数获取<br>
	 * buttrord 方法的详细说明第二行
	 * 
	 * @param wpl
	 *            ,wph,wsl,wsh Passband and stopband edge frequencies,
	 *            normalized from 0 to 1 (1 corresponds to pi radians / sample).
	 *            For example: - Bandpass: wp = [0.2, 0.5], ws = [0.1, 0.6] wp =
	 *            [wpl, wph], ws = [wsl, wsh]
	 * @param gpass
	 *            The maximum loss in the passband (dB).
	 * @param gstop
	 *            The minimum attenuation in the stopband (dB).
	 * @return a instance of @see ButtordResult
	 */
	ButtordResult buttord(double wpl, double wph, double wsl, double wsh,
			double gpass, double gstop) {
		// Pre-warp frequencies
		double passbl = Math.tan(wpl * Math.PI / 2.0);
		double passbh = Math.tan(wph * Math.PI / 2.0);
		double stopbl = Math.tan(wsl * Math.PI / 2.0);
		double stopbh = Math.tan(wsh * Math.PI / 2.0);
		double natl = (stopbl * stopbl - passbl * passbh)
				/ (stopbl * (passbl - passbh));
		double nath = (stopbh * stopbh - passbl * passbh)
				/ (stopbh * (passbl - passbh));
		double nat = Math.abs(natl) < Math.abs(nath) ? Math.abs(natl) : Math
				.abs(nath);
		double GSTOP = Math.pow(10, 0.1 * Math.abs(gstop));
		double GPASS = Math.pow(10, 0.1 * Math.abs(gpass));
		int ord = (int) (Math.ceil(Math.log10((GSTOP - 1.0) / (GPASS - 1.0))
				/ (2 * Math.log10(nat))));
		double W0 = nat
				/ Math.pow((Math.pow(10, 0.1 * Math.abs(gstop)) - 1),
						(1.0 / (2.0 * ord)));
		double W0l = -W0;
		double W0h = W0;
		double WNl = (-W0l * (passbh - passbl) / 2.0 + Math.sqrt(W0l * W0l
				/ 4.0 * (passbh - passbl) * (passbh - passbl)
				+ (passbl * passbh)));
		double WNh = (-W0h * (passbh - passbl) / 2.0 + Math.sqrt(W0h * W0h
				/ 4.0 * (passbh - passbl) * (passbh - passbl)
				+ (passbl * passbh)));
		double wnl = (2.0 / Math.PI)
				* Math.atan(Math.abs(WNl) < Math.abs(WNh) ? Math.abs(WNl)
						: Math.abs(WNh));
		double wnh = (2.0 / Math.PI)
				* Math.atan(Math.abs(WNl) > Math.abs(WNh) ? Math.abs(WNl)
						: Math.abs(WNh));
		ButtordResult br = new ButtordResult();
		br.n = ord;
		br.wnl = wnl;
		br.wnh = wnh;
		return br;
	}

	BiResult butter(int n, double wnl, double wnh) {
		double fs = 2.0;
		double warpedh = 2 * fs * Math.tan(Math.PI * wnh / fs);
		double warpedl = 2 * fs * Math.tan(Math.PI * wnl / fs);
		double bw = warpedh - warpedl;
		double wo = Math.sqrt(warpedl * warpedh);
		// z, p, k = buttap(n);
		//Complex[] z = null; // danger!
		Complex[] p = new Complex[n];
		for (int i = 0; i < n; ++i) {
			Complex _1j = new Complex(0.0, 1.0);
			// exp(1j * (2 * n - 1) / (2.0 * N) * pi) *
			// 1j;由于i从0开始，所以2*n-1改成2*i+1
			p[i] = (_1j.times((2 * i + 1) / (2.0 * n) * Math.PI).exp())
					.times(_1j);
		}
		//double k = 1;
		// b, a = zpk2tf(z, p, k)/////////////
		Complex[] b1 = new Complex[1]; // b = k * ploy(z);
		b1[0] = new Complex(1.0, 0.0);
		// 和scipy的ploy(p)生成的多项式，实部相同，但虚部不同
		ComplexPoly a1 = ComplexPoly.rootsToPoly(p); // a = atleast_1d(poly(p));
		// b, a = lp2bp(b, a, wo=wo, bw=bw)//////////////
		LpResult lr = lp2bp(b1, a1.polyNomCopy(), wo, bw);
		// b, a = bilinear(b, a, fs=fs)
		BiResult br = bilinear(lr.b, lr.a, fs);
		return br;
	}

	LpResult lp2bp(Complex[] b, Complex[] a, double wo, double bw) {
		int D = a.length - 1;
		int N = b.length - 1;
		int ma = D > N ? D : N;
		int Np = N + ma;
		int Dp = D + ma;
		Complex[] bprime = new Complex[Np + 1];
		Complex[] aprime = new Complex[Dp + 1];
		double wosq = wo * wo;
		for (int j = 0; j < Np + 1; ++j) {
			Complex val = new Complex(0.0);
			for (int i = 0; i < N + 1; ++i) {
				for (int k = 0; k < i + 1; ++k) {
					if (ma - i + 2 * k == j) {
						val = b[N - i].times(
								comb(i, k).doubleValue()
										* Math.pow(wosq, i - k)
										/ Math.pow(bw, i)).plus(val);
					}
				}
			}
			bprime[Np - j] = val;
		}
		for (int j = 0; j < Dp + 1; ++j) {
			Complex val = new Complex(0.0);
			for (int i = 0; i < D + 1; ++i) {
				for (int k = 0; k < i + 1; ++k) {
					if (ma - i + 2 * k == j) {
						val = a[D - i].times(
								comb(i, k).doubleValue()
										* Math.pow(wosq, i - k)
										/ Math.pow(bw, i)).plus(val);
					}
				}
			}
			aprime[Dp - j] = val;
		}
		// return normalize(bprime, aprime)
		LpResult lr = new LpResult();
		lr.b = bprime;// 浅拷贝不知道会不会出问题
		lr.a = aprime;
		return lr;
	}

	BiResult bilinear(Complex[] b, Complex[] a, double fs) {
		int D = a.length - 1;
		int N = b.length - 1;
		int ma = D > N ? D : N;
		int Np = ma;
		int Dp = ma;
		double[] bprime = new double[Np + 1];
		double[] aprime = new double[Dp + 1];
		for (int j = 0; j < Np + 1; ++j) {
			Complex val = new Complex(0.0);
			for (int i = 0; i < N + 1; ++i) {
				for (int k = 0; k < i + 1; ++k) {
					for (int l = 0; l < ma - i + 1; ++l) {
						if (k + l == j) {
							val = b[N - i].times(
									comb(i, k).doubleValue()
											* comb(ma - i, l).doubleValue()
											* Math.pow(2 * fs, i)
											* Math.pow(-1, k)).plus(val);
						}
					}
				}
			}
			bprime[j] = val.getReal();
		}
		for (int j = 0; j < Dp + 1; ++j) {
			Complex val = new Complex(0.0);
			for (int i = 0; i < D + 1; ++i) {
				for (int k = 0; k < i + 1; ++k) {
					for (int l = 0; l < ma - i + 1; ++l) {
						if (k + l == j) {
							val = a[D - i].times(
									comb(i, k).doubleValue()
											* comb(ma - i, l).doubleValue()
											* Math.pow(2 * fs, i)
											* Math.pow(-1, k)).plus(val);
						}
					}
				}
			}
			aprime[j] = val.getReal();
		}
		BiResult br = new BiResult();
		br.b = bprime; // 浅拷贝不知道会不会出问题
		br.a = aprime;
		return br;
		// return normalize(bprime, aprime)
	}

	// combination BigInteger version,21! > Long.MAX_VALUE;
	// 13! >Integer.MAX_VALUE
	BigInteger comb(final int N, final int K) {
		BigInteger ret = BigInteger.ONE;
		for (int k = 0; k < K; k++) {
			ret = ret.multiply(BigInteger.valueOf(N - k)).divide(
					BigInteger.valueOf(k + 1));
		}
		return ret;
	}
}
