package ticketsystem.manager;

import android.content.Context;


import library.listview.manager.BaseGroupListManager;
import library.model.ListData;
import library.util.TimeUtils;
import rx.functions.Action1;
import rx.functions.Func1;
import ticketsystem.api.DataManager;
import ticketsystem.bean.TicketOpenData;
import rx.Observable;

/**
 * @author xiaolong
 * @version v1.0
 * @function <描述功能>
 * @date 2017年7月13日 15:24:51
 */
public class HistoryManager extends BaseGroupListManager<TicketOpenData> {

    public static final String PAGE_SIZE = "10";
    public String mCode;
    public long endTimeMill;

    public HistoryManager(String code) {
        mCode = code;
    }

    @Override
    protected Observable<ListData<TicketOpenData>> getData(Context context) {
        if (currentPage == 1) {
            endTimeMill = System.currentTimeMillis();
        }
        return DataManager.getMutiPeriodCheck(mCode, PAGE_SIZE, TimeUtils.milliseconds2String(endTimeMill))
                .flatMap(new Func1<ListData<TicketOpenData>, Observable<ListData<TicketOpenData>>>() {
                    @Override
                    public Observable<ListData<TicketOpenData>> call(ListData<TicketOpenData> ticketOpenDataListData) {
                        ticketOpenDataListData.size = ticketOpenDataListData.list.size() + 1000;
                        return Observable.just(ticketOpenDataListData);
                    }
                }).doOnNext(new Action1<ListData<TicketOpenData>>() {
                    @Override
                    public void call(ListData<TicketOpenData> ticketOpenDataListData) {
                        int size = ticketOpenDataListData.list.size();
                        endTimeMill = (ticketOpenDataListData.list.get(size - 1).timestamp - 1) * 1000;
                    }
                });
    }

}
