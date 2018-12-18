//package com.archeanx.lib.tv.uninstall;
//
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.xz.xadapter.XRvPureDataAdapter;
//import com.xz.xadapter.xutil.XRvViewHolder;
//
///**
// * @author DEV
// */
//public class UnInstallAdapter extends XRvPureDataAdapter<PackageInfo> {
//    private PackageManager mPackageManager;
//
//    public UnInstallAdapter(PackageManager packageManager) {
//        this.mPackageManager = packageManager;
//    }
//
//    @Override
//    public int getItemLayout(int viewType) {
//        return R.layout.lib_item_un_install;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull XRvViewHolder holder, int position) {
//        ImageView img = holder.getView(R.id.lib_iui_img);
//        TextView nameTv = holder.getView(R.id.lib_iui_name);
//        TextView typeTv = holder.getView(R.id.lib_iui_type);
//
//        PackageInfo packageInfo = mDatas.get(position);
//        if (packageInfo.applicationInfo.loadIcon(mPackageManager) != null) {
//            img.setImageDrawable(packageInfo.applicationInfo.loadIcon(mPackageManager));
//        } else {
//            img.setImageResource(R.drawable.ic_launcher);
//        }
//        if (packageInfo.applicationInfo.loadLabel(mPackageManager) != null) {
//            nameTv.setText(packageInfo.applicationInfo.loadLabel(mPackageManager));
//        } else {
//            nameTv.setText("");
//        }
//
//        if (TextUtils.equals(packageInfo.sharedUserId, "android.uid.system")) {
//            typeTv.setText("系统应用 ("+packageInfo.versionName+")");
//        } else {
//            typeTv.setText("普通应用 ("+packageInfo.versionName+")");
//        }
//    }
//}
