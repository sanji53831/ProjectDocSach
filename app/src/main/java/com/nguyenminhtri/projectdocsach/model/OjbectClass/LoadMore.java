package com.nguyenminhtri.projectdocsach.model.OjbectClass;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class LoadMore extends RecyclerView.OnScrollListener {
    int frist = 0;
    int total = 0;
    int last = 0;
    int state;
    Boolean load = false;
    RecyclerView.LayoutManager layoutManager;
    InterfaceLoadMore interfaceLoadMore;

    public LoadMore(RecyclerView.LayoutManager layoutManager, InterfaceLoadMore interfaceLoadMore) {
        this.layoutManager = layoutManager;
        this.interfaceLoadMore = interfaceLoadMore;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        state = newState;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        total = layoutManager.getItemCount();

        if (layoutManager instanceof LinearLayoutManager) {
            frist = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

        } else if (layoutManager instanceof GridLayoutManager) {
            frist = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            last = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        if (last == (total - 1) && !load) {
            interfaceLoadMore.xuLyLoadMore(total);
        }
    }

    public void setLoad(Boolean load) {
        this.load = load;
    }

}
