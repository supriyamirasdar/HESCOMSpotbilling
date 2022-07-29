// 
// Decompiled by Procyon v0.5.36
// 

package in.nsoft.hescomspotbilling;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Environment;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

class AmigosESCCmd
{
	public byte[] DES_SETKEY;
	public byte[] DES_ENCRYPT;
	public byte[] DES_ENCRYPT2;
	public byte[] ERROR;
	public byte[] ESC_ALT;
	public byte[] ESC_L;
	public byte[] ESC_CAN;
	public byte[] FF;
	public byte[] ESC_FF;
	public byte[] ESC_S;
	public byte[] GS_P_x_y;
	public byte[] ESC_R_n;
	public byte[] ESC_t_n;
	public byte[] LF;
	public byte[] CR;
	public byte[] ESC_3_n;
	public byte[] ESC_SP_n;
	public byte[] DLE_DC4_n_m_t;
	public byte[] GS_V_m;
	public byte[] GS_V_m_n;
	public byte[] GS_W_nL_nH;
	public byte[] ESC_dollors_nL_nH;
	public byte[] ESC_a_n;
	public byte[] GS_exclamationmark_n;
	public byte[] ESC_M_n;
	public byte[] GS_E_n;
	public byte[] ESC_line_n;
	public byte[] ESC_lbracket_n;
	public byte[] GS_B_n;
	public byte[] ESC_V_n;
	public byte[] GS_backslash_m;
	public byte[] FS_p_n_m;
	public byte[] GS_H_n;
	public byte[] GS_f_n;
	public byte[] GS_h_n;
	public byte[] GS_w_n;
	public byte[] GS_k_m_n_;
	public byte[] GS_k_m_v_r_nL_nH;
	public byte[] ESC_W_xL_xH_yL_yH_dxL_dxH_dyL_dyH;
	public byte[] ESC_T_n;
	public byte[] GS_dollors_nL_nH;
	public byte[] GS_backslash_nL_nH;
	public byte[] FS_line_n;
	public byte[] GS_leftbracket_k_pL_pH_cn_67_n;
	public byte[] GS_leftbracket_k_pL_pH_cn_69_n;
	public byte[] GS_leftbracket_k_pL_pH_cn_80_m__d1dk;
	public byte[] GS_leftbracket_k_pL_pH_cn_fn_m;

	AmigosESCCmd() {
		this.DES_SETKEY = new byte[] { 31, 31, 0, 8, 0, 1, 1, 1, 1, 1, 1, 1, 1 };
		this.DES_ENCRYPT = new byte[] { 31, 31, 1 };
		this.DES_ENCRYPT2 = new byte[] { 31, 31, 2 };
		this.ERROR = new byte[1];
		this.ESC_ALT = new byte[] { 27, 64 };
		this.ESC_L = new byte[] { 27, 76 };
		this.ESC_CAN = new byte[] { 24 };
		this.FF = new byte[] { 12 };
		this.ESC_FF = new byte[] { 27, 12 };
		this.ESC_S = new byte[] { 27, 83 };
		this.GS_P_x_y = new byte[] { 29, 80, 0, 0 };
		this.ESC_R_n = new byte[] { 27, 82, 0 };
		this.ESC_t_n = new byte[] { 27, 116, 0 };
		this.LF = new byte[] { 10 };
		this.CR = new byte[] { 13 };
		this.ESC_3_n = new byte[] { 27, 51, 0 };
		this.ESC_SP_n = new byte[] { 27, 32, 0 };
		this.DLE_DC4_n_m_t = new byte[] { 16, 20, 1, 0, 1 };
		this.GS_V_m = new byte[] { 29, 86, 0 };
		this.GS_V_m_n = new byte[] { 29, 86, 66, 0 };
		this.GS_W_nL_nH = new byte[] { 29, 87, 118, 2 };
		this.ESC_dollors_nL_nH = new byte[] { 27, 36, 0, 0 };
		this.ESC_a_n = new byte[] { 27, 97, 0 };
		this.GS_exclamationmark_n = new byte[] { 29, 33, 0 };
		this.ESC_M_n = new byte[] { 27, 77, 0 };
		this.GS_E_n = new byte[] { 27, 69, 0 };
		this.ESC_line_n = new byte[] { 27, 45, 0 };
		this.ESC_lbracket_n = new byte[] { 27, 123, 0 };
		this.GS_B_n = new byte[] { 29, 66, 0 };
		this.ESC_V_n = new byte[] { 27, 86, 0 };
		this.GS_backslash_m = new byte[] { 29, 47, 0 };
		this.FS_p_n_m = new byte[] { 28, 112, 1, 0 };
		this.GS_H_n = new byte[] { 29, 72, 0 };
		this.GS_f_n = new byte[] { 29, 102, 0 };
		this.GS_h_n = new byte[] { 29, 104, -94 };
		this.GS_w_n = new byte[] { 29, 119, 3 };
		this.GS_k_m_n_ = new byte[] { 29, 107, 65, 12 };
		this.GS_k_m_v_r_nL_nH = new byte[] { 29, 107, 97, 0, 2, 0, 0 };
		this.ESC_W_xL_xH_yL_yH_dxL_dxH_dyL_dyH = new byte[] { 27, 87, 0, 0, 0, 0, 72, 2, -80, 4 };
		this.ESC_T_n = new byte[] { 27, 84, 0 };
		this.GS_dollors_nL_nH = new byte[] { 29, 36, 0, 0 };
		this.GS_backslash_nL_nH = new byte[] { 29, 92, 0, 0 };
		this.FS_line_n = new byte[] { 28, 45, 0 };
		this.GS_leftbracket_k_pL_pH_cn_67_n = new byte[] { 29, 40, 107, 3, 0, 49, 67, 3 };
		this.GS_leftbracket_k_pL_pH_cn_69_n = new byte[] { 29, 40, 107, 3, 0, 49, 69, 48 };
		this.GS_leftbracket_k_pL_pH_cn_80_m__d1dk = new byte[] { 29, 40, 107, 3, 0, 49, 80, 48 };
		this.GS_leftbracket_k_pL_pH_cn_fn_m = new byte[] { 29, 40, 107, 3, 0, 49, 81, 48 };
	}

	public byte[] POS_S_TextOut(String pszString, String encoding, int nOrgx, int nWidthTimes, int nHeightTimes, int nFontType, int nFontStyle)
	{
		//ESCCmd Cmd = new ESCCmd();
		/*if (((nOrgx > 65535 ? 1 : 0) | (nOrgx < 0 ? 1 : 0) | (nWidthTimes > 7 ? 1 : 0) | (nWidthTimes < 0 ? 1 : 0) | (nHeightTimes > 7 ? 1 : 0) | (nHeightTimes < 0 ? 1 : 0) | (nFontType < 0 ? 1 : 0) | (nFontType > 4 ? 1 : 0) | (pszString.length() == 0 ? 1 : 0)) != 0) {
			return null;
		}*/
		ESC_dollors_nL_nH[2] = ((byte)(nOrgx % 256));
		ESC_dollors_nL_nH[3] = ((byte)(nOrgx / 256));

		byte[] intToWidth = { 0, 8,16, 32, 48, 64, 80, 96, 112 };
		byte[] intToHeight = { 0, 0,1, 2, 3, 4, 5, 6, 7 };
		GS_exclamationmark_n[2] = ((byte)(intToWidth[nWidthTimes] + intToHeight[nHeightTimes]));

		byte[] tmp_ESC_M_n = ESC_M_n;
		if ((nFontType == 0) || (nFontType == 1)) {
			tmp_ESC_M_n[2] = ((byte)nFontType);
		} else {
			tmp_ESC_M_n =  ESC_M_n;
		}


		GS_E_n[2] = ((byte)(nFontStyle >> 3 & 0x1));

		ESC_line_n[2] = ((byte)(nFontStyle >> 7 & 0x3));
		FS_line_n[2] = ((byte)(nFontStyle >> 7 & 0x3));

		ESC_lbracket_n[2] = ((byte)(nFontStyle >> 9 & 0x1));

		GS_B_n[2] = ((byte)(nFontStyle >> 10 & 0x1));

		ESC_V_n[2] = ((byte)(nFontStyle >> 12 & 0x1));

		byte[] pbString = null;
		try {
			pbString = pszString.getBytes(encoding);
		} catch (Exception e) {
			return null;
		}

		byte[] data = byteArraysToBytes(new byte[][] {
				ESC_dollors_nL_nH, GS_exclamationmark_n, tmp_ESC_M_n, 
				GS_E_n, ESC_line_n, FS_line_n, ESC_lbracket_n, 
				GS_B_n, ESC_V_n, pbString });

		return data;
	}

	public byte[]  POS_S_SetBarcode(String strCodedata, int nOrgx, int nType, int nWidthX, int nHeight, int nHriFontType, int nHriFontPosition)
	{
		/* if (((nOrgx < 0 ? 1 : 0) | (nOrgx > 65535 ? 1 : 0) | (nType < 65 ? 1 : 0) | (nType > 73 ? 1 : 0) | (nWidthX < 2 ? 1 : 0) | (nWidthX > 6 ? 1 : 0) | (nHeight < 1 ? 1 : 0) | (nHeight > 255 ? 1 : 0)) != 0) {
        return;
      }*/
		byte[] bCodeData = null;
		try {
			bCodeData = strCodedata.getBytes("GBK");
		} catch (Exception e) {
			return null;
		}


		ESC_dollors_nL_nH[2] = ((byte)(nOrgx % 256));
		ESC_dollors_nL_nH[3] = ((byte)(nOrgx / 256));
		GS_w_n[2] = ((byte)nWidthX);
		GS_h_n[2] = ((byte)nHeight);
		GS_f_n[2] = ((byte)(nHriFontType & 0x1));
		GS_H_n[2] = ((byte)(nHriFontPosition & 0x3));
		GS_k_m_n_[2] = ((byte)nType);
		GS_k_m_n_[3] = ((byte)bCodeData.length);

		byte[] data = byteArraysToBytes(new byte[][] {
				ESC_dollors_nL_nH, GS_w_n, GS_h_n, GS_f_n, 
				GS_H_n, GS_k_m_n_, bCodeData });
		return data;
	}

	public static byte[] byteArraysToBytes(final byte[][] data) {
		int length = 0;
		for (int i = 0; i < data.length; ++i) {
			length += data[i].length;
		}
		final byte[] send = new byte[length];
		int k = 0;
		for (int j = 0; j < data.length; ++j) {
			for (int l = 0; l < data[j].length; ++l) {
				send[k++] = data[j][l];
			}
		}
		return send;
	}
	//Choose to use system libraries like Canvas to do the stuff.
	public byte[]  createImageFromString(final String text,Context ctx) {
		byte[] data =null;
		try
		{

			Typeface tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
			int height=0;
			String [] rows = text.split("\n\r");
			height=31*rows.length;/* Specify Length of Image File */

			final Paint textPaint = new Paint() {
				{
					setColor(Color.WHITE);
					setTextAlign(Paint.Align.LEFT);
					setTextSize(25f);
					setAntiAlias(true);

				}
			};
			/* Optional to set Rect */
			final Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);

			final Bitmap bmp = Bitmap.createBitmap(525, height, Bitmap.Config.ARGB_8888); //use ARGB_8888 for better quality
			final Canvas canvas = new Canvas(bmp);
			textPaint.setStyle(Paint.Style.FILL); //fill the background with blue color
			canvas.drawRect(0, 0, 525, height, textPaint);
			textPaint.setColor(Color.BLACK);
			textPaint.setTypeface(tf);
			float y=28;
			/* Custom your layout here */
			for(int i =0;i<rows.length;i++){
				if(i==(rows.length-8)){
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);  
					textPaint.setTextSize(28f);
				}else{
					textPaint.setTypeface(tf);
					textPaint.setTextSize(25f);
				}
				canvas.drawText(rows[i], 5, y, textPaint);
				y=y+28;
			}

			FileOutputStream fop = null;
			File file;
			//Specify the path where you want to save the image

			String filpath =  Environment.getExternalStorageDirectory().getPath() +"/SpotBilling";
			if(! new File(filpath).exists())
			{
				new File(filpath).mkdir();
			}
			//String imagepath = filpath + "/imageprint.jpg";
			//file = new File(imagepath);
			//FileOutputStream stream = new FileOutputStream(file); //create your FileOutputStream here
			//bmp.compress(CompressFormat.JPEG, 85, stream);
			//bmp.recycle();
			//stream.close();
			data = POS_PrintPicture(bmp,384,0);
			return data;
		}
		catch(Exception e)
		{
			return data;
		}
	}

	public Bitmap textAsBitmapMultiLine(final String text, final float textSize, final float stroke, final int color, final Typeface typeface, final int linespacing) {
		final TextPaint paint = new TextPaint();
		paint.setColor(color);
		paint.setDither(false);
		paint.setTextSize(textSize);

		paint.setStrokeWidth(stroke);
		paint.setTypeface(typeface);
		paint.setAntiAlias(true);
		paint.setTextAlign(Paint.Align.LEFT);
		final float baseline = (float)(int)(-paint.ascent() + 3.0f);
		final StaticLayout staticLayout = new StaticLayout((CharSequence)text, 0, text.length(), paint, 576, Alignment.ALIGN_NORMAL, 0.5f, 0.5f, false);
		final int linecount = staticLayout.getLineCount();
		final int height = ((int)(baseline + paint.descent() + 1.0f) * linecount + linespacing) / 2;
		final Bitmap image = Bitmap.createBitmap(576, height, Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(image);
		canvas.drawColor(-1);
		staticLayout.draw(canvas);
		return image;
	}
	public Bitmap addBorder(final Bitmap bmp, final int borderSize, final int borderColor) {
		final Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
		final Canvas canvas = new Canvas(bmpWithBorder);
		canvas.drawColor(borderColor);
		canvas.drawBitmap(bmp, (float)borderSize, (float)borderSize, (Paint)null);
		return bmpWithBorder;
	}
	public ArrayList<Bitmap> splitImage(final Bitmap bitmap, int chunkNumbers, int rows) {
		final int cols = 1;
		if (chunkNumbers == 0) {
			chunkNumbers = 1;
		}
		if (rows == 0) {
			rows = 1;
		}
		final ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
		final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
		final int chunkHeight = bitmap.getHeight() / rows;
		final int chunkWidth = bitmap.getWidth() / cols;
		int yCoord = 0;
		for (int x = 0; x < rows; ++x) {
			int xCoord = 0;
			for (int y = 0; y < cols; ++y) {
				chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
				xCoord += chunkWidth;
			}
			yCoord += chunkHeight;
		}
		return chunkedImages;
	}
	public byte[] multiLingualPrint(final String Printdata, final int text_size, final Typeface typeface, final int linespacing) {

		try {
			//final Bitmap bmp = textAsBitmapMultiLine(Printdata, (float)text_size, 9.0f, -16711681, typeface, linespacing);
			final Bitmap bmp = textAsBitmapMultiLine(Printdata, (float)text_size, 9.0f, Color.BLACK, typeface, linespacing);
			//final Bitmap image = addBorder(bmp, 0, 1);


			try
			{
				//Specify the path where you want to save the image
				String filpath =  Environment.getExternalStorageDirectory().getPath() +"/SpotBilling";
				if(! new File(filpath).exists())
				{
					new File(filpath).mkdir();
				}
				String imagepath = filpath + "/imageprint.jpg";
				File file = new File(imagepath);
				FileOutputStream stream;

				stream = new FileOutputStream(file);
				//create your FileOutputStream here
				bmp.compress(CompressFormat.JPEG, 85, stream);
				//bmp.recycle();
				stream.close();
			}
			catch(Exception e)
			{

			}

			return POS_PrintPicture(bmp,576,0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		} //
		/*int height;
		if (image.getHeight() < 3) {
			height = 3;
		}
		else {
			height = image.getHeight();
		}
		final int rows = height / 3;
		final ArrayList<Bitmap> smallImages = splitImage(image, rows, rows);
		final Object var13 = null;

	        try {
	        	for (int i = 0; i < smallImages.size(); ++i) {
	        		final Bitmap mBitmap = smallImages.get(i).copy(Bitmap.Config.ARGB_4444, true);
	        		// final byte[] c = utils.prepareDataToPrint(mBitmap);
	        		// new PrinterLib();
	        		// PrintData(c, c.length);
	        		POS_PrintPicture(mBitmap,384,0);
	        		Thread.sleep(1L);
	        	}
	        }
	        catch (InterruptedException var14) {
	        	var14.printStackTrace();
	        }*/
	}

	private byte[] bitmapToBWPix(final Bitmap mBitmap) {
		final int[] pixels = new int[mBitmap.getWidth() * mBitmap.getHeight()];
		final byte[] data = new byte[mBitmap.getWidth() * mBitmap.getHeight()];
		mBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		AmigosImageProcessing.format_K_dither16x16(pixels, mBitmap.getWidth(), mBitmap.getHeight(), data);
		return data;
	}

	private byte[] eachLinePixToCmd(final byte[] src, final int nWidth, final int nMode) {
		final int[] p0 = { 0, 128 };
		final int[] p2 = { 0, 64 };
		final int[] p3 = { 0, 32 };
		final int[] p4 = { 0, 16 };
		final int[] p5 = { 0, 8 };
		final int[] p6 = { 0, 4 };
		final int[] p7 = { 0, 2 };
		final int nHeight = src.length / nWidth;
		final int nBytesPerLine = nWidth / 8;
		final byte[] data = new byte[nHeight * (8 + nBytesPerLine)];
		int offset = 0;
		int k = 0;
		for (int i = 0; i < nHeight; ++i) {
			offset = i * (8 + nBytesPerLine);
			data[offset + 0] = 29;
			data[offset + 1] = 118;
			data[offset + 2] = 48;
			data[offset + 3] = (byte)(nMode & 0x1);
			data[offset + 4] = (byte)(nBytesPerLine % 256);
			data[offset + 5] = (byte)(nBytesPerLine / 256);
			data[offset + 6] = 1;
			data[offset + 7] = 0;
			for (int j = 0; j < nBytesPerLine; ++j) {
				data[offset + 8 + j] = (byte)(p0[src[k]] + p2[src[k + 1]] + p3[src[k + 2]] + p4[src[k + 3]] + p5[src[k + 4]] + p6[src[k + 5]] + p7[src[k + 6]] + src[k + 7]);
				k += 8;
			}
		}
		return data;
	}

	public byte[] POS_PrintPicture(final Bitmap mBitmap, final int nWidth, final int nMode) {
		try
		{
			final int width = (nWidth + 7) / 8 * 8;
			int height = mBitmap.getHeight() * width / mBitmap.getWidth();
			height = (height + 7) / 8 * 8;
			final Bitmap rszBitmap = AmigosImageProcessing.resizeImage(mBitmap, width, height);
			final Bitmap grayBitmap = AmigosImageProcessing.toGrayscale(rszBitmap);
			final byte[] dithered = this.bitmapToBWPix(grayBitmap);
			final byte[] data = this.eachLinePixToCmd(dithered, width, nMode);
			return data;
			//this.IO.Write(data, 0, data.length);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	//08-02-2021
	public byte[] POS_EPSON_SetQRCode(final String strCodedata, final int nWidthX, final int nVersion, final int nErrorCorrectionLevel) {
		if (nWidthX < 2 | nWidthX > 6 | nErrorCorrectionLevel < 1 | nErrorCorrectionLevel > 4) {
			return null;
		}
		if (nVersion < 0 || nVersion > 16) {
			return null;
		}
		byte[] bCodeData = null;
		try {
			bCodeData = strCodedata.getBytes("GBK");
			
		}
		catch (UnsupportedEncodingException e) {
			return null;
		}
		GS_w_n[2] = (byte)nWidthX;
		GS_leftbracket_k_pL_pH_cn_67_n[7] = (byte)nVersion;
		GS_leftbracket_k_pL_pH_cn_69_n[7] = (byte)(47 + nErrorCorrectionLevel);
		GS_leftbracket_k_pL_pH_cn_80_m__d1dk[3] = (byte)(bCodeData.length + 3 & 0xFF);
		GS_leftbracket_k_pL_pH_cn_80_m__d1dk[4] = (byte)((bCodeData.length + 3 & 0xFF00) >> 8);
		final byte[] data = byteArraysToBytes(new byte[][] { GS_w_n, GS_leftbracket_k_pL_pH_cn_67_n,GS_leftbracket_k_pL_pH_cn_69_n, GS_leftbracket_k_pL_pH_cn_80_m__d1dk, bCodeData, GS_leftbracket_k_pL_pH_cn_fn_m });
		return data;
	}


}
