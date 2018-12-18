//package com.archeanx.lib.tv.uninstall;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.xz.xadapter.XRvPureAdapter;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author xz
// * 获取盒子上的应用，并进行操作
// */
//public class UnInstallActivity extends AppCompatActivity {
//    /**
//     * 开启此Activity的键位密码  2846987
//     */
//    private static final String KEY_PAS = KeyEvent.KEYCODE_2 + " " + KeyEvent.KEYCODE_8 + " " +
//            KeyEvent.KEYCODE_4 + " " + KeyEvent.KEYCODE_6 + " " +
//            KeyEvent.KEYCODE_9 + " " + KeyEvent.KEYCODE_8 + " " + KeyEvent.KEYCODE_7 + " ";
//
//    private static String KEY_CODE = "";
//    private static int showIndex = 0;
//    private static String PACK_NAME = "pack_name";
//    private String mPackName;
//
//    /**
//     * @param context  context
//     * @param keyCode  键位
//     * @param packName 不显示在应用管理的包名
//     */
//    public static void start(Context context, int keyCode, String packName) {
//        if (KEY_PAS.contains(Integer.toString(keyCode))) {
//            showIndex++;
//            KEY_CODE += keyCode + " ";
//            if (showIndex == 7) {
//                if (TextUtils.equals(KEY_PAS, KEY_CODE)) {
//                    context.startActivity(new Intent(context, UnInstallActivity.class).putExtra(PACK_NAME, packName));
//                }
//                KEY_CODE = "";
//                showIndex = 0;
//            }
//        } else {
//            KEY_CODE = "";
//            showIndex = 0;
//        }
//    }
//
//
//    private UnInstallAdapter mAdapter;
//    private TextView mTitleTv;
//    private AlertDialog mDetailDialog;
//    private RecyclerView mRv;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.lib_un_install_activity);
//
//        mTitleTv = findViewById(R.id.lib_uia_tv);
//
//        mRv = findViewById(R.id.lib_uia_rv);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
//        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
//        mRv.setLayoutManager(gridLayoutManager);
//
//        mAdapter = new UnInstallAdapter(getPackageManager());
//        mRv.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new XRvPureAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                showDetailInfo(position, mAdapter.getItem(position));
//            }
//        });
//        mPackName = getIntent().getStringExtra(PACK_NAME);
//        mRv.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                initData();
//            }
//        }, 300);
//    }
//
//    private void initData() {
//        PackageManager packageManager = getPackageManager();
//        //获取手机系统的所有APP包名，然后进行一一比较
//        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
//
//        List<PackageInfo> lists = new ArrayList<>();
//        for (PackageInfo pi : pinfo) {
//            if (TextUtils.equals(pi.packageName, mPackName)) {
//                continue;
//            }
//            if (pi.packageName.contains("com.android")) {
//                continue;
//            }
//            if (pi.applicationInfo.sourceDir.contains("/system/app/")) {
//                continue;
//            }
//            lists.add(pi);
//        }
//        mAdapter.setDatas(lists, true);
//
//        mTitleTv.setText("应用管理 (" + lists.size() + ")");
//    }
//
//
//    private void showDetailInfo(final int position, final PackageInfo info) {
//        View detailView = getLayoutInflater().inflate(R.layout.lib_dialog_view, null);
//        ImageView img = detailView.findViewById(R.id.lib_dv_img);
//        final TextView name = detailView.findViewById(R.id.lib_dv_name);
//        TextView pack = detailView.findViewById(R.id.lib_dv_pack);
//        TextView versionCode = detailView.findViewById(R.id.lib_dv_version_code);
//        TextView versionName = detailView.findViewById(R.id.lib_dv_version_name);
//        TextView source = detailView.findViewById(R.id.lib_dv_source);
//        TextView type = detailView.findViewById(R.id.lib_dv_type);
//        TextView deleteApp = detailView.findViewById(R.id.lib_dv_delete);
//        TextView cancelDialog = detailView.findViewById(R.id.lib_dv_cancel);
//        TextView openApp = detailView.findViewById(R.id.lib_dv_open);
//        //打开app
//        openApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent resolveIntent = getPackageManager().getLaunchIntentForPackage(info.packageName);
//                    startActivity(resolveIntent);
//                } catch (Exception e) {
//                    Toast.makeText(getApplicationContext(), "无法打开该应用!", Toast.LENGTH_LONG).show();
//                    e.printStackTrace();
//                }
//            }
//        });
//        //卸载app
//        deleteApp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(UnInstallActivity.this).setTitle("警告").setMessage("确认卸载 " + name.getText().toString() + " App ?")
//                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (uninstall(info.packageName)) {
//                                    Toast.makeText(getApplicationContext(), "卸载成功!", Toast.LENGTH_LONG).show();
//                                    if (mDetailDialog != null && mDetailDialog.isShowing()) {
//                                        mDetailDialog.cancel();
//                                        mDetailDialog = null;
//                                    }
//                                    mAdapter.notifyItemRangeRemoved(position, 1);
//                                    mAdapter.getDatas().remove(position);
//                                    mRv.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mRv.getChildAt(position).requestFocus();
//                                            mTitleTv.setText("应用管理 (" + mAdapter.getDatas().size() + ")");
//                                        }
//                                    }, 50);
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "卸载失败!", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        }).setNeutralButton("取消", null).show();
//            }
//        });
//        //关闭dialog
//        cancelDialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDetailDialog != null && mDetailDialog.isShowing()) {
//                    mDetailDialog.cancel();
//                    mDetailDialog = null;
//                }
//            }
//        });
//        cancelDialog.requestFocus();
//
//        if (info.applicationInfo.loadIcon(getPackageManager()) != null) {
//            img.setImageDrawable(info.applicationInfo.loadIcon(getPackageManager()));
//        } else {
//            img.setImageResource(R.drawable.ic_launcher);
//        }
//        if (info.applicationInfo.loadLabel(getPackageManager()) != null) {
//            name.setText(info.applicationInfo.loadLabel(getPackageManager()));
//        } else {
//            name.setText("");
//        }
//        versionCode.setText("VersionCode：" + info.versionCode);
//
//        versionName.setText("VersionName：" + info.versionName);
//
//        source.setText("安装包路径：" + info.applicationInfo.sourceDir);
//
//        pack.setText("包名：" + info.packageName);
//        if (TextUtils.equals(info.sharedUserId, "android.uid.system")) {
//            type.setText("系统应用");
//        } else {
//            type.setText("普通应用");
//        }
//
//        mDetailDialog = new AlertDialog.Builder(this).setView(detailView).show();
//    }
//
//
//    /**
//     * 卸载
//     */
//    private boolean uninstall(String packageName) {
//        boolean isUn = false;
//        try {
//            PackageManager pm = getApplicationContext().getPackageManager();
//            Method[] methods = pm != null ? pm.getClass().getDeclaredMethods() : null;
//            Method mDel = null;
//            if (methods != null && methods.length > 0) {
//                for (Method method : methods) {
//                    if (method.getName().toString().equals("deletePackage")) {
//                        mDel = method;
//                        break;
//                    }
//                }
//            }
//            if (mDel != null) {
//                mDel.setAccessible(true);
//                mDel.invoke(pm, packageName, null, 0);
//            }
//            isUn = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return isUn;
//    }
//}
