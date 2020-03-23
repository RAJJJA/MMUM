package cviceni2;

public class Quality {
	private double mse;
	private double psnr;



	public Quality() {
		super();
		this.mse = 0;
		this.psnr = 0;
	}



	public double getMse (int[][] original, int[][] edited) {

		int width = original.length;
		int height = original[0].length;

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
			mse += Math.pow((original[i][j]-edited[i][j]),2);
			}
		}
		
		mse = mse/(double)(width*height);
		
		return mse;
	}



	public double getPsnr (int[][] original, int[][] edited) {

		System.out.println(getMse(original, edited));
		psnr= 10.0 * Math.log10( Math.pow( (Math.pow(2, 8)-1), 2) / getMse(original, edited));
		System.out.println(psnr);
		return psnr;
	}









}
