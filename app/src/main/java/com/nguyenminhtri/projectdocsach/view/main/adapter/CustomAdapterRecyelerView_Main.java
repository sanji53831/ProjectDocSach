package com.nguyenminhtri.projectdocsach.view.main.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.view.main.MainActivity;
import com.nguyenminhtri.projectdocsach.view.thongtinsach.ThongTinSach;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class CustomAdapterRecyelerView_Main extends RecyclerView.Adapter<CustomAdapterRecyelerView_Main.ViewHodel> {
    Context context;
    int layout;
    ArrayList<Sach> list;

    public CustomAdapterRecyelerView_Main(Context context, int layout, ArrayList<Sach> list) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public ViewHodel onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(layout, viewGroup, false);

        ViewHodel viewHodel = new ViewHodel(view);

        return viewHodel;
    }

    @Override
    public void onBindViewHolder(ViewHodel viewHodel, int i) {
        Sach sach = list.get(i);
        viewHodel.textView.setText(sach.getTenSach());
        Picasso.get().load(sach.getHinhAnh()).into(viewHodel.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHodel extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        ImageView imageView;

        public ViewHodel(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cartViewItem);
            textView = itemView.findViewById(R.id.tvTenSachMain);
            imageView = itemView.findViewById(R.id.imgSachMain);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageView.getDrawable() == null) {
                        return;
                    }
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Intent intent = new Intent(context, ThongTinSach.class);
                    intent.putExtra("Sach", list.get(getLayoutPosition()));
                    intent.putExtra("Hinh", byteArray);
                    ((AppCompatActivity) context).startActivityForResult(intent, 1);
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
                }
            });

        }
    }

}
