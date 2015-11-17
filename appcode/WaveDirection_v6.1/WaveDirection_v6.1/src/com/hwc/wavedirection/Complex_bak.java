package com.hwc.wavedirection;



/**
 * A complex number, with a real and an imaginary part. (Possibley to be
 * replaced with a class that has better support for complex arithmetic and
 * functions of a complex variable.)
 */
public class Complex_bak {

	public double re, im;

	/**
	 * Create a complex number initially equal to zero
	 */
	public Complex_bak() {
	}

	/**
	 * Create a complex number initially equal to the real number x.
	 */
	public Complex_bak(double x) {
		re = x;
	}

	/**
	 * Create a complex number initially equal to x + iy
	 */
	public Complex_bak(double x, double y) {
		re = x;
		im = y;
	}

	/**
	 * Create a new complex number that is initially equal to a given complex
	 * number.
	 * 
	 * @param c
	 *            The complex number to be copied. If null, it is treated as
	 *            zero.
	 */
	public Complex_bak(Complex_bak c) {
		copy(c);
	}

	public static final Complex_bak ZERO_C = new Complex_bak(0, 0);

	public static final Complex_bak ONE_C = new Complex_bak(1, 0);

	public static final Complex_bak I_C = new Complex_bak(0, 1);

	/**
	 * Returns true if obj is equal to this complex number. If obj is null or is
	 * not of type Complex, the return value is false.
	 */
	public boolean equals(Object obj) {
		try {
			Complex_bak c = (Complex_bak) obj;
			return c.re == re && c.im == im;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Computes the conjugate of a complex number.
	 */
	public Complex_bak conj() {
		return new Complex_bak(re, -im);
	}

	/**
	 * Returns the complex number (r*cos(theta)) + i*(r*sin(theta)).
	 */
	public static Complex_bak polar(double r, double theta) {
		return new Complex_bak(r * Math.cos(theta), r * Math.sin(theta));
	}

	/**
	 * Sets this complex number equal to a copy of a given number.
	 * 
	 * @param c
	 *            The number to be copied; if null, the number is treated as
	 *            zero.
	 */
	public void copy(Complex_bak c) {
		if (c == null)
			re = im = 0;
		else {
			re = c.re;
			im = c.im;
		}
	}

	/**
	 * Returns this + c; c must be non-null.
	 */
	public Complex_bak plus(Complex_bak c) {
		return new Complex_bak(re + c.re, im + c.im);
	}

	/**
	 * Returns this - c; c must be non-null.
	 */
	public Complex_bak minus(Complex_bak c) {
		return new Complex_bak(re - c.re, im - c.im);
	}

	/**
	 * Returns this * c; c must be non-null.
	 */
	public Complex_bak times(Complex_bak c) {
		return new Complex_bak(re * c.re - im * c.im, re * c.im + im * c.re);
	}

	/**
	 * Returns this / c; c must be non-null.
	 */
	public Complex_bak dividedBy(Complex_bak c) {
		double denom = c.re * c.re + c.im * c.im;
		if (denom == 0)
			return new Complex_bak(Double.NaN, Double.NaN);
		else
			return new Complex_bak((re * c.re + im * c.im) / denom, (im * c.re - re
					* c.im)
					/ denom);
	}

	public Complex_bak times(double x) {
		return new Complex_bak(re * x, im * x);
	}

	public Complex_bak plus(double x) {
		return new Complex_bak(re + x, im);
	}

	public Complex_bak minus(double x) {
		return new Complex_bak(re - x, im);
	}

	public Complex_bak dividedBy(double x) {
		return new Complex_bak(re / x, im / x);
	}

	/**
	 * Returns the absolute value squared of this.
	 * 
	 * @return real part squared plus imaginary part squared
	 */
	public double abs2() {
		return (re * re + im * im);
	}

	/**
	 * Returns the absolute value, "r" in polar coordinates, of this.
	 * 
	 * @return the square root of (real part squared plus imaginary part
	 *         squared)
	 */
	public double r() {
		return Math.sqrt(re * re + im * im);
	}

	/**
	 * Returns arg(this), the angular polar coordinate of this complex number,
	 * in the range -pi to pi. The return value is simply Math.atan2(imaginary
	 * part, real part).
	 */
	public double theta() {
		return Math.atan2(im, re);
	}

	/**
	 * Computes the complex exponential function, e^z, where z is this complex
	 * number.
	 */
	public Complex_bak exponential() {
		double length = Math.exp(re);
		return new Complex_bak(length * Math.cos(im), length * Math.sin(im));
	}

	/**
	 * Computes the complex reciprocal function, 1/z, where z is this complex
	 * number.
	 */
	public Complex_bak inverse() {
		double length = re * re + im * im;
		return new Complex_bak(re / length, -im / length);
	}

	public Complex_bak log() {
		double modulus = Math.sqrt(re * re + im * im);
		double arg = Math.atan2(im, re);
		return new Complex_bak(Math.log(modulus), arg);
	}

	/**
	 * Computes that complex logarithm of this complex number that is nearest to
	 * previous. A test code is in fractals.TestAnalyticContinuation.
	 */
	public Complex_bak logNearer(Complex_bak previous) {
		Complex_bak c = new Complex_bak(this.log());
		double h = (c.im - previous.im) / (2 * Math.PI);
		double d = (2 * Math.PI) * Math.floor(h + 0.5);
		c.im = c.im - d;
		return c;
	}

	public double sinh(double x) {
		return (Math.exp(x) - Math.exp(-x)) / 2;
	}

	public double cosh(double x) {
		return (Math.exp(x) + Math.exp(-x)) / 2;
	}

	public Complex_bak sine() {
		double x, y;
		Complex_bak z = new Complex_bak(0.0, 0.0);
		x = re;
		y = im;
		z.re = Math.sin(x) * cosh(y);
		z.im = Math.cos(x) * sinh(y);
		return z;
	}

	public Complex_bak power(double x) {
		double modulus = Math.sqrt(re * re + im * im);
		double arg = Math.atan2(im, re);
		double log_re = Math.log(modulus);
		double log_im = arg;
		double x_log_re = x * log_re;
		double x_log_im = x * log_im;
		double modulus_ans = Math.exp(x_log_re);
		return new Complex_bak(modulus_ans * Math.cos(x_log_im), modulus_ans
				* Math.sin(x_log_im));
	}

	/**
	 * Returns a complex k-th root of this complex number. The root that is
	 * returned is the one with the smallest positive arg. (If k is 0, the
	 * return value is 1. If k is negative, the value is 1/integerRoot(-k).)
	 */
	public Complex_bak integerRoot(int k) {
		double a, b;
		boolean neg = false;
		if (k < 0) {
			k = -k;
			neg = true;
		}
		if (k == 0) {
			a = 1;
			b = 0;
		} else if (k == 1) {
			a = re;
			b = im;
		} else {
			double length = r();
			double angle = theta();
			if (angle < 0)
				angle += Math.PI * 2;
			length = Math.pow(length, 1.0 / k);
			angle = angle / k;
			a = length * Math.cos(angle);
			b = length * Math.sin(angle);
		}
		if (neg) {
			double denom = a * a + b * b;
			a = a / denom;
			b = -b / denom;
		}
		return new Complex_bak(a, b);
	}

	/**
	 * Computes that square root of this complex number that is nearer to
	 * previous than to minus previous. A test code is in
	 * fractals.TestAnalyticContinuation.
	 */
	public Complex_bak squareRootNearer(Complex_bak previous) {
		Complex_bak c;
		c = this.integerRoot(2);
		if (c.re * previous.re + c.im * previous.im < 0) {
			c.re = -c.re;
			c.im = -c.im;
		}
		return new Complex_bak(c.re, c.im);
	}

	public double[] stereographicProjection() {

		double rsquare, rsquarePlusOne;
		double[] projPoint = new double[3];
		rsquare = re * re + im * im;
		rsquarePlusOne = rsquare + 1;
		projPoint[0] = (2 * re) / rsquarePlusOne;
		projPoint[1] = (2 * im) / rsquarePlusOne;
		projPoint[2] = (rsquare - 1) / rsquarePlusOne;
		return projPoint;
	}
}
