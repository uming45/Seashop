package com.xunshan.rxhelper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by eldorado on 17-5-4.
 *
 * 模拟一种情形： 异步线程中获取图片在线程中显示
 */
public class ImageFetcher {
    private final static String TAG = ImageFetcher.class.getSimpleName();

    public void fetch(List<Bitmap> imageList, File[] folders, Activity activity) {
        new Thread(() -> {
            for(File f : folders) {
                File[] subFiles = f.listFiles();
                for (File subFile : subFiles) {
                    if (subFile.getName().endsWith(".jpg")) {
                        final Bitmap bitmap = BitmapFactory.decodeFile(subFile.getName());
                        activity.runOnUiThread(() -> imageList.add(bitmap));
                    }
                }
            }
        }).start();
    }

    // 更像是基于动作
    // 1. 把所有要处理的数据归总出来
    // 2. 筛选需要的数据
    // 3. 每个数据转化成我们需要的
    // 4. 上面的操作在子线程运行
    // 5. 在主线程中观察结果
    public void rxFetch(List<Bitmap> imageList, File[] folders) {
        Observable.from(folders)
                .flatMap(file -> Observable.from(file.listFiles()))
                .filter(file -> file.getName().endsWith(".jpg"))
                .map(file -> BitmapFactory.decodeFile(file.getName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageList::add);
    }
}
