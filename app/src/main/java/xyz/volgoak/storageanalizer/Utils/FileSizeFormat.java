package xyz.volgoak.storageanalizer.Utils;

import java.text.DecimalFormat;

/**
 * Created by Volgoak on 19.10.2017.
 */

public abstract class FileSizeFormat {

    public static String readableFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
