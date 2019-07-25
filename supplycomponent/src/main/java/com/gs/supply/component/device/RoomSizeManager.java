package com.gs.supply.component.device;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.gs.supply.component.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author husky
 * create on 2019-07-25-09:57
 */
public class RoomSizeManager {

    public static long[] getRomMemroy() {
        long[] romInfo = new long[2];
        //Total rom memory
        romInfo[0] = getTotalInternalMemorySize();

        //Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfo[1] = blockSize * availableBlocks;

        return romInfo;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }


    public static long[] getSDCardMemory() {
        long[] sdCardInfo = new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();

            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return sdCardInfo;
    }

    /**
     * 获取运存大小
     *
     * @return 获取运存大小
     */
    public static long[] getRamMemory() {
        ActivityManager manager = (ActivityManager) Component.mApplicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        long availMem = info.availMem;
        long total = info.totalMem;
        long[] ram = new long[2];
        ram[0] = total;
        ram[1] = availMem;
        return ram;
    }

    /**
     * 内存大小
     *
     * @return 内存大小
     */
    public static long[] getRomMemory() {
        final StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long totalCounts = statFs.getBlockCountLong();//总共的block数
        long availableCounts = statFs.getAvailableBlocksLong(); //获取可用的block数
        long size = statFs.getBlockSizeLong(); //每格所占的大小，一般是4KB==
        long availROMSize = availableCounts * size;//可用内部存储大小
        long totalROMSize = totalCounts * size;
        long[] ram = new long[2];
        ram[0] = totalROMSize;
        ram[1] = availROMSize;
        return ram;
    }


}
