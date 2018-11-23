package com.zgg.hochat.common;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.upgrade.UpgradeStateListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.zgg.hochat.BuildConfig;
import com.zgg.hochat.ui.activity.MainActivity;
import com.zgg.hochat.utils.Constant;
import com.zgg.hochat.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * 类描述：Bugly 管理类
 * 创建时间：2018/5/4 13:45
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class BuglyManager {

    private static BuglyManager instance;
    private boolean isInitBugly;

    private BuglyManager() {
    }

    public static BuglyManager getInstance() {
        if (instance == null) {
            synchronized (BuglyManager.class) {
                if (instance == null) {
                    instance = new BuglyManager();
                }
            }
        }
        return instance;
    }

    public void initBugly(Context context) {
        isInitBugly = true;
        String packageName = context.getPackageName(); //获取当前包名
        String processName = getProcessName(android.os.Process.myPid()); // 获取当前进程名
        //UserStrategy类作为Bugly的初始化扩展，在这里您可以修改本次初始化Bugly数据的版本、渠道及部分初始化行为
        //如果通过UserStrategy设置了版本号和渠道号，则会覆盖“AndroidManifest.xml”里面配置的版本号和渠道
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName)); // 设置是否为上报进程
//        strategy.setAppChannel("bugly");  //设置渠道
        strategy.setAppVersion(BuildConfig.VERSION_NAME); //App的版本
        strategy.setAppPackageName(BuildConfig.APPLICATION_ID);  //App的包名

        //初始化Bugly之前通过以下接口把调试设备设置成“开发设备
        CrashReport.setIsDevelopmentDevice(context, BuildConfig.DEBUG);
//        CrashReport.initCrashReport(context, "da0381777c", isDebug, strategy);

        configBuglyUpdate();
        //第三个参数为SDK调试模式开关:建议在测试阶段建议设置成true，发布时设置为false
        Bugly.init(context, Constant.BUGLY_APPID, true, strategy); //升级SDK已经集成crash上报功能，固无需再集成crash sdk
    }

    private void configBuglyUpdate() {
        //自动检查更新开关:true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
        Beta.autoCheckUpgrade = false;

        //升级检查周期设置:设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
        Beta.upgradeCheckPeriod = 60 * 1000;

        //设置启动延时为3s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
        Beta.initDelay = 5 * 1000;

        //设置通知栏大图标:largeIconId为项目中的图片资源;
//        Beta.largeIconId = R.mipmap.ic_logo;

        //设置状态栏小图标:smallIconId为项目中的图片资源id
//        Beta.smallIconId = R.mipmap.ic_logo;


        //设置更新弹窗默认展示的banner:defaultBannerId为项目中的图片资源Id; 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading...“;
//        Beta.defaultBannerId = R.mipmap.bg_splash;

        //添加可显示弹窗的Activity:只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗。
        Beta.canShowUpgradeActs.add(MainActivity.class);

        //设置是否显示消息通知:如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。
        Beta.enableNotification = true;

        //设置是否显示弹窗中的apk信息:如果你使用我们默认弹窗是会显示apk信息的，如果你不想显示可以将这个接口设置为false。
        Beta.canShowApkInfo = true;

        //关闭热更新能力:升级SDK默认是开启热更新能力的，如果你不需要使用热更新，可以将这个接口设置为false。
        Beta.enableHotfix = false;

        /*设置自定义升级对话框UI布局
         *
         * upgrade_dialog为项目的布局资源。 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用： -
         * 特性图片：beta_upgrade_banner，如：android:tag="beta_upgrade_banner"
         *标题：beta_title，如：android:tag="beta_title"
         *升级信息：beta_upgrade_info 如： android:tag="beta_upgrade_info"
         *更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"
         *取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
         *确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
         */
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;


        /* 设置更新状态回调接口 */
        Beta.upgradeStateListener = new UpgradeStateListener() {
            @Override
            public void onUpgradeSuccess(boolean isManual) {
                //更新成功
                Logger.e("onUpgradeSuccess---bugly更新成功");
            }

            @Override
            public void onUpgradeFailed(boolean isManual) {
                //更新失败
                Logger.e("onUpgradeFailed---bugly更新失败");
            }

            @Override
            public void onUpgrading(boolean isManual) {
                //正在更新
                Logger.e("onUpgrading---bugly正在更新");
                ToastUtils.showShort("正在检查更新...");
            }

            @Override
            public void onDownloadCompleted(boolean b) {
                Logger.e("onDownloadCompleted---bugly下载apk完成");
            }

            @Override
            public void onUpgradeNoVersion(boolean isManual) {
                //没有更新
                ToastUtils.showShort("已是最新版本!");
                Logger.e("onUpgradeNoVersion---bugly没有更新");
            }
        };
    }

    /**
     * 手动检查更新（用于设置页面中检测更新按钮的点击事件）
     */
    public void checkUpgrade() {
        Beta.checkUpgrade();
    }

    /**
     * @param isManual  用户手动点击检查，非用户点击操作请传false
     * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
     */
    public void checkUpgrade(boolean isManual, boolean isSilence) {
        Beta.checkUpgrade(isManual, isSilence);
    }


    /**
     * 获取本地已有升级策略（非实时，可用于界面红点展示）<br/>
     * <p>
     * public String id = "";//唯一标识  <br/>
     * public String title = "";//升级提示标题 <br/>
     * public String newFeature = "";//升级特性描述 <br/>
     * public long publishTime = 0;//升级发布时间,ms <br/>
     * public int publishType = 0;//升级类型 0测试 1正式 <br/>
     * public int upgradeType = 1;//升级策略 1建议 2强制 3手工 <br/>
     * public int popTimes = 0;//提醒次数 <br/>
     * public long popInterval = 0;//提醒间隔 <br/>
     * public int versionCode; <br/>
     * public String versionName = ""; <br/>
     * public String apkMd5;//包md5值 <br/>
     * public String apkUrl;//APK的CDN外网下载地址 <br/>
     * public long fileSize;//APK文件的大小 <br/>
     * pubilc String imageUrl; // 图片url <br/>
     *
     * @return UpgradeInfo
     */
    public UpgradeInfo getUpgradeInfo() {
        return Beta.getUpgradeInfo();
    }


    public boolean isInit() {
        return isInitBugly;
    }


    public String getAppChanel() {
        return Bugly.getAppChannel();
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


}
