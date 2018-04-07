package dev.momo.library.connect.data.recycler;

import java.io.IOException;
import java.util.List;

import dev.momo.library.connect.data.parser.ObjectParser;
import dev.momo.library.core.data.recycler.ListRecyclerAdapter;
import dev.momo.library.core.data.recycler.ObjectViewHolder;
import dev.momo.library.core.log.Logger;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Use @link RefreshableProvider.class instead
 * @param <T>
 * @param <VH>
 */
@Deprecated
public abstract class RefreshableRecyclerAdapter<T, VH extends ObjectViewHolder<T>> extends ListRecyclerAdapter<T, VH> {

    private final String TAG = RefreshableRecyclerAdapter.class.getSimpleName();

    private int loadCount = 20;
    private int needLoadMoreOffset = 5;

    protected boolean fetching = false;
    protected boolean noMore = false;
    protected boolean refresh = false;


    protected abstract void onRefreshDone(boolean success);

    /**
     * @return T: parse item to what list want, return null to ignore this item from list
     */

    protected abstract ObjectParser<T, ?> getParser();

    protected abstract Call<List<?>> getApiCell();

    protected int getLoadCount() {
        return loadCount;
    }

    protected int getLoadOffset() {
        if (isRefresh()) {
            return 0;
        }
        return getItemCount();
    }

    protected int getNeedLoadMoreOffset() {
        return needLoadMoreOffset;
    }

    public void setLoadCount(int count) {
        this.loadCount = count;
    }

    public void setNeedLoadMoreOffset(int needLoadMoreOffset) {
        this.needLoadMoreOffset = needLoadMoreOffset;
    }

    protected boolean needLoadMore(int position) {
        return !noMore && getItemCount() - position < getNeedLoadMoreOffset();
    }

    public boolean isFetching() {
        return fetching;
    }


    public boolean isRefresh() {
        return refresh;
    }

    public void reload() {
        refresh = true;
        load();
    }


    public synchronized void load() {
        if (!refresh && fetching) {
            return;
        }

        if (getApiCell() == null) {
            return;
        }

        fetching = true;

        getApiCell().enqueue(new Callback<List<?>>() {
            @Override
            public void onResponse(Call<List<?>> call, Response<List<?>> response) {
                fetching = false;
                if (response == null) {
                    Logger.ES(TAG, "get null response");
                    onRefreshDone(false);
                    return;
                }

                ObjectParser parser = getParser();
                if (parser == null) {
                    Logger.E(TAG, new NullPointerException("empty parser"));
                    return;
                }

                // check if response has error body
                try {
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        String errormessage = errorBody.string();
                        Logger.ES(TAG, "refresh failed: %s", errormessage);
                        onRefreshDone(false);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // parse response to items
                List<T> list = getParser().parseResponse(response);
                if (list == null) {
                    Logger.ES(TAG, "get null data");
                    onRefreshDone(false);
                    return;
                }

                if (refresh) {
                    // On refresh, clean old data
                    refresh = false;
                    notifyItemRangeRemoved(0, data.size());
                    data.clear();
                }

                Logger.I(TAG, "update item %d", list.size());
                for (T t : list) {
//                    Logger.I(TAG, "adapter add %s", SimpleMapper.toString(t));
                    add(t);
                }

                if (list.size() < getLoadCount()) {
                    noMore = true;
                }
                onRefreshDone(true);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Logger.ES(TAG, "call api onFailure");
                fetching = false;
                onRefreshDone(false);
            }
        });


    }

    public void clear() {
        noMore = false;
        refresh = false;
        fetching = false;

        super.clear();
    }

}