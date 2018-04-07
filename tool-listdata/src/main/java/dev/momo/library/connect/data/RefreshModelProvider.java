package dev.momo.library.connect.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import dev.momo.library.connect.data.parser.ObjectParser;
import dev.momo.library.core.data.DataModel;
import dev.momo.library.core.data.DataModelProvider;
import dev.momo.library.core.log.Logger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * to get refresh data from server and parse into @link AdapterDataProvider
 * <p>
 * only refresh data when option is on main ui thread
 * <p>
 * Created by Peng on 2017/6/13.
 */
public class RefreshModelProvider<M extends DataModel<P>, P> extends DataModelProvider<M, P> {

    private final String TAG = RefreshModelProvider.class.getSimpleName();

    protected boolean fetching = false;
    protected boolean noMore = false;
    protected boolean refresh = false;

    @Nullable
    protected PagingOption pageOption;
    @Nullable
    protected OnRefreshListener<P> refreshOption;
    @Nullable
    protected RefreshCellHolder<M, P> refreshCellHolder;

    public RefreshModelProvider() {
        super();
        fetching = false;
        noMore = false;
        refresh = false;
    }

    @Override
    public void unbind() {
        super.unbind();
        this.refreshOption = null;
        this.pageOption = null;
        this.refreshCellHolder = null;
    }


    public void bind(RecyclerView.Adapter adapter, RefreshCellHolder remotCell) {
        bind(adapter, null, null, remotCell);
    }

    public void bind(RecyclerView.Adapter adapter, @Nullable OnRefreshListener refresh, RefreshCellHolder remotCell) {
        bind(adapter, null, refresh, remotCell);
    }


    public void bind(RecyclerView.Adapter adapter, @Nullable OnNotifyListener<P> notifyListener, @Nullable OnRefreshListener refresh, RefreshCellHolder remotCell) {
        bind(adapter, notifyListener, refresh, null, remotCell);
    }


    public void bind(RecyclerView.Adapter adapter, @Nullable OnNotifyListener<P> notifyListener, @Nullable OnRefreshListener refresh, @Nullable PagingOption page, RefreshCellHolder remotCell) {
        super.bind(adapter, notifyListener);
        this.refreshOption = refresh;
        this.pageOption = page;
        this.refreshCellHolder = remotCell;

        // call on refresh done again when bind again
        this.refreshOption.onRefreshDone(false);
    }


    @Nullable
    @Override
    public P get(int position) {
        // ask load more, on item get
        askLoadMore(position);
        return super.get(position);
    }

    private boolean isPagable() {
        return pageOption != null;
    }

    private boolean isRefreshable() {
        return refreshOption != null;
    }

    public boolean isFetching() {
        return fetching;
    }

    // Not recommended for use
    public void setFetching(boolean b) {
        fetching = b;
    }

    /**
     * Refresh Method
     */
    public void reload() {
        if (!isRefreshable()) {
            Logger.WS(TAG, "provider is not refreshable");
            return;
        }
        refresh = true;
        noMore = false;
        load();
    }

    public int getLoadOffset() {
        if (refresh) return 0;
        return count();
    }


    /**
     * Paging Method
     */
    protected void askLoadMore(int position) {
        if (!isPagable()) return;
        if (isFetching()) return;
        if (noMore) return;
        if (count() - position < pageOption.getPageItemCount()) {
            // load more
            load();
        }
    }

    public synchronized void load() {
        if (!refresh && fetching) {
            return;
        }

        if (refreshCellHolder == null) return;
        fetching = true;
        refreshCellHolder.getRefreshApiCell().enqueue(new Callback<List<M>>() {
            @Override
            public void onResponse(Call<List<M>> call, Response<List<M>> response) {
                if (response == null) {
                    Logger.ES(TAG, "get null response");
                    onLoadFinish(null);
                    return;
                }

                // check if response has error body
                try {
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        String errormessage = errorBody.string();
                        Logger.ES(TAG, "refresh failed: %s", errormessage);
                        onLoadFinish(null);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // parse response to items
                if (refreshCellHolder != null) {
                    List<P> list = refreshCellHolder.getRefreshParser().parseResponse(response);
                    onLoadFinish(list);
                } else {
                    onLoadFinish(null);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Logger.ES(TAG, "call api onFailure");
                onLoadFinish(null);
            }
        });
    }

    protected synchronized void onLoadFinish(@Nullable List<P> newItems) {
        fetching = false;
        boolean loadSuccess;
        if (newItems == null) {
            Logger.ES(TAG, "get null data");
            loadSuccess = false;
        } else {
            loadSuccess = true;
            if (refresh) {
                // On refresh, clean old data
                clear();
            }

            int newItemCount = newItems.size();
            Logger.I(TAG, "update item %d", newItemCount);
            for (P p : newItems) {
                add(p);
            }
            if (isPagable()) {
                if (newItemCount < pageOption.getPageItemCount()) {
                    noMore = true;
                } else {
                    noMore = false;
                }
            }
        }
        refresh = false;

        if (refreshOption == null) return;
        refreshOption.onRefreshDone(loadSuccess);
    }


    /**
     * @Param <L> item in list
     * <p>
     * Created by Peng on 2017/6/14.
     */

    public interface OnRefreshListener<L> {
        void onRefreshDone(boolean success);
    }


    /**
     * Paging option
     * <p>
     * Created by Peng on 2017/6/14.
     */

    public interface PagingOption {

        int getPageItemCount();
    }


    /**
     * Use for refreshable and pageable list activity
     *
     * @param <S> item in Server
     * @param <L> item in Shown List
     */
    public interface RefreshCellHolder<S extends DataModel<L>, L> {

        /**
         * parse item to what list want, return null to ignore this item from list
         */
        @NonNull
        ObjectParser<L, S> getRefreshParser();

        @NonNull
        Call<List<S>> getRefreshApiCell();
    }


}
