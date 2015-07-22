package com.hwc.wavedirection;



public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double wpl = 18000 / 22050.0;
		double wph = 20000 / 22050.0;
		double wsl = 17000 / 22050.0;
		double wsh = 21000 / 22050.0;
		double gpass = 10;
		double gstop = 40;
		FilterDesign fd = new FilterDesign();
		ButtordResult br = new ButtordResult();
		br = fd.buttord(wpl, wph, wsl, wsh, gpass, gstop);
		BiResult bir = fd.butter(br.n,br.wnl,br.wnh);
	}

}
