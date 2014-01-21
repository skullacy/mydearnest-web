package com.osquare.mydearnest.util.image.dominant;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DominantColors {
	public static final int DEFAULT_NUM_COLORS = 3;
	public static final double DEFAULT_MIN_DIFF = 0.5f;
	public static final int SIDE_SIZE = 50;
	
	public static DominantColor[] getDominantColor(BufferedImage image) {
		return getDominantColor(image, DEFAULT_NUM_COLORS);
	}
	
	public static DominantColor[] getDominantColor(BufferedImage image, int numColors) {
		return getDominantColor(image, numColors, DEFAULT_MIN_DIFF);
	}
	
	public static DominantColor[] getDominantColor(BufferedImage image, int numColors, double minDiff) {
		return kMeans(getPoints(image), numColors, minDiff);
	}
	
	public static int[] getPoints(BufferedImage image) {
		int[] points = new int[image.getWidth() * image.getHeight()];
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				points[j+i*width] = image.getRGB(j, i);
		
		return points;
	}
	
	/**
	 * @brief
	 * numColors 수만큼 무작위로 색깔을 분리해낸다. 단, 같은색깔은 피하기 
	 */
	private static int[] getRandomMiddles(int[] points, int numColors) {
		ArrayList<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < points.length; i++)
			indices.add(i);
		
		Collections.shuffle(indices);
		ArrayList<Integer> midArray = new ArrayList<Integer>();
		int[] middles = new int[numColors];
		int index = 0;
		while (midArray.size() < numColors) {
			int val = points[indices.get(index++)];
			
			//val이 중복되지 않게 검사한다.
			if(!midArray.contains(val)) {
				middles[midArray.size()] = val;
				midArray.add(val);
			}
		}
		
		return middles;
	}
	
	private static double calculateDistance(int _c1, int _c2) {
		Color c1 = new Color(_c1);
		Color c2 = new Color(_c2);
		return Math.sqrt(
					0.9 * Math.pow(c1.getRed() - c2.getRed(), 2) +
					1.2 * Math.pow(c1.getGreen() - c2.getGreen(), 2) +
					0.9 * Math.pow(c1.getBlue() - c2.getBlue(), 2)
				);
	}
	
	private static int calculateCenter(List<Integer> points) {
		int rSum, gSum, bSum;
		rSum = gSum = bSum = 0;
		for(int i: points) {
			Color pColor = new Color(i);
			rSum += pColor.getRed();
			gSum += pColor.getGreen();
			bSum += pColor.getBlue();
		}
		if (points.size() == 0) {
			return 0;
		} else {
			return new Color(rSum/points.size(), gSum/points.size(), bSum/points.size()).getRGB();
		}
	}
	
	
	/**
	 * @brief K-Means 알고리즘을 이용하여 사진의 RGB컬러를 클러스터링 한다. 
	 */
	public static DominantColor[] kMeans(int[] points, int numColors, double minDiff) {
		//클러스터 생성
		int[] middles = getRandomMiddles(points, numColors);
		DominantColor[] colors = new DominantColor[numColors];
		
		while(true) {
			// 샘플링과 정렬을 무한반복
			ArrayList<Integer>[] newClusters = new ArrayList[numColors];
			for (int i = 0; i < numColors; i++) 
				newClusters[i] = new ArrayList<Integer>();
			
			for (int point : points) {
				double minDist = Double.MAX_VALUE;
				int minId = 0;
				for (int i = 0; i < middles.length; i++) {
					//현재 포인트 컬러와 랜덤추출한 컬러의 유사도를 측정
					double dist = calculateDistance(point, middles[i]);
					if(dist < minDist) {
						minDist = dist;
						minId = i;
					}
				}
				newClusters[minId].add(point);
			}
			
			// 새 클러스터를 이전클러스터로 복사
			double diff = 0;
			for (int i = 0; i < middles.length; i++) {
				int newCenter = calculateCenter(newClusters[i]);
				diff = Math.max(diff, calculateDistance(newCenter, middles[i]));
				middles[i] = newCenter;
			}
			if(diff < minDiff) {
				for (int i = 0; i < middles.length; i++)
					colors[i] = new DominantColor(middles[i], (float) newClusters[i].size() / (float) points.length);
				break;
			}
			
			
		}
		
		Arrays.sort(colors, new Comparator<DominantColor>() {
			@Override
			public int compare(DominantColor lhs, DominantColor rhs) {
				return (int)(100 * (lhs.getPercentage() - rhs.getPercentage()));
			}
			
		});
		
		return colors;
	}

}
