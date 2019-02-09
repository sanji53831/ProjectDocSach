package com.nguyenminhtri.projectdocsach.view.tusach.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.view.loadsach.LoadSach;
import com.nguyenminhtri.projectdocsach.view.thongtinsach.ThongTinSach;
import com.nguyenminhtri.projectdocsach.view.tusach.TuSach;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CustomAdapter_Recycler_TuSach extends RecyclerView.Adapter<CustomAdapter_Recycler_TuSach.ViewHodelTuSach> {

    Context context;
    ArrayList<Sach> listSach;
    Database database;
    KhachHang kh;
    ConnectInternet connectInternet;

    public CustomAdapter_Recycler_TuSach(KhachHang kh, Database database, Context context, ArrayList<Sach> listSach) {
        this.kh = kh;
        this.database = database;
        this.context = context;
        this.listSach = listSach;
        connectInternet = new ConnectInternet(context);
    }

    @NonNull
    @Override
    public ViewHodelTuSach onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_tusach, viewGroup, false);

        ViewHodelTuSach viewHodelTuSach = new ViewHodelTuSach(view);

        return viewHodelTuSach;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodelTuSach viewHodelTuSach, int i) {
        final Sach sach = listSach.get(i);

        String total = sach.getTongsotrang();
        int totalpage = Integer.parseInt(total);
        int page = database.getPagerCurrentSachDaDoc(kh.getId(), sach.getId());
        int tiendo = page * 100 / totalpage;
        if (tiendo >= 97 && tiendo <= 99) {
            tiendo = 100;
        }
        viewHodelTuSach.imageView.setImageBitmap(sach.getBitmap());
        viewHodelTuSach.tvTenSach.setText(sach.getTenSach());
        viewHodelTuSach.tvTienDo.setText(String.valueOf(tiendo) + "%");

        viewHodelTuSach.progressBar.setProgress(tiendo);

        viewHodelTuSach.tvXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogThongTin(sach);
            }
        });

    }

    private void showDialogThongTin(Sach sach) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thongtinchitietsach);
        TextView tvTenSach, tvTacgia, tvTheLoai, tvNhaXuatBan, tvNgayXuatBan, tvMoTa;
        ImageButton imgClose;
        tvTenSach = dialog.findViewById(R.id.tvTenSach);
        tvTacgia = dialog.findViewById(R.id.tvTacGia);
        tvTheLoai = dialog.findViewById(R.id.tvTheLoai);
        tvNhaXuatBan = dialog.findViewById(R.id.tvNhaXuatBan);
        tvNgayXuatBan = dialog.findViewById(R.id.tvNgayXuatBan);
        tvMoTa = dialog.findViewById(R.id.tvMoTa);
        imgClose = dialog.findViewById(R.id.imgButtonCloseDialog);

        tvTenSach.setText(sach.getTenSach());
        tvTacgia.setText(sach.getTenTacGia());
        tvTheLoai.setText(sach.getTenTheLoai());
        tvNhaXuatBan.setText(sach.getTenNhaXuatBan());
        tvNgayXuatBan.setText(sach.getNgayXuatBan());
        tvMoTa.setText(sach.getMoTa());
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public int getItemCount() {
        return listSach.size();
    }

    class ViewHodelTuSach extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTenSach, tvTienDo, tvXemThem;
        ProgressBar progressBar;
        Button btnDocSach;

        public ViewHodelTuSach(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgHinhSach);
            tvTenSach = itemView.findViewById(R.id.tvTenSach);
            tvTienDo = itemView.findViewById(R.id.tvTienDo);
            tvXemThem = itemView.findViewById(R.id.tvXemChiTiet);
            progressBar = itemView.findViewById(R.id.progress_barTienDo);
            btnDocSach = itemView.findViewById(R.id.btnDocSach);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int REQUEST_CODE_THONGTINSACH = 200;
                    if (!connectInternet.checkInternetConnection()) {
                        return;
                    } else {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        listSach.get(getLayoutPosition()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        Intent intent = new Intent(context, ThongTinSach.class);
                        intent.putExtra("Sach", listSach.get(getLayoutPosition()));
                        intent.putExtra("Hinh", byteArray);
                        ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_CODE_THONGTINSACH);
                        ((AppCompatActivity) context).overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
                    }
                }
            });

            btnDocSach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int REQUEST_CODE_LOADSACH = 100;
                    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                    listSach.get(getLayoutPosition()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
                    byte[] b = arrayOutputStream.toByteArray();
                    Intent intent = new Intent(context.getApplicationContext(), LoadSach.class);
                    intent.putExtra("Sach", listSach.get(getLayoutPosition()));
                    intent.putExtra("HinhAnh", b);
                    ((AppCompatActivity) context).startActivityForResult(intent, REQUEST_CODE_LOADSACH);
                }
            });
        }
    }
}
