package com.android.formapp.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;


import com.android.formapp.BuildConfig;
import com.android.formapp.MyApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by lijie03 on 2017/10/16.
 */

public class CommonUtils {

    // 自定义log参数
    private static final String LOG_TAG = "O2O_app_v1";
    private static final int LOG_SIZE_LIMIT = 3500;

    // 定时器参数
    private static Button mBtn;
    private static int mTime;
    private static Handler handler = new Handler();
    private static boolean isSending = false;
    private static boolean mallowgetCode;
    private static boolean mispress;

    /**
     * 当前按钮点击的时间戳
     */
    private static long mBtnClickTimestamp = 0;

    /**
     * 是否按钮点击非法,判断是否重复点击
     *
     * @return true无效|false有效
     */
    public static boolean IsBtnClickInvalid() {
        long curBtnClickTimestamp = System.currentTimeMillis();
        if (curBtnClickTimestamp - mBtnClickTimestamp > 100) {
            mBtnClickTimestamp = curBtnClickTimestamp;
            return false;
        }

        return true;
    }

    /**
     * 统一自定义log，建议使用
     *
     * @param paramClass getClass()或xxx.class
     * @param param      需要打印Object
     */
    public static void LOG_D(Class<?> paramClass, Object param) {
        // 只有debug模式才打印log
        if ( BuildConfig.DEBUG) {
            String paramString = param.toString();
            String str = paramClass.getName();
            if (str != null) {
                str = str.substring(1 + str.lastIndexOf("."));
            }
            int i = paramString.length();
            if (i > LOG_SIZE_LIMIT) {
                int j = 0;
                int k = 1 + i / LOG_SIZE_LIMIT;
                while (j < k + -1) {
                    Log.d(LOG_TAG, paramString.substring(j * LOG_SIZE_LIMIT, LOG_SIZE_LIMIT * (j + 1)));
                    j++;
                }
                Log.d(LOG_TAG, paramString.substring(j * LOG_SIZE_LIMIT, i));
            } else {
                Log.d(LOG_TAG, str + " -> " + paramString);
            }
        }
    }

    /**
     * 获取安装包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = new PackageInfo();
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    /**
     * 判断Sdcard是否存在
     *
     * @return
     */
    public static boolean detectSdcardIsExist() {
        String extStorageState = Environment.getExternalStorageState();
        File file = Environment.getExternalStorageDirectory();
        if (!Environment.MEDIA_MOUNTED.equals(extStorageState) || !file.exists() || !file.canWrite() || file.getFreeSpace() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断指定路径下的文件是否存在
     */
    public static boolean detectFileIsExist(String path) {
        if (null != path) {
            File file = new File(path);
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 把index数字,转化为标准格式类似"001"
     *
     * @param pos index数字
     * @return 返回格式化的数字
     */
    public static String formatIndex(int pos) {
        if (pos < 0) {
            pos = 0;
        }

        if (pos > 999) {
            return pos / 1000 + "...";
        }

        if (pos / 100 > 0) {
            return String.valueOf(pos);
        }

        if (pos / 10 > 0) {
            return "0" + pos;
        }

        return "00" + pos;
    }

    /**
     * 获得当前时间
     *
     * @return
     */
    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 获得当前日期
     *
     * @return
     */
    public static String getDateNow() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
    }

    /**
     * 获得当前日期时间
     *
     * @return
     */
    public static String getDateTimeNow() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 时间比较
     * true，正确没有问题
     * chooseDate<currentDate 为true
     *
     * @param currentDate 当前日期
     * @param chooseDate  选择日期
     * @return
     */
    public static boolean compareCurrentDate(String currentDate, String chooseDate) {
        Long dCurrentDate = Long.valueOf(currentDate.replaceAll("-", ""));
        Long dChooseDate = Long.valueOf(chooseDate.replaceAll("/", ""));
        if (dChooseDate.compareTo(dCurrentDate) < 0) {
            return false;
        } else if (dChooseDate.compareTo(dCurrentDate) == 0) {
            return true;
        } else {
            return true;
        }
    }


    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int Dp2Px(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    public static int Px2Dp(Context context, float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 获取IMEI
     * 手机唯一设别号码
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        if (null == context) {
            context = MyApp.getInstance();
        }
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }


    /**
     * 获取手机brand相关信息
     *
     * @return string类型
     */
    public static String getDeviceBrandInfo() {
        Map<String, String> brandInfo = new WeakHashMap<>();
        brandInfo.put("MODEL", Build.MODEL);
        brandInfo.put("BRAND", Build.BRAND);
        brandInfo.put("MANUFACTURER", Build.MANUFACTURER);
        brandInfo.put("PRODUCT", Build.PRODUCT);
        brandInfo.put("SDK_INT", String.valueOf(Build.VERSION.SDK_INT));
        brandInfo.put("RELEASE", Build.VERSION.RELEASE);

        return brandInfo.toString();
    }

    @SuppressLint("NewApi")
    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            PackageManager pm = context.getPackageManager();
            result = pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;

            if (result) {
                result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
            }

            if (result) {
                result = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return result;
    }

    /**
     * 检查是否有camera权限,尝试open一个camera,捕获异常信息
     *
     * @param context
     * @return
     */
    public static boolean checkPermissionCamera(Context context) {
        boolean bIsCameraPermissonOk = false;

        Camera testCamera = null;
        try {
            bIsCameraPermissonOk = true;
            testCamera = Camera.open();
        } catch (RuntimeException re) {
            bIsCameraPermissonOk = false;
        } finally {
            if (null != testCamera) {
                testCamera.setPreviewCallback(null);
                testCamera.stopPreview();// 停掉原来摄像头的预览
                testCamera.release();
                testCamera = null;
            }
        }

        //NOTE:再次校验是否有授权,否则不让跳转手机拍照
        if (checkPermission(context, Manifest.permission.CAMERA)
                && bIsCameraPermissonOk) {
            return true;
        }

        return false;
    }

    /**
     * 判断字符串是否为空或NULL
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0 || "{}".equals(str) || "null".equals(str);
    }

    /**
     * 判断集合对象是否为空或NULL
     *
     * @param list
     * @param <>
     * @return
     */
    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * InputStream to String
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * 千分位金钱显示, 换样式比如:$1234;或者1234元
     *
     * @param money 需要转换的金钱值
     * @return 返回转换后的数值
     */
    public static String formatMoneyStyle(String money) {
        if (isNullOrEmpty(money)) {
            return "";
        }

        if (!VerifyUtils.isNumberic(money)) {
            return money;
        }

        int tempMoney = Integer.parseInt(money);
        if (tempMoney == 0) {
            return "暂无额度";
        }

        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        money = decimalFormat.format(tempMoney);

        money = String.format("￥%s", money);

        return money;
    }

    /**
     * 转换email格式
     *
     * @param email 待转换的email
     * @return 输出xxx***xxx@xxx.xxxxx
     */
    public static String formatEmailStyle(String email) {
        if (isNullOrEmpty(email)) {
            return "";
        }

        int lastIndexOfAt = email.lastIndexOf("@");
        if (lastIndexOfAt > 6) {
            //email转化为xxx***xxx@xxx.xxxxx
            email = email.replaceAll("(\\S{3})(\\S+)(\\S{3})(@\\S+\\.\\S+)", "$1***$3$4");
        }

        return email;
    }

    /**
     * 创建通用弹窗的builder,自定义内容
     *
     * @param contexts
     * @param positiveMessage
     * @param positiveListener
     * @param negativeMessage
     * @param negativeListener
     * @return
     */
    public static AlertDialog.Builder createDialogBuilder(Context contexts,
                                                          String positiveMessage,
                                                          DialogInterface.OnClickListener positiveListener,
                                                          String negativeMessage,
                                                          DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexts);
        // builder.setSingleChoiceItems(items, 0, listener);

        builder.setPositiveButton(positiveMessage, positiveListener);
        builder.setNegativeButton(negativeMessage, negativeListener);
        return builder;
    }

    /**
     * 创建通用弹窗的builder,自定义内容
     *
     * @param contexts
     * @param positiveMessage
     * @param positiveListener
     * @return
     */
    public static AlertDialog.Builder createDialogBuilder(Context contexts,
                                                          String positiveMessage,
                                                          DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexts);
        // builder.setSingleChoiceItems(items, 0, listener);

        builder.setPositiveButton(positiveMessage, positiveListener);
        return builder;
    }

    /**
     * 屏蔽手机号中间段
     *
     * @param replace
     * @return
     */
    public static String replaceWithAsteriskForPhone(@NonNull String replace) {
        if (TextUtils.isEmpty(replace)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(replace.substring(0, 3));
        for (int i = 3; i < replace.length() - 4; i++) {
            sb.append("*");
        }
        sb.append(replace.substring(replace.length() - 4));
        return sb.toString();
    }

    /**
     * 屏蔽银行卡号中间段
     *
     * @param replace
     * @return
     */
    public static String replaceWithAsteriskForBankCard(@NonNull String replace) {
        if (TextUtils.isEmpty(replace)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(replace.substring(0, 4));
        for (int i = 4; i < replace.length() - 4; i++) {
            if (i % 4 == 0) {
                //每4位添加一个空格
                sb.append(" ");
            }
            sb.append("*");
        }
        //最后倒数4位前,添加空格
        sb.append(" ");
        sb.append(replace.substring(replace.length() - 4));
        return sb.toString();
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int versioncode = 0;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versioncode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioncode;
    }

    /**
     * 获取应用版本名字
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String versioncode = "v1.0.0";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versioncode = packageInfo.versionName;
            versioncode = packageInfo.applicationInfo.loadLabel(packageManager).toString() + " " + versioncode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versioncode;
    }

    /**
     * 获取AndroidManifest中的Application的MetaData的value值
     *
     * @param applicationMetaDataName
     * @return
     */
    public static String getAppManifestApplicationMetaData(String applicationMetaDataName) {
        try {
            ApplicationInfo appInfo = MyApp.getInstance().getPackageManager().getApplicationInfo(MyApp.getInstance().getPackageName(), PackageManager.GET_META_DATA);
            String value = appInfo.metaData.getString(applicationMetaDataName);
            return value;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteAllFiles(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }

            for (File f : childFile) {
                deleteAllFiles(f);
            }
            file.delete();
        }
    }

    /**
     * 根据指定路径,创建文件夹,如果父节点不存在,则依次创建
     *
     * @param filePath 目标文件夹路径
     * @return
     */
    public static File createFileDir(String filePath) {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        return targetFile;
    }

    /**
     * 判断存储空间大小是否满足条件
     *
     * @param sizeByte
     * @return
     */
    public static boolean isAvaiableSpace(float sizeByte) {
        boolean ishasSpace = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            float availableSpare = blocks * blockSize;
            if (availableSpare > (sizeByte + 1024 * 1024)) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }

    /**
     * 开始安装apk文件
     *
     * @param context
     * @param localFilePath
     */
    public static void installApkByGuide(Context context, String localFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + localFilePath), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 设置图片路径，缩略图最大宽度，从文件中读取图像数据并返回Bitmap对象
     *
     * @param filePath
     * @param maxWeight
     * @return
     */
    public static Bitmap reduce(String filePath, int maxWeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        float larger = (realWidth > realHeight) ? realWidth : realHeight;
        int scale = (int) (larger / maxWeight);
        if (scale <= 0) {
            scale = 1;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;

        bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    /**
     * 启动当前界面快捷方式
     *
     * @param context
     * @param target
     */
    public static void launch(Context context, Class<?> target) {
        Intent intent = new Intent();
        intent.setClass(context, target);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        context.startActivity(intent);
    }

    /**
     * 启动当前界面,携带参数
     **/
    public static void launchWithData(Activity self, String key, Object value, Class<?> target) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, (Serializable) value);
        intent.putExtras(bundle);
        intent.setClass(self, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        self.startActivity(intent);
    }

    /**
     * 启动当前界面，关闭时需返回结果
     **/
    public static void launchWithResult(Activity self, String key, Object value, Class<?> target, int requestCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, (Serializable) value);
        intent.putExtras(bundle);
        intent.setClass(self, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        self.startActivityForResult(intent, requestCode);
    }

    /**
     * 关闭当前界面，回传结果
     **/
    public static void finishWithResult(Activity self, String key, Object value, Class<?> target, int resultCode) {
        Intent intent = new Intent(self, target);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, (Serializable) value);
        intent.putExtras(bundle);
        self.setResult(resultCode, intent);
        self.finish();
    }

    /**
     * 打电话
     *
     * @param context
     * @param phoneNum
     */
    public static void startIntentForCalling(Context context, String phoneNum) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNum));//mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
        context.startActivity(intent);
    }

    /**
     * 启动浏览器 打开url链接
     *
     * @param context
     * @param url
     */
    public static void startIntentForUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**
     * 启动设置-App-权限设置界面
     *
     * @param context
     */
    public static void startIntentForSettingPermission(Context context) {
        //去系统设置页面，需要用户手动打开
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + CommonUtils.getPackageInfo(context).packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 检查apk签名是否一致,防止签名不对,上架错误
     *
     * @param context 应用上下文
     * @return boolean true | false是否校验成功
     */
    public static boolean checkAppSignature(Context context) {
        //如果是线上环境,或者非debug环境,则需要检查
        if (BuildConfig.DEBUG) {
            return true;
        }
        //需要固定写在程序的某个位置的签名,sha1+md5+prefix
        String curSystemTime = System.currentTimeMillis() + "";
        String SIGNATURE_TARGET_MD5 = curSystemTime + "E01DB4EC";

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuilder hexString = new StringBuilder();

            for (byte tempByte : publicKey) {
                String appendString = Integer.toHexString(0xFF & tempByte).toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }

            //截断最后的冒号
            if (hexString.length() > 0) {
                hexString.setLength(hexString.length() - 1);
            }
            String tempAPKSignMD5 = EncryptUtils.md5(hexString.toString());
            tempAPKSignMD5 = curSystemTime + tempAPKSignMD5;
            hexString.setLength(0);
            if (!isNullOrEmpty(tempAPKSignMD5) && tempAPKSignMD5.startsWith(SIGNATURE_TARGET_MD5)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 强制隐藏软键盘
     */
    public static void hideKeyboardForce(TextView editText) {
        if (null != editText) {
            View view = (View) editText.getParent();
            view.requestFocus();

            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }

        }
    }

    /**
     * 格式化时间
     *
     * @param date
     * @return
     */
    public static String getTimeSS(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String getTimeDD(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String getTimeDD2(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }


    /**
     * 拷贝数据到剪贴板
     *
     * @param context 上下文对象
     * @param content 复制内容
     * @return true，复制成功
     */
    public static boolean copyToClipboard(Context context, String content) {
        if (content == null) {
            return false;
        }
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(content);
        return true;
    }

    /**
     * 从剪贴板获取数据
     *
     * @param context 上下文对象
     * @return 剪切板数据
     */
    public static String getClipboardData(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboardManager.getText().toString();
    }

    /**
     * @param view 调整Layout的宽高
     * @param w    修改后的宽度
     * @param h    修改后的高度
     */
    public static void setLayoutParams(View view, int w, int h) {

        ViewGroup.LayoutParams para1 = view.getLayoutParams();
        para1.height = h;
        para1.width = w;
        view.setLayoutParams(para1);
    }


    /**
     * 保留两位小数
     *
     * @param bd
     * @return
     */
    public static String formatAmount(BigDecimal bd) {
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();

    }

    /**
     * 保留两位小数
     *
     * @param num
     * @return
     */
    public static String formatAmount(String num) {
        return formatAmount(new BigDecimal(num));
    }

    /**
     * 保留两位小数
     *
     * @param num
     * @return
     */
    public static String formatAmount(double num) {
        return formatAmount(new BigDecimal(num));

    }

    /**
     * 保留两位小数
     *
     * @param num
     * @return
     */
    public static String formatAmountwan(BigDecimal num) {
        return formatAmount(new BigDecimal((num.doubleValue() / 10000))) + "万";
    }

}
