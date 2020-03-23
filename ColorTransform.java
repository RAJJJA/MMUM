package cviceni2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import Jama.Matrix;
import ij.ImagePlus;

public class ColorTransform {

	private BufferedImage bImage;
	private ColorModel colormModel;

	private int[][] red;
	private int[][] green;
	private int[][] blue;

	private Matrix y;
	private Matrix cB;
	private Matrix cR;

	private int width;
	private int height;

	public static final double[][] quantizationMatrix8Y = { { 16, 11, 10, 16, 24, 40, 51, 61 },
			{ 12, 12, 14, 19, 26, 58, 60, 55 }, { 14, 13, 16, 24, 40, 57, 69, 56 }, { 14, 17, 22, 29, 51, 87, 80, 62 },
			{ 18, 22, 37, 56, 68, 109, 103, 77 }, { 24, 35, 55, 64, 81, 104, 113, 92 },
			{ 49, 64, 78, 87, 103, 121, 120, 101 }, { 72, 92, 95, 98, 112, 100, 103, 99 } };

	public double[][] getQuantizationMatrix8Y() {
		return quantizationMatrix8Y;
	}

	public static final double[][] quantizationMatrix8C = { { 17, 18, 24, 47, 99, 99, 99, 99 },
			{ 18, 21, 26, 66, 99, 99, 99, 99 }, { 24, 26, 56, 99, 99, 99, 99, 99 }, { 47, 66, 99, 99, 99, 99, 99, 99 },
			{ 99, 99, 99, 99, 99, 99, 99, 99 }, { 99, 99, 99, 99, 99, 99, 99, 99 }, { 99, 99, 99, 99, 99, 99, 99, 99 },
			{ 99, 99, 99, 99, 99, 99, 99, 99 } };
	
	public double[][] getQuantizationMatrix8C() {
		return quantizationMatrix8C;
	}
	
	public Matrix quantMatrix8Y;
	public Matrix quantMatrix8C;

	public ColorTransform(BufferedImage bImage) {
		this.bImage = bImage;

		this.width = bImage.getWidth();
		this.height = bImage.getHeight();

		this.colormModel = bImage.getColorModel();

		this.red = new int[this.height][this.width];
		this.green = new int[this.height][this.width];
		this.blue = new int[this.height][this.width];

		this.y = new Matrix(this.height, this.width);
		this.cB = new Matrix(this.height, this.width);
		this.cR = new Matrix(this.height, this.width);

		getRGB();
		toYCBCR();
		
		setQuantMatrix();
		//editQuantMatrix(1);
		// bigTranformation(8);

		// getDctMatrix(8);
	}

	private void getRGB() {
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				red[i][j] = colormModel.getRed(this.bImage.getRGB(j, i));
				green[i][j] = colormModel.getGreen(this.bImage.getRGB(j, i));
				blue[i][j] = colormModel.getBlue(this.bImage.getRGB(j, i));
			}
		}
	}

	public ImagePlus setImageFromRGB() {
		BufferedImage bImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		int[][] rgb = new int[this.height][this.width];

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				rgb[i][j] = new Color(this.red[i][j], this.green[i][j], this.blue[i][j]).getRGB();
				bImage.setRGB(j, i, rgb[i][j]);
			}
		}
		return (new ImagePlus("New image", bImage));
	}

	public int[][] returnRGB() {
		int[][] rgb = new int[this.height][this.width];

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				rgb[i][j] = new Color(this.red[i][j], this.green[i][j], this.blue[i][j]).getRGB();
			}
		}
		return (rgb);
	}

	public ImagePlus getComponent(String barva) {

		BufferedImage bImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		int[][] rgb = new int[this.height][this.width];

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {

				switch (barva) {
				case "Red": {
					rgb[i][j] = new Color(this.red[i][j], 0, 0).getRGB();
					bImage.setRGB(j, i, rgb[i][j]);
				}
					break;

				case "Green": {
					rgb[i][j] = new Color(0, this.green[i][j], 0).getRGB();
					bImage.setRGB(j, i, rgb[i][j]);
				}
					break;

				case "Blue": {
					rgb[i][j] = new Color(0, 0, this.blue[i][j]).getRGB();
					bImage.setRGB(j, i, rgb[i][j]);
				}
					break;

				default:

					break;
				}

			}
		}

		return (new ImagePlus(barva, bImage));
	}

	public ImagePlus getComponent(Matrix x, String name) {
		int width = x.getColumnDimension();
		int height = x.getRowDimension();

		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[][] rgb = new int[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				rgb[i][j] = new Color((int) (x.get(i, j)), (int) (x.get(i, j)), (int) (x.get(i, j))).getRGB();
				bImage.setRGB(j, i, rgb[i][j]);
			}
		}
		return (new ImagePlus(name, bImage));
	}

	public Matrix downSample(Matrix mat) {
		Matrix sample = new Matrix(mat.getRowDimension(), mat.getColumnDimension() / 2);
		int k = 0;

		for (int i = 0; i < sample.getRowDimension(); i++) {
			for (int j = 0; j < sample.getColumnDimension(); j++) {
				sample.set(i, j, mat.get(i, k));
				if (k + 2 < mat.getColumnDimension())
					k += 2;
			}
			k = 0;
		}
		return sample;
	}

	public Matrix overSample(Matrix mat) {
		Matrix sample = new Matrix(mat.getRowDimension(), mat.getColumnDimension() * 2);
		double k = 0;

		for (int i = 0; i < sample.getRowDimension(); i++) {
			for (int j = 0; j < sample.getColumnDimension(); j++) {
				sample.set(i, j, mat.get(i, (int) k));
				if (k < mat.getColumnDimension())
					k += 0.5;
			}
			k = 0;
		}
		return sample;
	}

	public void toYCBCR() {
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				y.set(i, j, (0.257 * red[i][j] + 0.504 * green[i][j] + 0.098 * blue[i][j] + 16));
				cB.set(i, j, (-0.148 * red[i][j] - 0.291 * green[i][j] + 0.439 * blue[i][j] + 128));
				cR.set(i, j, (0.439 * red[i][j] - 0.368 * green[i][j] - 0.071 * blue[i][j] + 128));
			}
		}
	}

	public void fromYCBCR() {
		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				red[i][j] = (int) Math.round(1.164 * (y.get(i, j) - 16) + 1.596 * (cR.get(i, j) - 128));
				if (red[i][j] > 255) {
					red[i][j] = 255;
				}
				if (red[i][j] < 0) {
					red[i][j] = 0;
				}
				green[i][j] = (int) Math.round(
						1.164 * (y.get(i, j) - 16) - 0.813 * (cR.get(i, j) - 128) - 0.391 * (cB.get(i, j) - 128));
				if (green[i][j] > 255) {
					green[i][j] = 255;
				}
				if (green[i][j] < 0) {
					green[i][j] = 0;
				}
				blue[i][j] = (int) Math.round(1.164 * (y.get(i, j) - 16) + 2.018 * (cB.get(i, j) - 128));
				if (blue[i][j] > 255) {
					blue[i][j] = 255;
				}
				if (blue[i][j] < 0) {
					blue[i][j] = 0;
				}
			}
		}
	}

	public Matrix transform(int size, Matrix transformMatrix, Matrix inputMatrix) {
		Matrix transformed = new Matrix(size, size);
		transformed = transformMatrix.times(inputMatrix);
		transformed = transformed.times(transformMatrix.transpose());
		return transformed;
	}

	public Matrix inverseTransform(int size, Matrix transformMatrix, Matrix inputMatrix) {
		Matrix original = new Matrix(size, size);
		original = transformMatrix.transpose().times(inputMatrix);
		original = original.times(transformMatrix);
		return original;
	}

	public Matrix getDctMatrix(int size) {
		Matrix dctMatrix = new Matrix(size, size);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (i == 0) {
					dctMatrix.set(i, j, calculateDCT(i, j, size, true));
				} else {
					dctMatrix.set(i, j, calculateDCT(i, j, size, false));
				}
			}
		}
		// dctMatrix.print(2, 2);
		return dctMatrix;
	}

	private double calculateDCT(int i, int j, int N, boolean first) {
		double result = 0;
		if (first) {
			result = Math.sqrt(1.0 / N) * Math.cos(((2 * j + 1) * i * Math.PI) / (2 * N));
		} else {
			result = Math.sqrt(2.0 / N) * Math.cos(((2 * j + 1) * i * Math.PI) / (2 * N));
		}

		return result;
	}

	public void bigTranformation(int size, int q) {
		editQuantMatrix(q);
		Matrix Yblock = new Matrix(size, size);
		Matrix cBblock = new Matrix(size, size);
		Matrix cRblock = new Matrix(size, size);
		for (int k = 0; k < this.width / size; k++) {
			for (int l = 0; l < this.height / size; l++) {
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						Yblock.set(i, j, y.get((size * k) + i, (size * l) + j));						
						cBblock.set(i, j, cB.get((size * k) + i, (size * l) + j));
						cRblock.set(i, j, cR.get((size * k) + i, (size * l) + j));						
					}
				}
				Yblock = transform(size, getDctMatrix(size), Yblock);
				Yblock = Yblock.arrayRightDivide(quantMatrix8Y);
				// Yblock = inverseTransform(size, getDctMatrix(size), Yblock);
				cBblock = transform(size, getDctMatrix(size), cBblock);
				cBblock = cBblock.arrayRightDivide(quantMatrix8C);
				// cBblock = inverseTransform(size, getDctMatrix(size), cBblock);
				cRblock = transform(size, getDctMatrix(size), cRblock);
				cRblock = cRblock.arrayRightDivide(quantMatrix8C);
				// cRblock = inverseTransform(size, getDctMatrix(size), cRblock);
				// Yblock.print(2, 2);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						Yblock.set(i, j, (int) Yblock.get(i, j));
						cBblock.set(i, j, (int) cBblock.get(i, j));
						cRblock.set(i, j, (int) cRblock.get(i, j));
					}
				}
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						this.y.set((size * k) + i, (size * l) + j, Yblock.get(i, j));
						this.cB.set((size * k) + i, (size * l) + j, cBblock.get(i, j));
						this.cR.set((size * k) + i, (size * l) + j, cRblock.get(i, j));
					}
				}
			}
		}
	}

	public void bigDetransformation(int size) {
		Matrix Yblock = new Matrix(size, size);
		Matrix cBblock = new Matrix(size, size);
		Matrix cRblock = new Matrix(size, size);
		for (int k = 0; k < this.height / size; k++) {
			for (int l = 0; l < this.width / size; l++) {
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						Yblock.set(i, j, y.get((size * k) + i, (size * l) + j));
						cBblock.set(i, j, cB.get((size * k) + i, (size * l) + j));
						cRblock.set(i, j, cR.get((size * k) + i, (size * l) + j));
					}
				}
				// Yblock = transform(size, getDctMatrix(size), Yblock);
				Yblock = Yblock.arrayTimes(quantMatrix8Y);
				Yblock = inverseTransform(size, getDctMatrix(size), Yblock);
				// cBblock = transform(size, getDctMatrix(size), cBblock);
				cBblock = cBblock.arrayTimes(quantMatrix8C);
				cBblock = inverseTransform(size, getDctMatrix(size), cBblock);
				// cRblock = transform(size, getDctMatrix(size), cRblock);
				cRblock = cRblock.arrayTimes(quantMatrix8C);
				cRblock = inverseTransform(size, getDctMatrix(size), cRblock);
				// block.print(2, 2);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						this.y.set((size * k) + i, (size * l) + j, Yblock.get(i, j));
						this.cB.set((size * k) + i, (size * l) + j, cBblock.get(i, j));
						this.cR.set((size * k) + i, (size * l) + j, cRblock.get(i, j));
					}
				}
			}
		}
	}
	
	public void setQuantMatrix() {
		quantMatrix8C = new Matrix(8, 8);
		quantMatrix8Y = new Matrix(8, 8);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				this.quantMatrix8C.set(i, j, getQuantizationMatrix8C()[i][j]);
				this.quantMatrix8Y.set(i, j, getQuantizationMatrix8Y()[i][j]);
			}
		}
	}
	
	public void editQuantMatrix(int q) {
		int a = 0;
		if (q <= 50) {
			a = 50 / q;
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					this.quantMatrix8C.set(i, j, quantMatrix8C.get(i, j) * a);
					this.quantMatrix8Y.set(i, j, quantMatrix8Y.get(i, j) * a);
				}
			}
		}
		if (q > 50 && q < 100) {
			a = 2 - ((2 * q) / 100);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					this.quantMatrix8C.set(i, j, quantMatrix8C.get(i, j) * a);
					this.quantMatrix8Y.set(i, j, quantMatrix8Y.get(i, j) * a);
				}
			}
		}
		
		if (q == 100) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					this.quantMatrix8C.set(i, j, 1);
					this.quantMatrix8Y.set(i, j, 1);
				}
			}
		}
	}
	
	public Matrix getY() {
		return y;
	}

	public void setY(Matrix y) {
		this.y = y;
	}

	public Matrix getcB() {
		return cB;
	}

	public void setcB(Matrix cB) {
		this.cB = cB;
	}

	public Matrix getcR() {
		return cR;
	}

	public void setcR(Matrix cR) {
		this.cR = cR;
	}

}
