package com.archeanx.lib.tv.util;

import android.app.Instrumentation;
import android.util.Log;

import com.google.common.io.ByteStreams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @创建者
 * @创建时间
 * @描述 adb 工具命令使用类
 * 示例  setprop persist.sys.audio.usbcard 2  需要用空格区分
 */
public class AdbUtil {


    /**
     * 执行adb命令，并返回，监听是子线程，使用时注意线程问题
     *
     * @param commandTxt 命令
     * @param listener   监听
     */
    public static void adbCommand(final String commandTxt, final OnAdbCommandListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] command = commandTxt.trim().split(" ");
                    ProcessBuilder processBuilder = new ProcessBuilder(command);
                    //让错误和正确信息，到一个流里面输出
                    processBuilder.redirectErrorStream(true);
                    final Process process = processBuilder.start();

                    final Timer timeout = new Timer();
                    timeout.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            process.destroy();
                        }
                    }, 1000 * 3);// timeout

                    int exitCode = process.waitFor();
                    timeout.cancel();

                    String msg = new String(ByteStreams.toByteArray(process.getInputStream()));
                    //getMsg(process.getInputStream());
                    //很多时候 执行正确的adb命令，但是不会返回提示
                    Log.d("adb_command:", msg);

                    if (listener != null) {
                        listener.onSuccess(exitCode, msg);
                    }
                } catch (Exception e) {
                    Log.e("adb_command_error:", e.getMessage());
                    if (listener != null) {
                        listener.onError(-1, e.getMessage());
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void simulateKeystroke(final int KeyCode) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyCode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private static String getMsg(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(UTF_8.name());
    }


    public interface OnAdbCommandListener {
        void onSuccess(int code, String msg);

        void onError(int code, String msg);
    }

}
