package org.zero.collection.logic;

import android.os.Environment;

/**
 * 常量
 * Created by Zero on 2016/1/5.
 */
public class Constant {

    /**
     * 路径
     */
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String BUGS_PATH = SDCARD_PATH + "/Zero/Bugs";

}
