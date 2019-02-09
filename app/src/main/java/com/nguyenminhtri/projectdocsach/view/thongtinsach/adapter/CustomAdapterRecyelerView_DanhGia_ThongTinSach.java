package com.nguyenminhtri.projectdocsach.view.thongtinsach.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;

import java.util.ArrayList;

public class CustomAdapterRecyelerView_DanhGia_ThongTinSach extends RecyclerView.Adapter<CustomAdapterRecyelerView_DanhGia_ThongTinSach.ViewHodel> {

    Context context;
    ArrayList<DanhGia> list;
    int limit = -1;

    public CustomAdapterRecyelerView_DanhGia_ThongTinSach(Context context, ArrayList<DanhGia> list) {
        this.context = context;
        this.list = list;
    }

    public CustomAdapterRecyelerView_DanhGia_ThongTinSach(Context context, ArrayList<DanhGia> list, int limit) {
        this.context = context;
        this.list = list;
        this.limit = limit;
    }

    @Override
    public ViewHodel onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_row_recyclerview_danhgia_thongtinsach, viewGroup, false);

        ViewHodel viewHodel = new ViewHodel(view);

        return viewHodel;
    }

    @Override
    public void onBindViewHolder(ViewHodel viewHodel, int i) {
        DanhGia danhGia = list.get(i);

        viewHodel.tvTieuDe.setText(danhGia.getTieuDe());
        viewHodel.tvNgayDang.setText(danhGia.getNgayDanhGia());
        viewHodel.tvNoiDung.setText(danhGia.getNoiDung());
        viewHodel.tvTenUser.setText("Đăng bởi " + danhGia.getTenUser());
        viewHodel.rbDanhGia.setRating(Float.parseFloat(danhGia.getSoSao()));
    }

    @Override
    public int getItemCount() {
        if (limit == -1) {
            return list.size();
        } else {
            if (limit == 0 || (list.size() < limit)) {
                return list.size();
            } else {
                return limit;
            }
        }


    }

    public class ViewHodel extends RecyclerView.ViewHolder {
        TextView tvTieuDe, tvNgayDang, tvNoiDung, tvTenUser;
        RatingBar rbDanhGia;

        public ViewHodel(View itemView) {
            super(itemView);
            tvTieuDe = itemView.findViewById(R.id.tvTieuDeDanhGia);
            tvNgayDang = itemView.findViewById(R.id.tvNgayDang);
            tvNoiDung = itemView.findViewById(R.id.tvNoiDungDanhGia);
            tvTenUser = itemView.findViewById(R.id.tvTenUserDanhGia);
            rbDanhGia = itemView.findViewById(R.id.rbDanhGia);
        }
    }
}
