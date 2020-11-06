package com.ynkeen.app.bitmaphandlelearn.utils;


import android.graphics.Bitmap;

/**
 * author: ykhe
 * date: 20-11-2
 * description:
 */
public class BlurUtil {
//    const gaussBlur = (imgData) => {
//        //bm 的像素数组
//        let pixes = imgData.data
//        let width = imgData.width
//        let height = imgData.height
//        //变换矩阵?
//        let gaussMatrix = []
//        let gaussSum = 0
//        let x; let y
//        let r; let g
//        let i; let j; let k; let len
//        let radius = 60
//        let sigma = 15
//
//        let a = 1 / (Math.sqrt(2 * Math.PI) * sigma)
//        let b = -1 / (2 * sigma * sigma)
//        // 生成高斯矩阵
//        for (i = 0, x = -radius; x <= radius; x++, i++) {
//            g = a * Math.exp(b * x * x)
//            gaussMatrix[i] = g
//            gaussSum += g
//        }
//        // 归一化, 保证高斯矩阵的值在[0,1]之间
//        for (i = 0, len = gaussMatrix.length; i < len; i++) {
//            gaussMatrix[i] /= gaussSum
//        }
//        // x 方向一维高斯运算
//        for (y = 0; y < height; y++) {
//            for (x = 0; x < width; x++) {
//                r = g = b = a = 0
//                gaussSum = 0
//                for (j = -radius; j <= radius; j++) {
//                    k = x + j
//                    if (k >= 0 && k < width) { // 确保 k 没超出 x 的范围
//                        // r,g,b,a 四个一组
//                        i = (y * width + k) * 4
//                        r += pixes[i] * gaussMatrix[j + radius]
//                        g += pixes[i + 1] * gaussMatrix[j + radius]
//                        b += pixes[i + 2] * gaussMatrix[j + radius]
//                        gaussSum += gaussMatrix[j + radius]
//                    }
//                }
//                i = (y * width + x) * 4
//                // 除以 gaussSum 是为了消除处于边缘的像素, 高斯运算不足的问题
//                pixes[i] = r / gaussSum
//                pixes[i + 1] = g / gaussSum
//                pixes[i + 2] = b / gaussSum
//            }
//        }
//        // y 方向一维高斯运算
//        for (x = 0; x < width; x++) {
//            for (y = 0; y < height; y++) {
//                r = g = b = a = 0
//                gaussSum = 0
//                for (j = -radius; j <= radius; j++) {
//                    k = y + j
//                    if (k >= 0 && k < height) { // 确保 k 没超出 y 的范围
//                        i = (k * width + x) * 4
//                        r += pixes[i] * gaussMatrix[j + radius]
//                        g += pixes[i + 1] * gaussMatrix[j + radius]
//                        b += pixes[i + 2] * gaussMatrix[j + radius]
//                        gaussSum += gaussMatrix[j + radius]
//                    }
//                }
//                i = (y * width + x) * 4
//                pixes[i] = r / gaussSum
//                pixes[i + 1] = g / gaussSum
//                pixes[i + 2] = b / gaussSum
//            }
//        }
//        return imgData
//    }
}
