package cviceni2;

import Jama.Matrix;
import ij.ImagePlus;

public class Process
{

	private ImagePlus imageP;
	private ColorTransform colorT;
	private ColorTransform colorOrig;

	public static final int S411 = 10;
	public static final int S420 = 9;
	public static final int S422 = 8;
	public static final int S444 = 7;

	public Process (ImagePlus imagePlus)
	{
		this.imageP = imagePlus;
		this.imageP.setTitle("Original");
		this.colorT = new ColorTransform(imagePlus.getBufferedImage());
		this.colorOrig = new ColorTransform(imagePlus.getBufferedImage());
	}


	public void showColor(String barva)
	{
		colorT.getComponent(barva).show();
	}

	public void showSlozka(String slozka)
	{
		switch (slozka) {
		case "Y":
			colorT.getComponent(colorT.getY(),"Y").show();
			break;
		case "Cb":
			colorT.getComponent(colorT.getcB(),"Cb").show();
			break;
		case "Cr":
			colorT.getComponent(colorT.getcR(),"Cr").show();
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + slozka);
		}
	}
	
	public void transformation(int tranType) {
		switch (tranType) {
		case 1:
			this.colorT = new ColorTransform(imageP.getBufferedImage());
			//colorT.bigTranformation(4);
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
			break;
		
		case 2:
			this.colorT = new ColorTransform(imageP.getBufferedImage());
			//colorT.bigTranformation(8);
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
			break;
			
		case 3:
			this.colorT = new ColorTransform(imageP.getBufferedImage());
			//colorT.bigTranformation(16);
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
			break;
			
		case 4:
			colorT.bigDetransformation(4);
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
			break;
			
		case 5:
			colorT.bigDetransformation(8);
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
			break;
			
		case 6:
			colorT.bigDetransformation(16);
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
			break;
		
		case 7:
			this.colorT = new ColorTransform(imageP.getBufferedImage());
	//		colorT.bigTranformation(8);
			colorT.bigDetransformation(8);
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
			break;

		default:
			break;
		}
	}
	
	public void all(int q) {
		this.colorT = new ColorTransform(imageP.getBufferedImage());
				colorT.bigTranformation(8, q);
				colorT.bigDetransformation(8);
				colorT.fromYCBCR();
				colorT.setImageFromRGB().show();
	}


	public void downsample(int downsampleType)
	{
		switch (downsampleType) {
		case 7:{
			this.colorT = new ColorTransform(imageP.getBufferedImage());
			showSlozka("Y");
			showSlozka("Cb");
			showSlozka("Cr");
			colorT.transform(512, colorT.getDctMatrix(512), colorT.getY());
			colorT.transform(512, colorT.getDctMatrix(512), colorT.getcB());
			colorT.transform(512, colorT.getDctMatrix(512), colorT.getcR());
			colorT.inverseTransform(512, colorT.getDctMatrix(512), colorT.getY());
			colorT.inverseTransform(512, colorT.getDctMatrix(512), colorT.getcR());
			colorT.inverseTransform(512, colorT.getDctMatrix(512), colorT.getcB());
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
		}
			break;
		case 8:{
			this.colorT = new ColorTransform(imageP.getBufferedImage());
			Matrix cB = new Matrix(colorT.getcB().getArray());
			Matrix cR = new Matrix(colorT.getcR().getArray());
			colorT.setcB(colorT.downSample(cB));
			colorT.setcR(colorT.downSample(cR));
			showSlozka("Y");
			showSlozka("Cb");
			showSlozka("Cr");
		}
			break;
		case 9:{
			this.colorT = new ColorTransform(imageP.getBufferedImage());
			Matrix cB = new Matrix(colorT.getcB().getArray());
			Matrix cR = new Matrix(colorT.getcR().getArray());
			cB=colorT.downSample(cB);
			cB=cB.transpose();
			cB=colorT.downSample(cB);
			cB=cB.transpose();
			cR=colorT.downSample(cR);
			cR=cR.transpose();
			cR=colorT.downSample(cR);
			cR=cR.transpose();
			colorT.setcB(cB);
			colorT.setcR(cR);
			showSlozka("Y");
			showSlozka("Cb");
			showSlozka("Cr");

		}
			break;
		case 10:{
			this.colorT = new ColorTransform(imageP.getBufferedImage());
			Matrix cB = new Matrix(colorT.getcB().getArray());
			Matrix cR = new Matrix(colorT.getcR().getArray());
			cB=colorT.downSample(cB);
			cR=colorT.downSample(cR);
			colorT.setcB(colorT.downSample(cB));
			colorT.setcR(colorT.downSample(cR));
			showSlozka("Y");
			showSlozka("Cb");
			showSlozka("Cr");
		}
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + downsampleType);
		}

	}


	public void oversample()
	{
		int downsampleType;
		int refw = colorT.getY().getColumnDimension();
		int refh = colorT.getY().getRowDimension();
		int width = colorT.getcB().getColumnDimension();
		int height = colorT.getcB().getRowDimension();


		if (refw/width==4) downsampleType =10;
		else if(refh/height==2) downsampleType = 9;
		else if(refw/width==2) downsampleType = 8;
		else downsampleType = 7;

		System.err.println(downsampleType);

		switch (downsampleType) {
		case 7:{
			showSlozka("Y");
			showSlozka("Cb");
			showSlozka("Cr");
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
		}
			break;
		case 8:{
			Matrix cB = new Matrix(colorT.getcB().getArray());
			Matrix cR = new Matrix(colorT.getcR().getArray());
			colorT.setcB(colorT.overSample(cB));
			colorT.setcR(colorT.overSample(cR));
			showSlozka("Y");
			showSlozka("Cb");
			showSlozka("Cr");
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
		}
			break;
		case 9:{
			Matrix cB = new Matrix(colorT.getcB().getArray());
			Matrix cR = new Matrix(colorT.getcR().getArray());
			cB=colorT.overSample(cB);
			cB=cB.transpose();
			cB=colorT.overSample(cB);
			cB=cB.transpose();
			cR=colorT.overSample(cR);
			cR=cR.transpose();
			cR=colorT.overSample(cR);
			cR=cR.transpose();
			colorT.setcB(cB);
			colorT.setcR(cR);
			showSlozka("Y");
			showSlozka("Cb");
			showSlozka("Cr");
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
		}
			break;
		case 10:{
			Matrix cB = new Matrix(colorT.getcB().getArray());
			Matrix cR = new Matrix(colorT.getcR().getArray());
			cB=colorT.overSample(cB);
			cR=colorT.overSample(cR);
			colorT.setcB(colorT.overSample(cB));
			colorT.setcR(colorT.overSample(cR));
			showSlozka("Y");
			showSlozka("Cb");
			showSlozka("Cr");
			colorT.fromYCBCR();
			colorT.setImageFromRGB().show();
		}
			break;

		default:
			throw new IllegalArgumentException("Unexpected value: " + downsampleType);
		}

	}



	public double calculateMSE() {
		Quality mse=new Quality();

		return mse.getMse(colorOrig.returnRGB(), colorT.returnRGB());
	}

	public double calculatePsnr() {
		Quality Psnr=new Quality();

		return Psnr.getPsnr(colorOrig.returnRGB(), colorT.returnRGB());
	}

	public void test1()
	{
		colorT.toYCBCR();
		colorT.fromYCBCR();
		colorT.setImageFromRGB().show();
	}


}
